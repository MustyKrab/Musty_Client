package me.musty.client.ui;

import me.musty.client.MistyClient;
import me.musty.client.module.Module;
import me.musty.client.setting.Setting;
import me.musty.client.ui.elements.Panel;
import me.musty.client.ui.elements.Button;
import me.musty.client.ui.elements.Slider;
import me.musty.client.ui.elements.CheckBox;
import me.musty.client.ui.elements.Dropdown;
import me.musty.client.ui.elements.TextBox;
import me.musty.client.ui.elements.Tab;
import me.musty.client.ui.elements.ProgressBar;
import me.musty.client.ui.animations.Animation;
import me.musty.client.ui.animations.SlideAnimation;
import me.musty.client.ui.animations.FadeAnimation;
import me.musty.client.ui.animations.ScaleAnimation;
import me.musty.client.ui.renderers.ComponentRenderer;
import me.musty.client.ui.renderers.ShapeRenderer;
import me.musty.client.ui.renderers.TextRenderer;
import me.musty.client.ui.renderers.TextureRenderer;
import me.musty.client.ui.utils.ThemeColors;
import me.musty.client.ui.utils.ThemeManager;
import me.musty.client.ui.utils.UIUtils;
import me.musty.client.ui.enum.ClickGuiTheme;
import me.musty.client.ui.enum.ClickGuiPosition;
import me.musty.client.ui.enum.ClickGuiAnimation;
import me.musty.client.ui.enum.ClickGuiState;
import me.musty.client.ui.events.ClickGuiEvent;
import me.musty.client.ui.events.HudEvent;
import me.musty.client.ui.events.ModuleRenderEvent;
import me.musty.client.ui.handlers.ClickGuiHandler;
import me.musty.client.ui.handlers.HudHandler;
import me.musty.client.ui.handlers.ModuleRenderHandler;
import me.musty.client.ui.handlers.ThemeHandler;
import me.musty.client.ui.renderers.particle.ParticleRenderer;
import me.musty.client.ui.renderers.particle.Particle;
import me.musty.client.ui.renderers.particle.ParticleType;
import me.musty.client.ui.renderers.particle.effects.GlowParticle;
import me.musty.client.ui.renderers.particle.effects.SparkleParticle;

public class UiRenderer {
    private static UiRenderer INSTANCE;
    private final ClickGuiHandler clickGuiHandler;
    private final HudHandler hudHandler;
    private final ModuleRenderHandler moduleRenderHandler;
    private final ThemeHandler themeHandler;
    private final ParticleRenderer particleRenderer;

    public UiRenderer() {
        this.clickGuiHandler = new ClickGuiHandler();
        this.hudHandler = new HudHandler();
        this.moduleRenderHandler = new ModuleRenderHandler();
        this.themeHandler = new ThemeHandler();
        this.particleRenderer = new ParticleRenderer();
    }

    public static UiRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UiRenderer();
        }
        return INSTANCE;
    }

    public void render() {
        // Render particles
        particleRenderer.renderParticles();

        // Render HUD
        hudHandler.render();

        // Render ClickGUI if enabled
        if (clickGuiHandler.isEnabled()) {
            clickGuiHandler.render();
        }

        // Render modules
        moduleRenderHandler.renderModules();
    }

    public void handleEvents() {
        // Handle click gui events
        clickGuiHandler.handleEvents();

        // Handle hud events
        hudHandler.handleEvents();

        // Handle module render events
        moduleRenderHandler.handleEvents();

        // Handle theme events
        themeHandler.handleEvents();
    }

    public ClickGuiHandler getClickGuiHandler() {
        return clickGuiHandler;
    }

    public HudHandler getHudHandler() {
        return hudHandler;
    }

    public ModuleRenderHandler getModuleRenderHandler() {
        return moduleRenderHandler;
    }

    public ThemeHandler getThemeHandler() {
        return themeHandler;
    }

    public ParticleRenderer getParticleRenderer() {
        return particleRenderer;
    }

    public void toggleClickGui() {
        clickGuiHandler.toggle();
    }

    public void toggleHud() {
        hudHandler.toggle();
    }

    public void toggleModuleRender() {
        moduleRenderHandler.toggle();
    }

    public void toggleTheme() {
        themeHandler.toggle();
    }

    public void setClickGuiPosition(ClickGuiPosition position) {
        clickGuiHandler.setPosition(position);
    }

    public void setClickGuiTheme(ClickGuiTheme theme) {
        themeHandler.setTheme(theme);
    }

    public void setClickGuiAnimation(ClickGuiAnimation animation) {
        clickGuiHandler.setAnimation(animation);
    }

    public void setClickGuiState(ClickGuiState state) {
        clickGuiHandler.setState(state);
    }

    public void addParticle(Particle particle) {
        particleRenderer.addParticle(particle);
    }

    public void clearParticles() {
        particleRenderer.clearParticles();
    }
}