package me.musty.client.ui.events;

public class HudEvent {
    private boolean cancelled;
    public HudEvent() { this.cancelled = false; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
