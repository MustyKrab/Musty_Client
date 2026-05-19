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
import me.musty.client.ui.enums.ClickGuiTheme;
import me.musty.client.ui.enums.ClickGuiPosition;
import me.musty.client.ui.enums.ClickGuiAnimation;
import me.musty.client.ui.enums.ClickGuiState;
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

public class ClickGui {
    private static ClickGui INSTANCE;
    private final ClickGuiHandler clickGuiHandler;
    private final Panel mainPanel;
    private final Button enableButton;
    private final Button disableButton;
    private final Slider widthSlider;
    private final Slider heightSlider;
    private final CheckBox showParticlesCheckbox;
    private final Dropdown themeDropdown;
    private final TextBox customThemeTextBox;
    private final Tab modulesTab;
    private final Tab settingsTab;
    private final Tab aboutTab;
    private final ProgressBar loadingBar;
    private final Button applyButton;
    private final Button resetButton;
    private final Button closeButton;

    public ClickGui() {
        this.clickGuiHandler = new ClickGuiHandler();
        this.mainPanel = new Panel(100, 100, 300, 400, "ClickGUI");
        this.enableButton = new Button(120, 120, 100, 30, "Enable");
        this.disableButton = new Button(230, 120, 100, 30, "Disable");
        this.widthSlider = new Slider(120, 160, 210, 20, 0, 1920, 800);
        this.heightSlider = new Slider(120, 190, 210, 20, 0, 1080, 600);
        this.showParticlesCheckbox = new CheckBox(120, 220, 20, 20, "Show Particles");
        this.themeDropdown = new Dropdown(120, 250, 210, 30, "Select Theme", ClickGuiTheme.values());
        this.customThemeTextBox = new TextBox(120, 290, 210, 30, "Custom Theme Name");
        this.modulesTab = new Tab(10, 350, 90, 30, "Modules");
        this.settingsTab = new Tab(110, 350, 90, 30, "Settings");
        this.aboutTab = new Tab(210, 350, 90, 30, "About");
        this.loadingBar = new ProgressBar(120, 400, 210, 20, 0, 100, 0);
        this.applyButton = new Button(120, 430, 100, 30, "Apply");
        this.resetButton = new Button(230, 430, 100, 30, "Reset");
        this.closeButton = new Button(280, 50, 20, 20, "X");
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    public void render() {
        if (clickGuiHandler.isEnabled()) {
            mainPanel.render();
            enableButton.render();
            disableButton.render();
            widthSlider.render();
            heightSlider.render();
            showParticlesCheckbox.render();
            themeDropdown.render();
            customThemeTextBox.render();
            modulesTab.render();
            settingsTab.render();
            aboutTab.render();
            loadingBar.render();
            applyButton.render();
            resetButton.render();
            closeButton.render();
        }
    }

    public void handleEvents() {
        if (clickGuiHandler.isEnabled()) {
            mainPanel.handleEvents();
            enableButton.handleEvents();
            disableButton.handleEvents();
            widthSlider.handleEvents();
            heightSlider.handleEvents();
            showParticlesCheckbox.handleEvents();
            themeDropdown.handleEvents();
            customThemeTextBox.handleEvents();
            modulesTab.handleEvents();
            settingsTab.handleEvents();
            aboutTab.handleEvents();
            loadingBar.handleEvents();
            applyButton.handleEvents();
            resetButton.handleEvents();
            closeButton.handleEvents();
        }
    }

    public ClickGuiHandler getClickGuiHandler() {
        return clickGuiHandler;
    }

    public Panel getMainPanel() {
        return mainPanel;
    }

    public Button getEnableButton() {
        return enableButton;
    }

    public Button getDisableButton() {
        return disableButton;
    }

    public Slider getWidthSlider() {
        return widthSlider;
    }

    public Slider getHeightSlider() {
        return heightSlider;
    }

    public CheckBox getShowParticlesCheckbox() {
        return showParticlesCheckbox;
    }

    public Dropdown getThemeDropdown() {
        return themeDropdown;
    }

    public TextBox getCustomThemeTextBox() {
        return customThemeTextBox;
    }

    public Tab getModulesTab() {
        return modulesTab;
    }

    public Tab getSettingsTab() {
        return settingsTab;
    }

    public Tab getAboutTab() {
        return aboutTab;
    }

    public ProgressBar getLoadingBar() {
        return loadingBar;
    }

    public Button getApplyButton() {
        return applyButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public void toggle() {
        clickGuiHandler.toggle();
    }

    public boolean isEnabled() {
        return clickGuiHandler.isEnabled();
    }

    public void setPosition(ClickGuiPosition position) {
        clickGuiHandler.setPosition(position);
    }

    public void setTheme(ClickGuiTheme theme) {
        clickGuiHandler.setTheme(theme);
    }

    public void setAnimation(ClickGuiAnimation animation) {
        clickGuiHandler.setAnimation(animation);
    }

    public void setState(ClickGuiState state) {
        clickGuiHandler.setState(state);
    }
}
