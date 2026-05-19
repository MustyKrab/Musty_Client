package me.musty.client.ui.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class TextRenderer {
    private static TextRenderer INSTANCE;
    private Graphics2D graphics;
    private Font currentFont;
    private Color currentColor;

    public TextRenderer() {
        this.currentFont = new Font("Segoe UI", Font.PLAIN, 14);
        this.currentColor = Color.WHITE;
    }

    public static TextRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TextRenderer();
        }
        return INSTANCE;
    }

    public static void drawString(String text, float x, float y, Color color) {
        // Draw text at specified position
    }

    public static void drawStringWithShadow(String text, float x, float y, Color color) {
        // Draw text with drop shadow
    }

    public static void drawCenteredString(String text, float x, float y, float width, Color color) {
        // Draw text centered horizontally within bounds
    }

    public static int getStringWidth(String text) {
        // Get string width in pixels
        return text.length() * 7;
    }

    public static int getStringHeight(String text) {
        // Get string height in pixels
        return 14;
    }

    public static void drawScaledString(String text, float x, float y, float scale, Color color) {
        // Draw scaled text
    }

    public static void drawWrappedString(String text, float x, float y, float maxWidth, Color color) {
        // Draw wrapped text within maxWidth
    }

    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    public void setFont(Font font) {
        this.currentFont = font;
    }

    public void setColor(Color color) {
        this.currentColor = color;
    }

    public Font getFont() {
        return currentFont;
    }

    public Color getColor() {
        return currentColor;
    }
}