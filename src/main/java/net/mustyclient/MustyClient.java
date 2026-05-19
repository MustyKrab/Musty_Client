package net.mustyclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.mustyclient.combat.MaceAura;
import net.mustyclient.combat.MaceDamage;
import net.mustyclient.combat.MaceMacro;
import net.mustyclient.render.Cham;
import net.mustyclient.ui.CombatPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MustyClient implements ModInitializer, ClientModInitializer {

    public static final String MOD_ID = "mustyclient";
    public static final Logger LOGGER  = LoggerFactory.getLogger(MOD_ID);

    // Combat modules
    public static final MaceMacro  maceMacro  = new MaceMacro();
    public static final MaceAura   maceAura   = new MaceAura();
    public static final MaceDamage maceDamage  = new MaceDamage();

    // Render modules
    public static final Chams chams = new Chams();

    @Override
    public void onInitialize() {
        LOGGER.info("[MustyClient] Server-side init");
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("[MustyClient] Client init");
        maceMacro.init();
        maceAura.init();
        maceDamage.init();
        chams.init();

        // Register the in-game ClickGui keybind + combat panel
        CombatPanel.init();
    }
}
