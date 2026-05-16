package net.mustyclient.combat;

// MC 26.1 uses official Mojang mappings — class names match Mojang's obfmap
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;  // renamed from KeyBindingHelper
import net.minecraft.client.KeyMapping;                                  // renamed from KeyBinding
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;                             // renamed from Text
import net.minecraft.world.entity.player.Player;                         // renamed from PlayerEntity
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.mustyclient.MustyClient;
import com.mojang.blaze3d.platform.InputConstants;                       // replaces InputUtil

public class MaceMacro {

    // Hotbar slots (0-indexed): slot 9 = index 8, slot 6 = index 5
    private static final int DENSITY_MACE_SLOT = 8;
    private static final int BREACH_MACE_SLOT  = 5;

    private KeyMapping toggleKey;
    private boolean enabled = true;

    private float peakFallDistance = 0.0f;
    private boolean wasInAir = false;

    public void init() {
        // Mouse Button 5 (forward side button) = GLFW button index 5
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.mace_toggle",
                InputConstants.Type.MOUSE,
                5,
                "category.mustyclient"
        ));

        // END_CLIENT_TICK is unchanged in 26.1
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.displayClientMessage(
                            Component.literal("\u00a77[\u00a7bMustyClient\u00a77] MaceMacro " +
                                    (enabled ? "\u00a7aEnabled" : "\u00a7cDisabled")),
                            true
                    );
                }
            }

            if (!enabled) return;
            if (client.player == null) return;

            Player player = client.player;

            float currentFall = player.fallDistance;
            boolean isInAir = currentFall > 0.05f;

            if (isInAir && currentFall > peakFallDistance) {
                peakFallDistance = currentFall;
            }

            if (wasInAir && !isInAir && peakFallDistance > 0.1f) {
                handleLanding(player, peakFallDistance);
                peakFallDistance = 0.0f;
            }

            wasInAir = isInAir;
        });

        MustyClient.LOGGER.info("[MaceMacro] Initialized. Toggle: Mouse Button 5");
        MustyClient.LOGGER.info("[MaceMacro] Density slot: {} | Breach slot: {}",
                DENSITY_MACE_SLOT + 1, BREACH_MACE_SLOT + 1);
    }

    private void handleLanding(Player player, float fallDistance) {
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
        if (targetSlot == player.getInventory().selected) return;  // .selected = selectedSlot in official mappings

        ItemStack stack = player.getInventory().getItem(targetSlot);  // getItem = getStack in official mappings
        if (stack.getItem() == Items.MACE) {
            player.getInventory().selected = targetSlot;
            player.displayClientMessage(
                    Component.literal(String.format(
                            "\u00a77[\u00a7bMustyClient\u00a77] \u00a7aSwapped to %s mace \u00a77(\u00a7f%.1f blocks\u00a77)",
                            maceType, fallDistance
                    )),
                    true
            );
        } else if (!stack.isEmpty()) {
            player.displayClientMessage(
                    Component.literal(String.format(
                            "\u00a77[\u00a7bMustyClient\u00a77] \u00a7cSlot %d is not a mace!",
                            targetSlot + 1
                    )),
                    true
            );
        }
    }
}
