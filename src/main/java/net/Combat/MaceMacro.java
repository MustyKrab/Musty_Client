package net.maceauto;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MaceAutoSwap implements ModInitializer, ClientModInitializer {
    
    private static final String MOD_ID = "maceautoswap";
    
    // Configurable hotbar slots (0-8)
    private static final int DENSITY_MACE_SLOT = 8;  // Slot 9 (0-indexed)
    private static final int BREACH_MACE_SLOT = 5;   // Slot 6 (0-indexed)
    
    // Toggle key: Mouse Button 5 (forward side button)
    private static KeyBinding toggleKey;
    private static boolean enabled = true;
    
    // Fall tracking
    private float peakFallDistance = 0.0f;
    private boolean wasInAir = false;
    
    @Override
    public void onInitialize() {
        System.out.println("[MaceAutoSwap] Mod initialized!");
        System.out.println("[MaceAutoSwap] Density mace: Slot 9 | Breach mace: Slot 6");
        System.out.println("[MaceAutoSwap] Toggle: Mouse Button 5");
    }
    
    @Override
    public void onInitializeClient() {
        // Register toggle key - Mouse Button 5
        // GLFW doesn't have constants for mouse buttons 4+5, using raw values
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.maceautoswap.toggle",
            InputUtil.Type.MOUSE,
            6,  // Mouse Button 5 is button 6 (Button 4 = 5, Button 5 = 6)
            "category.maceautoswap"
        ));
        
        // Main tick handler
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle toggle key
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.sendMessage(
                        Text.literal("§7[MaceAutoSwap] §" + (enabled ? "aEnabled" : "cDisabled")),
                        true
                    );
                }
            }
            
            if (!enabled) return;
            if (client.player == null) return;
            
            PlayerEntity player = client.player;
            
            // Get current fall distance (direct field access!)
            float currentFall = player.fallDistance;
            boolean isInAir = currentFall > 0.05f;
            
            // Track peak fall distance
            if (isInAir && currentFall > peakFallDistance) {
                peakFallDistance = currentFall;
            }
            
            // On landing (just left air)
            if (wasInAir && !isInAir && peakFallDistance > 0.1f) {
                handleLanding(player, peakFallDistance);
                peakFallDistance = 0.0f;
            }
            
            wasInAir = isInAir;
        });
        
        System.out.println("[MaceAutoSwap] Client initialized! Toggle: Mouse Button 5");
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
        
        if (targetSlot != -1 && targetSlot != player.getInventory().selectedSlot) {
            // Check if the target slot actually has a mace
            ItemStack stack = player.getInventory().getStack(targetSlot);
            if (stack.getItem() == Items.MACE) {
                player.getInventory().selectedSlot = targetSlot;
                player.sendMessage(
                    Text.literal("§7[MaceAutoSwap] §aSwapped to " + maceType + " mace §7(§f" + 
                    String.format("%.1f", fallDistance) + " blocks§7)"),
                    true
                );
            } else if (!stack.isEmpty()) {
                player.sendMessage(
                    Text.literal("§7[MaceAutoSwap] §cSlot " + (targetSlot + 1) + " is not a mace!"),
                    true
                );
            }
        }
    }
}
