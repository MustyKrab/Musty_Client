package me.musty.client.ui.elements;

import java.awt.Color;

public class Button {
    private float x, y, width, height;
    private String label;
    private boolean hovered;
    private Color bgColor;
    private Color textColor;

    public Button(float x, float y, float width, float height, String label) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.label = label;
        this.bgColor = new Color(60, 60, 120, 200);
        this.textColor = new Color(220, 220, 255);
    }

    public void render() {}
    public void handleEvents() {}
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public float getX() { return x; } public void setX(float x) { this.x = x; }
    public float getY() { return y; } public void setY(float y) { this.y = y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
