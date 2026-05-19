package me.musty.client.ui.elements;

public class Dropdown {
    private float x, y, width, height;
    private String label;
    private Object[] items;
    private int selectedIndex;
    private boolean open;

    public Dropdown(float x, float y, float width, float height, String label, Object[] items) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.label = label; this.items = items; this.selectedIndex = 0; this.open = false;
    }

    public void render() {}
    public void handleEvents() {}
    public Object getSelected() { return items != null && items.length > 0 ? items[selectedIndex] : null; }
    public int getSelectedIndex() { return selectedIndex; }
    public void setSelectedIndex(int index) { this.selectedIndex = index; }
    public boolean isOpen() { return open; }
}
