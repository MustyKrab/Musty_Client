package me.musty.client.ui.elements;

public class Tab {
    private float x, y, width, height;
    private String label;
    private boolean selected;

    public Tab(float x, float y, float width, float height, String label) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.label = label; this.selected = false;
    }

    public void render() {}
    public void handleEvents() {}
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    public String getLabel() { return label; }
}
