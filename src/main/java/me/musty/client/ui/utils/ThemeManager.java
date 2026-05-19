package me.musty.client.ui.utils;

import me.musty.client.ui.enum.ClickGuiTheme;
import java.awt.Color;

public class ThemeManager {
    private static ThemeManager INSTANCE;
    private ClickGuiTheme currentTheme;
    private ThemeColors currentColors;
    private boolean enableAnimations;
    private float animationSpeed;
    private boolean enableParticles;
    private boolean enableBlur;
    private boolean enableShadows;
    private boolean enableGradients;

    public ThemeManager() {
        this.currentTheme = ClickGuiTheme.DARK;
        this.currentColors = new ThemeColors();
        this.enableAnimations = true;
        this.animationSpeed = 1.0f;
        this.enableParticles = true;
        this.enableBlur = true;
        this.enableShadows = true;
        this.enableGradients = true;
    }

    public static ThemeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThemeManager();
        }
        return INSTANCE;
    }

    public void setTheme(ClickGuiTheme theme) {
        this.currentTheme = theme;
        applyTheme();
    }

    private void applyTheme() {
        switch (currentTheme) {
            case DARK:
                applyDarkTheme();
                break;
            case LIGHT:
                applyLightTheme();
                break;
            case BLUE:
                applyBlueTheme();
                break;
            case PURPLE:
                applyPurpleTheme();
                break;
            case GREEN:
                applyGreenTheme();
                break;
            case ORANGE:
                applyOrangeTheme();
                break;
            case RED:
                applyRedTheme();
                break;
            case CUSTOM:
                applyCustomTheme();
                break;
        }
    }

    private void applyDarkTheme() {
        currentColors.setPrimaryColor(new Color(100, 100, 255));
        currentColors.setBackgroundColor(new Color(15, 15, 25));
        currentColors.setSurfaceColor(new Color(25, 25, 40));
        currentColors.setTextColor(new Color(220, 220, 255));
        currentColors.setAccentColor(new Color(200, 100, 255));
    }

    private void applyLightTheme() {
        currentColors.setPrimaryColor(new Color(80, 80, 200));
        currentColors.setBackgroundColor(new Color(240, 240, 255));
        currentColors.setSurfaceColor(new Color(220, 220, 240));
        currentColors.setTextColor(new Color(30, 30, 50));
        currentColors.setAccentColor(new Color(180, 80, 240));
    }

    private void applyBlueTheme() {
        currentColors.setPrimaryColor(new Color(50, 150, 255));
        currentColors.setBackgroundColor(new Color(10, 20, 40));
        currentColors.setSurfaceColor(new Color(20, 40, 60));
        currentColors.setTextColor(new Color(200, 220, 255));
        currentColors.setAccentColor(new Color(50, 200, 255));
    }

    private void applyPurpleTheme() {
        currentColors.setPrimaryColor(new Color(150, 50, 255));
        currentColors.setBackgroundColor(new Color(25, 15, 40));
        currentColors.setSurfaceColor(new Color(40, 25, 60));
        currentColors.setTextColor(new Color(230, 210, 255));
        currentColors.setAccentColor(new Color(255, 50, 200));
    }

    private void applyGreenTheme() {
        currentColors.setPrimaryColor(new Color(50, 200, 100));
        currentColors.setBackgroundColor(new Color(10, 30, 20));
        currentColors.setSurfaceColor(new Color(20, 45, 30));
        currentColors.setTextColor(new Color(200, 255, 220));
        currentColors.setAccentColor(new Color(50, 255, 150));
    }

    private void applyOrangeTheme() {
        currentColors.setPrimaryColor(new Color(255, 140, 50));
        currentColors.setBackgroundColor(new Color(35, 25, 15));
        currentColors.setSurfaceColor(new Color(50, 35, 20));
        currentColors.setTextColor(new Color(255, 230, 200));
        currentColors.setAccentColor(new Color(255, 100, 50));
    }

    private void applyRedTheme() {
        currentColors.setPrimaryColor(new Color(220, 50, 50));
        currentColors.setBackgroundColor(new Color(35, 15, 15));
        currentColors.setSurfaceColor(new Color(50, 20, 20));
        currentColors.setTextColor(new Color(255, 200, 200));
        currentColors.setAccentColor(new Color(255, 50, 100));
    }

    private void applyCustomTheme() {
        // Custom theme colors - user defined
    }

    public ClickGuiTheme getCurrentTheme() { return currentTheme; }
    public ThemeColors getColors() { return currentColors; }
    public boolean isEnableAnimations() { return enableAnimations; }
    public void setEnableAnimations(boolean enableAnimations) { this.enableAnimations = enableAnimations; }
    public float getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(float animationSpeed) { this.animationSpeed = animationSpeed; }
    public boolean isEnableParticles() { return enableParticles; }
    public void setEnableParticles(boolean enableParticles) { this.enableParticles = enableParticles; }
    public boolean isEnableBlur() { return enableBlur; }
    public void setEnableBlur(boolean enableBlur) { this.enableBlur = enableBlur; }
    public boolean isEnableShadows() { return enableShadows; }
    public void setEnableShadows(boolean enableShadows) { this.enableShadows = enableShadows; }
    public boolean isEnableGradients() { return enableGradients; }
    public void setEnableGradients(boolean enableGradients) { this.enableGradients = enableGradients; }
}