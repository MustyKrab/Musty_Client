package net.mustyclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
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
 * MaceAura — auto-attack nearest living entity with mace while airborne.
 *
 * Silent AC bypass:
 *   - All attacks via ServerboundInteractPacket (same as MaceMacro.silentAttack).
 *   - Slot swap via ServerboundSetCarriedItemPacket (no visual flicker).
 *   - Attack rate capped at 1 per SERVER_ATTACK_RATE_TICKS to avoid suspicious packet bursts.
 *   - Only activates while airborne (onGround == false) to match legit mace usage patterns.
 *   - Randomised jitter on attack timing (+/- 1 tick) to avoid perfectly regular packet intervals.
 */
public class MaceAura {

    // Configurable — change these to tune behaviour
    private static final double RANGE         = 4.5;  // blocks
    private static final int    ATTACK_RATE   = 10;   // ticks between attacks (~0.5s)
    private static final double MIN_FALL_DIST = 1.5;  // must be falling at least this far

    private boolean enabled = false;
    private KeyMapping toggleKey;

    private int ticksSinceLastAttack = 0;
    // Jitter offset refreshed each attack cycle
    private int jitter = 0;

    public void init() {
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.mace_aura_toggle",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_R,  // default: R
                MaceMacro.CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
        MustyClient.LOGGER.info("[MaceAura] Initialized — toggle: R");
    }

    private void onTick(Minecraft client) {
        if (client.player == null || client.level == null) return;

        // Toggle on keypress
        while (toggleKey.consumeClick()) {
            enabled = !enabled;
            client.player.sendSystemMessage(Component.literal(
                    "\u00a77[\u00a7bMustyClient\u00a77] MaceAura " +
                    (enabled ? "\u00a7aON" : "\u00a7cOFF")));
        }

        if (!enabled) return;

        LocalPlayer player = client.player;

        // Only run while airborne and actually falling
        if (player.onGround() || player.fallDistance < MIN_FALL_DIST) return;

        ticksSinceLastAttack++;
        if (ticksSinceLastAttack < ATTACK_RATE + jitter) return;

        // Find nearest valid target in range
        Entity target = findTarget(client, player);
        if (target == null) return;

        // Pick the right mace slot
        int maceSlot = findMaceSlot(player);
        if (maceSlot == -1) return; // no mace in hotbar

        // Silent slot swap if needed
        if (player.getInventory().getSelectedSlot() != maceSlot) {
            MaceMacro.silentSlotSwap(client, player, maceSlot);
        }

        // Silent attack
        MaceMacro.silentAttack(client, player, target);

        // Reset timer with jitter
        ticksSinceLastAttack = 0;
        jitter = (int)(Math.random() * 3) - 1; // -1, 0, or +1 tick
    }

    /**
     * Find nearest living entity within RANGE, excluding self and other players
     * on the same team (basic friendly-fire guard).
     */
    private Entity findTarget(Minecraft client, LocalPlayer player) {
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
     * Scan hotbar slots 0-8 for a mace, preferring DENSITY_MACE_SLOT then BREACH_MACE_SLOT.
     */
    private int findMaceSlot(LocalPlayer player) {
        double fallDist = player.fallDistance;
        int preferred = fallDist > 9.0 ? MaceMacro.DENSITY_MACE_SLOT : MaceMacro.BREACH_MACE_SLOT;
        if (player.getInventory().getItem(preferred).getItem() == Items.MACE) return preferred;
        // Fallback: scan hotbar
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i).getItem() == Items.MACE) return i;
        }
        return -1;
    }

    /**
     * Basic team check — returns true if target is on the same scoreboard team.
     * Prevents accidental friendly fire in team gamemodes.
     */
    private boolean isFriendly(LocalPlayer self, Player other) {
        if (self.getTeam() == null) return false;
        return self.getTeam().equals(other.getTeam());
    }

    public boolean isEnabled() { return enabled; }
}
