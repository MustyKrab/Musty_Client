package net.mustyclient.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mustyclient.MustyClient;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

/**
 * Chams ESP — two-pass AABB box renderer.
 *
 * Uses raw GL11 (stable, no version churn) for depth/blend/draw.
 * Pass 1: GL_ALWAYS  → occluded color through walls
 * Pass 2: GL_LEQUAL  → visible color on top
 */
public class Chams {

    private Color   visibleColor  = new Color(255, 50,  50,  200);
    private Color   occludedColor = new Color(255, 50,  50,  55);
    private boolean teamCheck     = true;
    private boolean selfChams     = false;
    private boolean enabled       = false;
    private boolean filledChams   = true;
    private float   lineWidth     = 1.5f;

    public void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(this::onRender);
        MustyClient.LOGGER.info("[Chams] Initialized");
    }

    private void onRender(WorldRenderContext ctx) {
        if (!enabled) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        LocalPlayer self = mc.player;
        Vec3 cam = ctx.camera().getPosition();

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glLineWidth(lineWidth);

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (!living.isAlive() || living.isSpectator()) continue;
            if (!selfChams && entity == self) continue;
            if (teamCheck && entity instanceof Player other && isFriendly(self, other)) continue;

            AABB box = living.getBoundingBox();
            double x0 = box.minX - cam.x, x1 = box.maxX - cam.x;
            double y0 = box.minY - cam.y, y1 = box.maxY - cam.y;
            double z0 = box.minZ - cam.z, z1 = box.maxZ - cam.z;

            // Pass 1 — through wall
            GL11.glDepthFunc(GL11.GL_ALWAYS);
            drawBox(x0, y0, z0, x1, y1, z1, occludedColor);

            // Pass 2 — visible portion
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            drawBox(x0, y0, z0, x1, y1, z1, visibleColor);
        }

        GL11.glPopAttrib();
    }

    private void drawBox(double x0, double y0, double z0,
                         double x1, double y1, double z1,
                         Color c) {
        float r = c.getRed()   / 255f;
        float g = c.getGreen() / 255f;
        float b = c.getBlue()  / 255f;
        float a = c.getAlpha() / 255f;
        GL11.glColor4f(r, g, b, a);

        if (filledChams) {
            GL11.glBegin(GL11.GL_QUADS);
            // bottom
            GL11.glVertex3d(x0,y0,z0); GL11.glVertex3d(x1,y0,z0);
            GL11.glVertex3d(x1,y0,z1); GL11.glVertex3d(x0,y0,z1);
            // top
            GL11.glVertex3d(x0,y1,z0); GL11.glVertex3d(x0,y1,z1);
            GL11.glVertex3d(x1,y1,z1); GL11.glVertex3d(x1,y1,z0);
            // north
            GL11.glVertex3d(x0,y0,z0); GL11.glVertex3d(x0,y1,z0);
            GL11.glVertex3d(x1,y1,z0); GL11.glVertex3d(x1,y0,z0);
            // south
            GL11.glVertex3d(x0,y0,z1); GL11.glVertex3d(x1,y0,z1);
            GL11.glVertex3d(x1,y1,z1); GL11.glVertex3d(x0,y1,z1);
            // west
            GL11.glVertex3d(x0,y0,z0); GL11.glVertex3d(x0,y0,z1);
            GL11.glVertex3d(x0,y1,z1); GL11.glVertex3d(x0,y1,z0);
            // east
            GL11.glVertex3d(x1,y0,z0); GL11.glVertex3d(x1,y1,z0);
            GL11.glVertex3d(x1,y1,z1); GL11.glVertex3d(x1,y0,z1);
            GL11.glEnd();
        } else {
            GL11.glBegin(GL11.GL_LINES);
            // bottom edges
            GL11.glVertex3d(x0,y0,z0); GL11.glVertex3d(x1,y0,z0);
            GL11.glVertex3d(x1,y0,z0); GL11.glVertex3d(x1,y0,z1);
            GL11.glVertex3d(x1,y0,z1); GL11.glVertex3d(x0,y0,z1);
            GL11.glVertex3d(x0,y0,z1); GL11.glVertex3d(x0,y0,z0);
            // top edges
            GL11.glVertex3d(x0,y1,z0); GL11.glVertex3d(x1,y1,z0);
            GL11.glVertex3d(x1,y1,z0); GL11.glVertex3d(x1,y1,z1);
            GL11.glVertex3d(x1,y1,z1); GL11.glVertex3d(x0,y1,z1);
            GL11.glVertex3d(x0,y1,z1); GL11.glVertex3d(x0,y1,z0);
            // verticals
            GL11.glVertex3d(x0,y0,z0); GL11.glVertex3d(x0,y1,z0);
            GL11.glVertex3d(x1,y0,z0); GL11.glVertex3d(x1,y1,z0);
            GL11.glVertex3d(x1,y0,z1); GL11.glVertex3d(x1,y1,z1);
            GL11.glVertex3d(x0,y0,z1); GL11.glVertex3d(x0,y1,z1);
            GL11.glEnd();
        }
    }

    private boolean isFriendly(LocalPlayer self, Player other) {
        if (self.getTeam() == null) return false;
        return self.getTeam().equals(other.getTeam());
    }

    public boolean isEnabled()               { return enabled; }
    public void    setEnabled(boolean v)     { enabled = v; }
    public Color   getVisibleColor()         { return visibleColor; }
    public void    setVisibleColor(Color c)  { visibleColor = c; }
    public Color   getOccludedColor()        { return occludedColor; }
    public void    setOccludedColor(Color c) { occludedColor = c; }
    public boolean isTeamCheck()             { return teamCheck; }
    public void    setTeamCheck(boolean v)   { teamCheck = v; }
    public boolean isSelfChams()             { return selfChams; }
    public void    setSelfChams(boolean v)   { selfChams = v; }
    public boolean isFilledChams()           { return filledChams; }
    public void    setFilledChams(boolean v) { filledChams = v; }
    public float   getLineWidth()            { return lineWidth; }
    public void    setLineWidth(float v)     { lineWidth = v; }
}
