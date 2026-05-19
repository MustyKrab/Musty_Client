package me.musty.client.ui.events;

public class ClickGuiEvent {
    private boolean cancelled;
    public ClickGuiEvent() { this.cancelled = false; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
