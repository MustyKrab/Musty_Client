package me.musty.client.ui.elements;

public class CheckBox {
    private float x, y, width, height;
    private String label;
    private boolean checked;

    public CheckBox(float x, float y, float width, float height, String label) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.label = label; this.checked = false;
    }

    public void render() {}
    public void handleEvents() {}
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
    public String getLabel() { return label; }
}
