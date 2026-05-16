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

    private static final int DENSITY_MACE_SLOT = 8; // slot 9
    private static final int BREACH_MACE_SLOT  = 5; // slot 6

    private KeyMapping mb5Key;
    private boolean attackQueued = false;

    public void init() {
        mb5Key = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.mace_slam",
                InputConstants.Type.MOUSE,
                5,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            Player player = client.player;

            while (mb5Key.consumeClick()) {
                doSlam(player);
            }

            if (attackQueued) {
                client.options.keyAttack.setDown(false);
                attackQueued = false;
            }
        });

        MustyClient.LOGGER.info("[MaceMacro] Slam bound to Mouse Button 5");
    }

    private void doSlam(Player player) {
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

        if (targetSlot == player.getInventory().getSelectedSlot()) {
            queueAttack();
            player.sendSystemMessage(
                    Component.literal("\u00a77[\u00a7bMustyClient\u00a77] \u00a7aSlam with " + maceType + " mace \u00a77(\u00a7f" + String.format("%.1f", fallDist) + " blocks\u00a77)")
            );
            return;
        }

        ItemStack stack = player.getInventory().getItem(targetSlot);
        if (stack.getItem() == Items.MACE) {
            player.getInventory().setSelectedSlot(targetSlot);
            queueAttack();
            player.sendSystemMessage(
                    Component.literal("\u00a77[\u00a7bMustyClient\u00a77] \u00a7aSwapped to " + maceType + " mace \u00a77(\u00a7f" + String.format("%.1f", fallDist) + " blocks\u00a77)")
            );
        } else if (!stack.isEmpty()) {
            player.sendSystemMessage(
                    Component.literal("\u00a77[\u00a7bMustyClient\u00a77] \u00a7cSlot " + (targetSlot + 1) + " is not a mace!")
            );
        } else {
            player.sendSystemMessage(
                    Component.literal("\u00a77[\u00a7bMustyClient\u00a77] \u00a7cNo mace in slot " + (targetSlot + 1))
            );
        }
    }

    private void queueAttack() {
        Minecraft.getInstance().options.keyAttack.setDown(true);
        attackQueued = true;
    }
}