package me.musty.client.ui.renderers;

public class ComponentRenderer {
    private static ComponentRenderer INSTANCE;
    public static ComponentRenderer getInstance() {
        if (INSTANCE == null) INSTANCE = new ComponentRenderer();
        return INSTANCE;
    }
    public void render() {}
}
