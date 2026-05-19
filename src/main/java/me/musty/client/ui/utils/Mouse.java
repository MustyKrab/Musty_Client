package me.musty.client.ui.utils;

public class Mouse {
    private static float x = 0;
    private static float y = 0;
    private static final boolean[] buttons = new boolean[8];

    public static float getX() { return x; }
    public static float getY() { return y; }
    public static void setX(float x) { Mouse.x = x; }
    public static void setY(float y) { Mouse.y = y; }
    public static boolean isButtonDown(int button) { return button >= 0 && button < buttons.length && buttons[button]; }
    public static void setButtonDown(int button, boolean down) { if (button >= 0 && button < buttons.length) buttons[button] = down; }
}
