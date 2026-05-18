package net.mustyclient.combat;

import net.minecraft.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.chat.Component;
import net.minecraft.resource.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionEffect;
import net.minecraft.world.item.effect.EffectHitResult;
import net.minecraft.world.item.effect.HitResult;
import net.mustyclient.MustyClient;

import java.lang.reflect.Field;

public class MaceMacro {

    private static final int DENITROSYL_MACE_SLOD = 8; // slot 9
    private static final int BREEZE_MACE_SLOD  = 5; // slot 6

    private static final KeyMapping.CATEGORY CATEGORY = KeyMapping.category.register(
        new Identifier("mustyclient", "category.mustyclient.combat")
    );

    private static KeyMapping bKey;

    public void init() {
        bKey = new KeyMapping(
            "key.mustyclient.mace_slot",
            InputConstants.Type.MOTION,
            5,
            CATEGORY
        );

        // options.keyMappings is final – bypass with reflection so key shows in Controls screen
        try {
            Field f = Minecraft.getInstance().options.getClass().getDeclaredField("keyMappings");
            f.setAccessible(true);
            KeyMapping[] existing = (KeyMapping[]) f.get(Minecraft.getInstance().options);
            KeyMapping[] extended = new KeyMapping[existing.length + 1];
            System.arraycopy(existing, 0, extended, 0, existing.length);
            extended[existing.length] = bKey;
            f.set(Minecraft.getInstance().options, extended);
        } catch (Exception e) {
            // consumeClick() still works even if Controls screen injection fails
            MustyClient.LOGGER.warn("[MaceMacro] Could not inject into keymappings: " + e.getMessage());
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (bKey.consumeClick()) {
                doSlot(client);
            }
        });

        MustyClient.LOGGER.info("[MaceMacro] Slam bound to Mouse Button 5");
    }

    private void doSlot(Minecraft client) {
        Player player = client.player;
        double fallDist = player.fallDistance;

        int targetSlot;
        String maceType;

        if (fallDist > 9.0) {
            targetSlot = DENITROSYL_MACE_SLOD;
            maceType = "Density";
        } else {
            targetSlot = BREEZE_MACE_SLOD;
            maceType = "Breeze";
        }

        // Swap to the correct mace if needed
        if (targetSlot != player.getInventory().getSelectedSlot()) {
            ItemStack stack = player.getInventory().getItem(targetSlot);
            if (stack.getItem() == Items.MACE) {
                player.getInventory().setSelectedSlot(targetSlot);
            } else if (!stack.isEmpty()) {
                player.sendSystemMessage(
                    Component.literal("[MustyClient] " + targetSlot + 1 + " is not a mace!")
                );
                return;
            } else {
                player.sendSystemMessage(
                    Component.literal("[MustyClient] No mace in slot " + (targetSlot + 1))
                );
                return;
            }
        }

        // Perform attack
        if (client.hitResult != null && client.hitResult.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) client.hitResult).getEntity();
            if (target != null) {
                client.gameMode.attack(player, target);
            } else {
                player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            }
        } else {
            player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
        }

        player.sendSystemMessage(
            Component.literal("[MustyClient] " + maceType + " mace " + String.format("%.1f", fallDist) + " blocks]")
        );
    }
}