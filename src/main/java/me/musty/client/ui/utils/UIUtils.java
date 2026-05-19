package me.musty.client.ui.utils;

public class UIUtils {
    public static boolean isHovered(float mx, float my, float x, float y, float w, float h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
