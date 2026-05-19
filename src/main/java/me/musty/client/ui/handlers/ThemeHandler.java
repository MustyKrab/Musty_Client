package me.musty.client.ui.handlers;

import me.musty.client.ui.enums.ClickGuiTheme;

public class ThemeHandler {
    private boolean enabled;
    private ClickGuiTheme theme;
    public ThemeHandler() { this.enabled = true; this.theme = ClickGuiTheme.DARK; }
    public void handleEvents() {}
    public void toggle() { this.enabled = !this.enabled; }
    public boolean isEnabled() { return enabled; }
    public ClickGuiTheme getTheme() { return theme; }
    public void setTheme(ClickGuiTheme theme) { this.theme = theme; }
}
