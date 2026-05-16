package net.mustyclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.mustyclient.MustyClient;
import com.mojang.blaze3d.platform.InputConstants;

public class MaceMacro {

    private static final int DENSITY_MACE_SLOT = 8;
    private static final int BREACH_MACE_SLOT  = 5;

    private KeyMapping toggleKey;
    private boolean enabled = true;

    private double peakFallDistance = 0.0;
    private boolean wasInAir = false;

    public void init() {
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.mace_toggle",
                InputConstants.Type.MOUSE,
                5,
                "category.mustyclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.sendSystemMessage(
                            Component.literal("\u00a77[\u00a7bMustyClient\u00a77] MaceMacro " +
                                    (enabled ? "\u00a7aEnabled" : "\u00a7cDisabled"))
                    );
                }
            }

            if (!enabled) return;
            if (client.player == null) return;

            Player player = client.player;

            double currentFall = player.fallDistance;
            boolean isInAir = currentFall > 0.05;

            if (isInAir && currentFall > peakFallDistance) {
                peakFallDistance = currentFall;
            }

            if (wasInAir && !isInAir && peakFallDistance > 0.1) {
                handleLanding(player, peakFallDistance);
                peakFallDistance = 0.0;
            }

            wasInAir = isInAir;
        });

        MustyClient.LOGGER.info("[MaceMacro] Initialized. Toggle: Mouse Button 5");
        MustyClient.LOGGER.info("[MaceMacro] Density slot: {} | Breach slot: {}",
                DENSITY_MACE_SLOT + 1, BREACH_MACE_SLOT + 1);
    }

    private void handleLanding(Player player, double fallDistance) {
        int targetSlot = -1;
        String maceType = "";

        if (fallDistance > 9.0) {
            targetSlot = DENSITY_MACE_SLOT;
            maceType = "Density";
        } else if (fallDistance > 0.1) {
            targetSlot = BREACH_MACE_SLOT;
            maceType = "Breach";
        }

        if (targetSlot == -1) return;
        if (targetSlot == player.getInventory().getSelectedSlot()) return;

        ItemStack stack = player.getInventory().getItem(targetSlot);
        if (stack.getItem() == Items.MACE) {
            player.getInventory().setSelectedSlot(targetSlot);
            player.sendSystemMessage(
                    Component.literal(String.format(
                            "\u00a77[\u00a7bMustyClient\u00a77] \u00a7aSwapped to %s mace \u00a77(\u00a7f%.1f blocks\u00a77)",
                            maceType, fallDistance
                    ))
            );
        } else if (!stack.isEmpty()) {
            player.sendSystemMessage(
                    Component.literal(String.format(
                            "\u00a77[\u00a7bMustyClient\u00a77] \u00a7cSlot %d is not a mace!",
                            targetSlot + 1
                    ))
            );
        }
    }
}
