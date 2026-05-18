package net.mustyclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.mustyclient.combat.MaceAura;
import net.mustyclient.combat.MaceMacro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MustyClient implements ModInitializer, ClientModInitializer {

    public static final String MOD_ID = "mustyclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Combat modules
    private static final MaceMacro maceMacro = new MaceMacro();
    private static final MaceAura  maceAura  = new MaceAura();

    @Override
    public void onInitialize() {
        LOGGER.info("[MustyClient] Server-side init");
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("[MustyClient] Client init");
        maceMacro.init();
        maceAura.init();
    }
}
