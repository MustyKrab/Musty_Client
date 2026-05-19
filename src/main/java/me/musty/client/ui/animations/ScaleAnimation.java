package me.musty.client.ui.animations;

public class ScaleAnimation extends Animation {
    public ScaleAnimation(float speed) { super(speed); }
    @Override
    public void update() {
        if (progress < 1f) { progress = Math.min(1f, progress + speed); }
        else { finished = true; }
    }
}
