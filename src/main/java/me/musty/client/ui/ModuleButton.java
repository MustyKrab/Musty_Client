package me.musty.client.ui;

public class ModuleButton {
    private static ModuleButton INSTANCE;
    private boolean enabled;
    private String name;
    private String description;
    private int keyBind;
    private String category;
    private boolean visible;
    private float x;
    private float y;
    private float width;
    private float height;

    public ModuleButton() {
        this.enabled = false;
        this.name = "Module";
        this.description = "Description";
        this.keyBind = 0;
        this.category = "Misc";
        this.visible = true;
        this.x = 0;
        this.y = 0;
        this.width = 100;
        this.height = 20;
    }

    public static ModuleButton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModuleButton();
        }
        return INSTANCE;
    }

    public void render() {
        // Render module button
    }

    public void handleEvents() {
        // Handle events
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}