package me.musty.client.ui.events;

public class ModuleRenderEvent {
    private boolean cancelled;
    public ModuleRenderEvent() { this.cancelled = false; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
