package net.mustyclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.mustyclient.MustyClient;
import com.mojang.blaze3d.platform.InputConstants;

import java.lang.reflect.Field;

public class MaceMacro {

    private static final int DENSITY_MACE_SLOT = 8; // slot 9
    private static final int BREACH_MACE_SLOT  = 5; // slot 6

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            ResourceLocation.fromNamespaceAndPath("mustyclient", "category.mustyclient.combat")
    );

    private KeyMapping mb5Key;

    public void init() {
        mb5Key = new KeyMapping(
                "key.mustyclient.mace_slam",
                InputConstants.Type.MOUSE,
                5,
                CATEGORY
        );

        // options.keyMappings is final — bypass with reflection so key shows in Controls screen
        try {
            Field f = Minecraft.getInstance().options.getClass().getDeclaredField("keyMappings");
            f.setAccessible(true);
            KeyMapping[] existing = (KeyMapping[]) f.get(Minecraft.getInstance().options);
            KeyMapping[] extended = new KeyMapping[existing.length + 1];
            System.arraycopy(existing, 0, extended, 0, existing.length);
            extended[existing.length] = mb5Key;
            f.set(Minecraft.getInstance().options, extended);
        } catch (Exception e) {
            // consumeClick() still works even if Controls screen injection fails
            MustyClient.LOGGER.warn("[MaceMacro] Could not inject into keyMappings: " + e.getMessage());
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (mb5Key.consumeClick()) {
                doSlam(client);
            }
        });

        MustyClient.LOGGER.info("[MaceMacro] Slam bound to Mouse Button 5");
    }

    private void doSlam(Minecraft client) {
        Player player = client.player;
        double fallDist = player.fallDistance;

        int targetSlot;
        String maceType;

        if (fallDist > 9.0) {
            targetSlot = DENSITY_MACE_SLOT;
            maceType = "Density";
        } else {
            targetSlot = BREACH_MACE_SLOT;
            maceType = "Breach";
        }

        // Swap to the correct mace if needed
        if (targetSlot != player.getInventory().getSelectedSlot()) {
            ItemStack stack = player.getInventory().getItem(targetSlot);
            if (stack.getItem() == Items.MACE) {
                player.getInventory().setSelectedSlot(targetSlot);
            } else if (!stack.isEmpty()) {
                player.sendSystemMessage(
                        Component.literal("\u00a77[\u00a7bMustyClient\u00a77] \u00a7cSlot " + (targetSlot + 1) + " is not a mace!")
                );
                return;
            } else {
                player.sendSystemMessage(
                        Component.literal("\u00a77[\u00a7bMustyClient\u00a77] \u00a7cNo mace in slot " + (targetSlot + 1))
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
                Component.literal("\u00a77[\u00a7bMustyClient\u00a77] \u00a7aSlammed with " + maceType + " mace \u00a77(\u00a7f" + String.format("%.1f", fallDist) + " blocks\u00a77)")
        );
    }
}
