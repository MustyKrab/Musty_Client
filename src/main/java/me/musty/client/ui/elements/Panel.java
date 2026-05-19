package me.musty.client.ui.elements;

import java.awt.Color;

public class Panel {
    private float x, y, width, height;
    private String title;
    private boolean draggable;
    private boolean dragging;
    private float dragX, dragY;
    private boolean visible;
    private Color backgroundColor;
    private Color borderColor;
    private Color titleColor;
    private float borderWidth;
    private float cornerRadius;
    private float animationProgress;

    public Panel(float x, float y, float width, float height, String title) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        this.draggable = true;
        this.dragging = false;
        this.visible = true;
        this.backgroundColor = new Color(20, 20, 30, 220);
        this.borderColor = new Color(100, 100, 255, 200);
        this.titleColor = new Color(200, 200, 255);
        this.borderWidth = 1.5f;
        this.cornerRadius = 4.0f;
        this.animationProgress = 0.0f;
    }

    public void render() {
        if (!visible) return;
        if (animationProgress < 1.0f) {
            animationProgress = Math.min(1.0f, animationProgress + 0.05f);
        }
        ShapeRenderer.drawRoundedRect(x, y, width, height, cornerRadius, backgroundColor);
        ShapeRenderer.drawRoundedRectOutline(x, y, width, height, cornerRadius, borderColor, borderWidth);
        float titleBarHeight = 20.0f;
        ShapeRenderer.drawRect(x + 2, y + 2, width - 4, titleBarHeight, new Color(40, 40, 60, 200));
        TextRenderer.drawString(title, x + 8, y + 6, titleColor);
        ShapeRenderer.drawLine(x + 4, y + titleBarHeight + 4, x + width - 4, y + titleBarHeight + 4, borderColor, 1.0f);
    }

    public void handleEvents() {
        if (!visible) return;
        if (draggable && Mouse.isButtonDown(0)) {
            float mouseX = Mouse.getX();
            float mouseY = Mouse.getY();
            if (!dragging) {
                if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 20) {
                    dragging = true;
                    dragX = mouseX - x;
                    dragY = mouseY - y;
                }
            } else {
                x = mouseX - dragX;
                y = mouseY - dragY;
            }
        } else {
            dragging = false;
        }
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getWidth() { return width; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }
}