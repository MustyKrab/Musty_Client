package net.mustyclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.mustyclient.MustyClient;
import com.mojang.blaze3d.platform.InputConstants;

/**
 * MaceMacro — MB5 slam with silent AC bypass.
 *
 * Silent approach:
 *   - Slot swap via ServerboundSetCarriedItemPacket (no visual flicker).
 *   - Attack via raw ServerboundInteractPacket (bypasses client-side cooldown check).
 *   - Swing via ServerboundSwingPacket (server sees the animation, no client swing).
 */
public class MaceMacro {

    public static final int DENSITY_MACE_SLOT = 8; // slot 9
    public static final int BREACH_MACE_SLOT  = 5; // slot 6

    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
            Identifier.fromNamespaceAndPath("mustyclient", "category.mustyclient.combat")
    );

    private KeyMapping mb5Key;

    private static final int SWAP_DELAY_TICKS = 1;
    private int pendingAttackTicks = -1;
    private Entity pendingTarget = null;
    private int pendingSlot = -1;

    public void init() {
        mb5Key = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.mace_slam",
                InputConstants.Type.MOUSE,
                5,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) return;

            if (pendingAttackTicks > 0) {
                pendingAttackTicks--;
            } else if (pendingAttackTicks == 0) {
                pendingAttackTicks = -1;
                if (pendingTarget != null) {
                    silentAttack(client, client.player, pendingTarget);
                    pendingTarget = null;
                    pendingSlot = -1;
                }
            }

            while (mb5Key.consumeClick()) {
                doSlam(client);
            }
        });

        MustyClient.LOGGER.info("[MaceMacro] Slam bound to Mouse Button 5");
    }

    private void doSlam(Minecraft client) {
        LocalPlayer player = client.player;
        double fallDist = player.fallDistance;

        int targetSlot = fallDist > 9.0 ? DENSITY_MACE_SLOT : BREACH_MACE_SLOT;

        Entity target = null;
        if (client.hitResult != null && client.hitResult.getType() == HitResult.Type.ENTITY) {
            target = ((EntityHitResult) client.hitResult).getEntity();
        }
        if (target == null) {
            silentSwing(client, player);
            return;
        }

        ItemStack stack = player.getInventory().getItem(targetSlot);
        if (stack.isEmpty()) {
            player.sendSystemMessage(Component.literal(
                    "\u00a77[\u00a7bMustyClient\u00a77] \u00a7cNo mace in slot " + (targetSlot + 1)));
            return;
        }
        if (stack.getItem() != Items.MACE) {
            player.sendSystemMessage(Component.literal(
                    "\u00a77[\u00a7bMustyClient\u00a77] \u00a7cSlot " + (targetSlot + 1) + " is not a mace!"));
            return;
        }

        if (player.getInventory().getSelectedSlot() != targetSlot) {
            silentSlotSwap(client, player, targetSlot);
            pendingTarget = target;
            pendingSlot = targetSlot;
            pendingAttackTicks = SWAP_DELAY_TICKS;
        } else {
            silentAttack(client, player, target);
        }
    }

    public static void silentSlotSwap(Minecraft client, LocalPlayer player, int slot) {
        client.getConnection().send(
                new net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket(slot)
        );
        player.getInventory().setSelectedSlot(slot);
    }

    /**
     * MC 26.1.2 — unobfuscated Mojang names, no Yarn.
     * ServerboundInteractPacket has no createAttackPacket factory.
     * Constructor: ServerboundInteractPacket(int entityId, boolean sneaking, Action action)
     * Action is a sealed interface; ATTACK impl is the inner record ServerboundInteractPacket.Attack (no fields).
     */
    public static void silentAttack(Minecraft client, LocalPlayer player, Entity target) {
        client.getConnection().send(
                new ServerboundInteractPacket(target.getId(), player.isShiftKeyDown(), ServerboundInteractPacket.Attack.INSTANCE)
        );
        silentSwing(client, player);
    }

    public static void silentSwing(Minecraft client, LocalPlayer player) {
        client.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
    }
}
