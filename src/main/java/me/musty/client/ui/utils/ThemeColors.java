package me.musty.client.ui.utils;

import java.awt.Color;

public class ThemeColors {
    private static ThemeColors INSTANCE;
    private Color primaryColor;
    private Color secondaryColor;
    private Color backgroundColor;
    private Color surfaceColor;
    private Color textColor;
    private Color accentColor;
    private Color successColor;
    private Color errorColor;
    private Color warningColor;
    private Color infoColor;
    private Color disabledColor;
    private Color hoverColor;
    private Color activeColor;

    public ThemeColors() {
        this.primaryColor = new Color(100, 100, 255);
        this.secondaryColor = new Color(150, 100, 255);
        this.backgroundColor = new Color(15, 15, 25);
        this.surfaceColor = new Color(25, 25, 40);
        this.textColor = new Color(220, 220, 255);
        this.accentColor = new Color(200, 100, 255);
        this.successColor = new Color(50, 200, 100);
        this.errorColor = new Color(255, 60, 60);
        this.warningColor = new Color(255, 200, 50);
        this.infoColor = new Color(60, 180, 255);
        this.disabledColor = new Color(80, 80, 100);
        this.hoverColor = new Color(130, 130, 255, 100);
        this.activeColor = new Color(100, 100, 255, 150);
    }

    public static ThemeColors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThemeColors();
        }
        return INSTANCE;
    }

    public Color getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(Color primaryColor) { this.primaryColor = primaryColor; }
    public Color getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(Color secondaryColor) { this.secondaryColor = secondaryColor; }
    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }
    public Color getSurfaceColor() { return surfaceColor; }
    public void setSurfaceColor(Color surfaceColor) { this.surfaceColor = surfaceColor; }
    public Color getTextColor() { return textColor; }
    public void setTextColor(Color textColor) { this.textColor = textColor; }
    public Color getAccentColor() { return accentColor; }
    public void setAccentColor(Color accentColor) { this.accentColor = accentColor; }
    public Color getSuccessColor() { return successColor; }
    public void setSuccessColor(Color successColor) { this.successColor = successColor; }
    public Color getErrorColor() { return errorColor; }
    public void setErrorColor(Color errorColor) { this.errorColor = errorColor; }
    public Color getWarningColor() { return warningColor; }
    public void setWarningColor(Color warningColor) { this.warningColor = warningColor; }
    public Color getInfoColor() { return infoColor; }
    public void setInfoColor(Color infoColor) { this.infoColor = infoColor; }
    public Color getDisabledColor() { return disabledColor; }
    public void setDisabledColor(Color disabledColor) { this.disabledColor = disabledColor; }
    public Color getHoverColor() { return hoverColor; }
    public void setHoverColor(Color hoverColor) { this.hoverColor = hoverColor; }
    public Color getActiveColor() { return activeColor; }
    public void setActiveColor(Color activeColor) { this.activeColor = activeColor; }
}