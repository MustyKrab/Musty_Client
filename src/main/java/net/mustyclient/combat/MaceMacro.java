package net.mustyclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.mustyclient.MustyClient;

public class MaceMacro {

    // Configurable hotbar slots (0-indexed, so slot 9 = index 8, slot 6 = index 5)
    private static final int DENSITY_MACE_SLOT = 8;
    private static final int BREACH_MACE_SLOT  = 5;

    private KeyBinding toggleKey;
    private boolean enabled = true;

    // Fall tracking
    private float peakFallDistance = 0.0f;
    private boolean wasInAir = false;

    public void init() {
        // Register toggle key — Mouse Button 5 (forward side button)
        // GLFW mouse button 5 = index 6 (0=left,1=right,2=middle,3=back,4=forward... actually GLFW_MOUSE_BUTTON_6 = 5)
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mustyclient.mace_toggle",
                InputUtil.Type.MOUSE,
                5, // GLFW_MOUSE_BUTTON_6 — forward side button
                "category.mustyclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle toggle
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.sendMessage(
                            Text.literal("\u00a77[\u00a7bMustyClient\u00a77] MaceMacro " +
                                    (enabled ? "\u00a7aEnabled" : "\u00a7cDisabled")),
                            true
                    );
                }
            }

            if (!enabled) return;
            if (client.player == null) return;

            PlayerEntity player = client.player;

            float currentFall = player.fallDistance;
            boolean isInAir = currentFall > 0.05f;

            // Track peak fall distance while airborne
            if (isInAir && currentFall > peakFallDistance) {
                peakFallDistance = currentFall;
            }

            // On landing (just transitioned from air to ground)
            if (wasInAir && !isInAir && peakFallDistance > 0.1f) {
                handleLanding(player, peakFallDistance);
                peakFallDistance = 0.0f;
            }

            wasInAir = isInAir;
        });

        MustyClient.LOGGER.info("[MaceMacro] Initialized. Toggle: Mouse Button 5");
        MustyClient.LOGGER.info("[MaceMacro] Density mace: Slot {} | Breach mace: Slot {}",
                DENSITY_MACE_SLOT + 1, BREACH_MACE_SLOT + 1);
    }

    private void handleLanding(PlayerEntity player, float fallDistance) {
        int targetSlot = -1;
        String maceType = "";

        if (fallDistance > 9.0f) {
            targetSlot = DENSITY_MACE_SLOT;
            maceType = "Density";
        } else if (fallDistance > 0.1f) {
            targetSlot = BREACH_MACE_SLOT;
            maceType = "Breach";
        }

        if (targetSlot == -1) return;
        if (targetSlot == player.getInventory().selectedSlot) return;

        ItemStack stack = player.getInventory().getStack(targetSlot);
        if (stack.getItem() == Items.MACE) {
            player.getInventory().selectedSlot = targetSlot;
            player.sendMessage(
                    Text.literal(String.format(
                            "\u00a77[\u00a7bMustyClient\u00a77] \u00a7aSwapped to %s mace \u00a77(\u00a7f%.1f blocks\u00a77)",
                            maceType, fallDistance
                    )),
                    true
            );
        } else if (!stack.isEmpty()) {
            player.sendMessage(
                    Text.literal(String.format(
                            "\u00a77[\u00a7bMustyClient\u00a77] \u00a7cSlot %d is not a mace!",
                            targetSlot + 1
                    )),
                    true
            );
        }
    }
}
