package me.musty.client.ui.handlers;

public class ModuleRenderHandler {
    private boolean enabled;
    public ModuleRenderHandler() { this.enabled = true; }
    public void renderModules() {}
    public void handleEvents() {}
    public void toggle() { this.enabled = !this.enabled; }
    public boolean isEnabled() { return enabled; }
}
