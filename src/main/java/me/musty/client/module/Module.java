package me.musty.client.module;

public class Module {
    private String name;
    private boolean enabled;
    public Module(String name) { this.name = name; this.enabled = false; }
    public String getName() { return name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void toggle() { this.enabled = !this.enabled; }
}
