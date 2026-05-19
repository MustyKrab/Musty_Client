package me.musty.client.ui.renderers.particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleRenderer {
    private final List<Particle> particles = new ArrayList<>();

    public void renderParticles() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (p.isDead()) it.remove();
        }
    }

    public void addParticle(Particle particle) { particles.add(particle); }
    public void clearParticles() { particles.clear(); }
    public int getParticleCount() { return particles.size(); }
}
