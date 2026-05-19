package me.musty.client.ui.handlers;

public class HudHandler {
    private boolean enabled;
    public HudHandler() { this.enabled = true; }
    public void render() {}
    public void handleEvents() {}
    public void toggle() { this.enabled = !this.enabled; }
    public boolean isEnabled() { return enabled; }
}
