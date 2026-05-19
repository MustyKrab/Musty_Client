package me.musty.client.ui.animations;

public abstract class Animation {
    protected float progress;
    protected float speed;
    protected boolean finished;

    public Animation(float speed) { this.speed = speed; this.progress = 0f; this.finished = false; }
    public abstract void update();
    public float getProgress() { return progress; }
    public boolean isFinished() { return finished; }
    public void reset() { progress = 0f; finished = false; }
}
