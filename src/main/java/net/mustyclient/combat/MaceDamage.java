package net.mustyclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.mustyclient.MustyClient;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.Comparator;
import java.util.List;

/**
 * MaceDamage — spoofs fall distance via position packets for insane mace smash damage.
 *
 * Approach: Before each attack, sends two position packets:
 *   1. Player at Y + SPOOF_FALL (server starts tracking fall from that height)
 *   2. Player at real Y (server calculates fallDistance = SPOOF_FALL)
 * Then sends the attack packet — server applies mace damage based on spoofed fall.
 *
 * Two modes:
 *   AIR_ONLY (default): Only activates while airborne — looks more legit
 *   GROUND: Activates even on ground — full hack, no jump needed
 *
 * Toggle: J | Mode: K
 */
public class MaceDamage {

    // Spoofed fall distance in blocks — higher = more damage. Tune this.
    private static final double SPOOF_FALL = 100.0;
    private static final int    ATTACK_RATE  = 10;     // ticks between attacks (~0.5s)
    private static final double RANGE        = 4.5;    // target search range

    private boolean enabled    = false;
    private boolean groundMode = false;

    private KeyMapping toggleKey;
    private KeyMapping modeKey;

    private int ticksSinceLastAttack = 0;
    private int jitter = 0;

    public void init() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.mustyclient.mace_damage_toggle",
                InputConstants.Type.KEYSYM,
                74,  // J key
                MaceMacro.CATEGORY
        ));

        modeKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.mustyclient.mace_damage_mode",
                InputConstants.Type.KEYSYM,
                75,  // K key
                MaceMacro.CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
        MustyClient.LOGGER.info("[MaceDamage] Initialized — toggle: J, mode: K");
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.level == null) return;

        // Toggle
        while (toggleKey.wasPressed()) {
            enabled = !enabled;
            client.player.sendMessage(Component.literal(
                    "\u00a77[\u00a7bMustyClient\u00a77] MaceDamage " +
                    (enabled ? "\u00a7aON" : "\u00a7cOFF")));
        }

        // Mode switch
        while (modeKey.wasPressed()) {
            groundMode = !groundMode;
            client.player.sendMessage(Component.literal(
                    "\u00a77[\u00a7bMustyClient\u00a77] MaceDamage " +
                    (groundMode ? "\u00a7cGROUND" : "\u00a7aAIR_ONLY")));
        }

        if (!enabled) return;

        LocalPlayer player = client.player;

        // In AIR_ONLY mode, skip if on ground
        if (!groundMode && player.isOnGround()) return;

        ticksSinceLastAttack++;
        if (ticksSinceLastAttack < ATTACK_RATE + jitter) return;

        // Find a mace in hotbar
        int maceSlot = findMaceSlot(player);
        if (maceSlot == -1) return;

        // Find target
        Entity target = findTarget(client, player);
        if (target == null) return;

        // Silent slot swap if needed
        if (player.getInventory().getSelectedSlot() != maceSlot) {
            MaceMacro.silentSlotSwap(client, player, maceSlot);
        }

        // === SPOOF fall distance via position packets ===
        // Packet 1: Tell server we're at Y + SPOOF_FALL (onGround=false)
        // Server updates player position and starts tracking fall from that height
        double realX = player.getX();
        double realY = player.getY();
        double realZ = player.getZ();

        client.getConnection().send(new ServerboundMovePlayerPacket.Pos(
                realX, realY + SPOOF_FALL, realZ, false));

        // Packet 2: Tell server we're at real Y (onGround=false)
        // Server calculates fallDistance = SPOOF_FALL between the two packets
        client.getConnection().send(new ServerboundMovePlayerPacket.Pos(
                realX, realY, realZ, false));

        // Attack — server uses its now-spoofed fallDistance for mace damage calc
        MaceMacro.silentAttack(client, player, target);

        // Reset timers with jitter
        ticksSinceLastAttack = 0;
        jitter = (int)(Math.random() * 3) - 1; // -1, 0, or +1 tick
    }

    /**
     * Find nearest living entity within RANGE, excluding self and friendly players.
     */
    private Entity findTarget(MinecraftClient client, LocalPlayer player) {
        AABB box = player.getBoundingBox().inflate(RANGE);
        List<Entity> candidates = client.level.getEntities(
                player,
                box,
                e -> e instanceof LivingEntity
                        && e.isAlive()
                        && !e.isSpectator()
                        && !(e instanceof Player p && isFriendly(player, p))
                        && player.distanceTo(e) <= RANGE
        );
        if (candidates.isEmpty()) return null;
        candidates.sort(Comparator.comparingDouble(player::distanceTo));
        return candidates.get(0);
    }

    /**
     * Scan hotbar for a mace, preferring density slot for high spoofed fall.
     */
    private int findMaceSlot(LocalPlayer player) {
        int preferred = SPOOF_FALL > 9.0 ? MaceMacro.DENSITY_MACE_SLOT : MaceMacro.BREACH_MACE_SLOT;
        if (player.getInventory().getItem(preferred).getItem() == Items.MACE) return preferred;
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i).getItem() == Items.MACE) return i;
        }
        return -1;
    }

    /**
     * Basic team check — prevents friendly fire in team gamemodes.
     */
    private boolean isFriendly(LocalPlayer self, Player other) {
        if (self.getTeam() == null) return false;
        return self.getTeam().equals(other.getTeam());
    }

    public boolean isEnabled()     { return enabled; }
    public boolean isGroundMode()  { return groundMode; }
    public void setGroundMode(boolean v) { groundMode = v; }
}
