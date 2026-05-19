package me.musty.client.ui.handlers;

import me.musty.client.ui.enums.ClickGuiAnimation;
import me.musty.client.ui.enums.ClickGuiPosition;
import me.musty.client.ui.enums.ClickGuiState;
import me.musty.client.ui.enums.ClickGuiTheme;

public class ClickGuiHandler {
    private boolean enabled;
    private ClickGuiPosition position;
    private ClickGuiTheme theme;
    private ClickGuiAnimation animation;
    private ClickGuiState state;

    public ClickGuiHandler() {
        this.enabled = false;
        this.position = ClickGuiPosition.CENTER;
        this.theme = ClickGuiTheme.DARK;
        this.animation = ClickGuiAnimation.FADE;
        this.state = ClickGuiState.CLOSED;
    }

    public void render() {}
    public void handleEvents() {}
    public void toggle() { this.enabled = !this.enabled; this.state = enabled ? ClickGuiState.OPEN : ClickGuiState.CLOSED; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public ClickGuiPosition getPosition() { return position; }
    public void setPosition(ClickGuiPosition position) { this.position = position; }
    public ClickGuiTheme getTheme() { return theme; }
    public void setTheme(ClickGuiTheme theme) { this.theme = theme; }
    public ClickGuiAnimation getAnimation() { return animation; }
    public void setAnimation(ClickGuiAnimation animation) { this.animation = animation; }
    public ClickGuiState getState() { return state; }
    public void setState(ClickGuiState state) { this.state = state; }
}
