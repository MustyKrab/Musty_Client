package me.musty.client.ui;

public class HudComponent {
    private static HudComponent INSTANCE;
    private boolean enabled;
    private String name;
    private String description;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean visible;

    public HudComponent() {
        this.enabled = false;
        this.name = "HUD Component";
        this.description = "HUD Component Description";
        this.x = 0;
        this.y = 0;
        this.width = 100;
        this.height = 20;
        this.visible = true;
    }

    public static HudComponent getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HudComponent();
        }
        return INSTANCE;
    }

    public void render() {
        // Render HUD component
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}