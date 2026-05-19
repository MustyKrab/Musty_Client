package net.mustyclient.ui;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.mustyclient.MustyClient;
import com.mojang.blaze3d.platform.InputConstants;

import java.awt.Color;

/**
 * CombatPanel — lightweight in-game toggle/config panel for:
 *   - MaceAura  (toggle: R)
 *   - MaceMacro (toggle: MB5 — already wired in MaceMacro itself)
 *   - Chams ESP (toggle: V, color cycling: B)
 *
 * Opens/closes with INSERT. Prints status to action bar so you always
 * know what's on without needing a full ClickGui screen.
 *
 * No GL rendering here — just keybind-driven state management.
 * Hook a proper screen renderer into WorldRenderEvents if you want
 * an on-screen panel later.
 */
public class CombatPanel {

    private static KeyMapping openKey;
    private static KeyMapping chamsToggleKey;
    private static KeyMapping chamsColorKey;
    private static KeyMapping chamsStyleKey;

    // Chams color presets cycled with B
    private static final Color[] COLOR_PRESETS = {
        new Color(255, 50,  50,  180),  // red
        new Color(50,  255, 50,  180),  // green
        new Color(50,  150, 255, 180),  // blue
        new Color(255, 255, 50,  180),  // yellow
        new Color(200, 50,  255, 180),  // purple
        new Color(255, 165, 0,   180),  // orange
        new Color(255, 255, 255, 180),  // white
    };
    private static final Color[] OCCLUDED_PRESETS = {
        new Color(255, 50,  50,  55),
        new Color(50,  255, 50,  55),
        new Color(50,  150, 255, 55),
        new Color(255, 255, 50,  55),
        new Color(200, 50,  255, 55),
        new Color(255, 165, 0,   55),
        new Color(255, 255, 255, 55),
    };
    private static final String[] COLOR_NAMES = {
        "Red", "Green", "Blue", "Yellow", "Purple", "Orange", "White"
    };
    private static int colorIndex = 0;

    public static void init() {
        openKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.combat_panel",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_INSERT,
                MaceMacro.CATEGORY
        ));

        chamsToggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.chams_toggle",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_V,
                MaceMacro.CATEGORY
        ));

        chamsColorKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.chams_color",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_B,
                MaceMacro.CATEGORY
        ));

        chamsStyleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.chams_style",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_N,
                MaceMacro.CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(CombatPanel::onTick);
        MustyClient.LOGGER.info("[CombatPanel] Registered — INSERT=panel, V=chams, B=color, N=style");
    }

    private static void onTick(Minecraft mc) {
        if (mc.player == null) return;

        // INSERT — print full status to chat
        while (openKey.consumeClick()) {
            printStatus(mc);
        }

        // V — toggle Chams
        while (chamsToggleKey.consumeClick()) {
            boolean now = !MustyClient.chams.isEnabled();
            MustyClient.chams.setEnabled(now);
            msg(mc, "\u00a77[\u00a7bChams\u00a77] " + (now ? "\u00a7aON" : "\u00a7cOFF"));
        }

        // B — cycle Chams color
        while (chamsColorKey.consumeClick()) {
            colorIndex = (colorIndex + 1) % COLOR_PRESETS.length;
            MustyClient.chams.setVisibleColor(COLOR_PRESETS[colorIndex]);
            MustyClient.chams.setOccludedColor(OCCLUDED_PRESETS[colorIndex]);
            msg(mc, "\u00a77[\u00a7bChams\u00a77] Color: \u00a7e" + COLOR_NAMES[colorIndex]);
        }

        // N — toggle filled vs wireframe
        while (chamsStyleKey.consumeClick()) {
            boolean filled = !MustyClient.chams.isFilledChams();
            MustyClient.chams.setFilledChams(filled);
            msg(mc, "\u00a77[\u00a7bChams\u00a77] Style: \u00a7e" + (filled ? "Filled" : "Wireframe"));
        }
    }

    private static void printStatus(Minecraft mc) {
        msg(mc, "\u00a77=== \u00a7bMustyClient\u00a77 ===" );
        msg(mc, "\u00a77MaceAura  [R]:  " + state(MustyClient.maceAura.isEnabled()));
        msg(mc, "\u00a77MaceMacro [MB5]: \u00a7eActive (always on)");
        msg(mc, "\u00a77Chams     [V]:  " + state(MustyClient.chams.isEnabled())
                + "  Color [B]: \u00a7e" + COLOR_NAMES[colorIndex]
                + "  Style [N]: \u00a7e" + (MustyClient.chams.isFilledChams() ? "Filled" : "Wireframe"));
    }

    private static String state(boolean on) {
        return on ? "\u00a7aON" : "\u00a7cOFF";
    }

    private static void msg(Minecraft mc, String text) {
        if (mc.player != null)
            mc.player.sendSystemMessage(Component.literal(text));
    }
}
