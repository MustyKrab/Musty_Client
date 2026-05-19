package me.musty.client.ui.renderers.particle;

import java.awt.Color;

public class Particle {
    private float x, y;
    private float vx, vy;
    private float life;
    private float maxLife;
    private Color color;
    private ParticleType type;

    public Particle(float x, float y, float vx, float vy, float life, Color color, ParticleType type) {
        this.x = x; this.y = y; this.vx = vx; this.vy = vy;
        this.life = life; this.maxLife = life; this.color = color; this.type = type;
    }

    public void update() { x += vx; y += vy; life -= 0.016f; }
    public boolean isDead() { return life <= 0; }
    public float getX() { return x; } public float getY() { return y; }
    public float getLife() { return life; } public float getMaxLife() { return maxLife; }
    public Color getColor() { return color; }
    public ParticleType getType() { return type; }
}
