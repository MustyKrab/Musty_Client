package me.musty.client.ui.renderers.particle.effects;

import me.musty.client.ui.renderers.particle.Particle;
import me.musty.client.ui.renderers.particle.ParticleType;
import java.awt.Color;

public class GlowParticle extends Particle {
    private float radius;
    public GlowParticle(float x, float y, float vx, float vy, float life, Color color, float radius) {
        super(x, y, vx, vy, life, color, ParticleType.GLOW);
        this.radius = radius;
    }
    public float getRadius() { return radius; }
}
