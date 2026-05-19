package me.musty.client.ui.renderers;

public class TextureRenderer {
    private static TextureRenderer INSTANCE;
    public static TextureRenderer getInstance() {
        if (INSTANCE == null) INSTANCE = new TextureRenderer();
        return INSTANCE;
    }
    public void render() {}
}
