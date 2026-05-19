package me.musty.client.ui.elements;

public class TextBox {
    private float x, y, width, height;
    private String placeholder;
    private String text;
    private boolean focused;

    public TextBox(float x, float y, float width, float height, String placeholder) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.placeholder = placeholder; this.text = ""; this.focused = false;
    }

    public void render() {}
    public void handleEvents() {}
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isFocused() { return focused; }
}
