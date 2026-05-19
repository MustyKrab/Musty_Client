package me.musty.client.ui.elements;

public class Slider {
    private float x, y, width, height;
    private float min, max, value;

    public Slider(float x, float y, float width, float height, float min, float max, float value) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.min = min; this.max = max; this.value = value;
    }

    public void render() {}
    public void handleEvents() {}
    public float getValue() { return value; }
    public void setValue(float value) { this.value = Math.max(min, Math.min(max, value)); }
    public float getMin() { return min; }
    public float getMax() { return max; }
}
