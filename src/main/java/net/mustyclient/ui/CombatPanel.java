package net.mustyclient.ui;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.mustyclient.MustyClient;
import net.mustyclient.combat.MaceMacro;
import com.mojang.blaze3d.platform.InputConstants;

import java.awt.Color;

/**
 * CombatPanel — keybind-driven in-game status/toggle panel.
 *
 * Backtick (`) = print full module status to chat
 * R            = MaceAura toggle  (registered in MaceAura itself)
 * MB5          = MaceMacro slam   (registered in MaceMacro itself)
 * V            = Chams toggle
 * B            = Chams color cycle
 * N            = Chams style (filled / wireframe)
 */
public class CombatPanel {

    private static KeyMapping openKey;
    private static KeyMapping chamsToggleKey;
    private static KeyMapping chamsColorKey;
    private static KeyMapping chamsStyleKey;

    private static final Color[] COLOR_PRESETS = {
        new Color(255, 50,  50,  200),
        new Color(50,  255, 50,  200),
        new Color(50,  150, 255, 200),
        new Color(255, 255, 50,  200),
        new Color(200, 50,  255, 200),
        new Color(255, 165, 0,   200),
        new Color(255, 255, 255, 200),
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
        // Backtick = GLFW key 96
        openKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.mustyclient.combat_panel",
                InputConstants.Type.KEYSYM,
                96,
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
        MustyClient.LOGGER.info("[CombatPanel] Registered — `=status, V=chams, B=color, N=style");
    }

    private static void onTick(Minecraft mc) {
        if (mc.player == null) return;

        while (openKey.consumeClick()) {
            printStatus(mc);
        }

        while (chamsToggleKey.consumeClick()) {
            boolean now = !MustyClient.chams.isEnabled();
            MustyClient.chams.setEnabled(now);
            msg(mc, "\u00a77[\u00a7bChams\u00a77] " + (now ? "\u00a7aON" : "\u00a7cOFF"));
        }

        while (chamsColorKey.consumeClick()) {
            colorIndex = (colorIndex + 1) % COLOR_PRESETS.length;
            MustyClient.chams.setVisibleColor(COLOR_PRESETS[colorIndex]);
            MustyClient.chams.setOccludedColor(OCCLUDED_PRESETS[colorIndex]);
            msg(mc, "\u00a77[\u00a7bChams\u00a77] Color: \u00a7e" + COLOR_NAMES[colorIndex]);
        }

        while (chamsStyleKey.consumeClick()) {
            boolean filled = !MustyClient.chams.isFilledChams();
            MustyClient.chams.setFilledChams(filled);
            msg(mc, "\u00a77[\u00a7bChams\u00a77] Style: \u00a7e" + (filled ? "Filled" : "Wireframe"));
        }
    }

    private static void printStatus(Minecraft mc) {
        msg(mc, "\u00a77=== \u00a7bMustyClient\u00a77 ===");
        msg(mc, "\u00a77MaceAura  [R]:   " + state(MustyClient.maceAura.isEnabled()));
        msg(mc, "\u00a77MaceMacro [MB5]: \u00a7eActive");
        msg(mc, "\u00a77Chams     [V]:   " + state(MustyClient.chams.isEnabled())
                + "  Color[B]: \u00a7e" + COLOR_NAMES[colorIndex]
                + "  Style[N]: \u00a7e" + (MustyClient.chams.isFilledChams() ? "Filled" : "Wireframe"));
    }

    private static String state(boolean on) {
        return on ? "\u00a7aON" : "\u00a7cOFF";
    }

    private static void msg(Minecraft mc, String text) {
        mc.player.sendSystemMessage(Component.literal(text));
    }
}
