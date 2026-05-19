package net.mustyclient.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.mustyclient.MustyClient;

import java.awt.Color;

/**
 * Chams ESP — renders players through walls with a flat/solid color overlay.
 *
 * Two-pass approach:
 *   Pass 1 (through-wall): GL_ALWAYS depth func, semi-transparent fill — shows enemy through terrain.
 *   Pass 2 (normal):       GL_LEQUAL depth func, solid fill — renders the visible portion brighter.
 *
 * Colors are configurable per-state: visible vs. occluded.
 */
public class Chams {

    // ---- configurable ----
    private Color visibleColor   = new Color(255, 50,  50,  180); // red, solid portion
    private Color occludedColor  = new Color(255, 50,  50,  60);  // red, through-wall portion
    private boolean teamCheck    = true;   // skip teammates
    private boolean selfChams    = false;  // include local player
    private boolean enabled      = false;
    private boolean filledChams  = true;   // flat fill vs. wireframe
    private float   lineWidth    = 1.5f;   // wireframe line width

    // ---- internal ----
    private Minecraft mc;

    public void init() {
        mc = Minecraft.getInstance();

        // Hook into the world render event — fires after normal entity rendering
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (!enabled) return;
            if (mc.player == null || mc.level == null) return;
            renderChams(context);
        });

        MustyClient.LOGGER.info("[Chams] Initialized");
    }

    private void renderChams(net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext context) {
        LocalPlayer self = mc.player;
        com.mojang.blaze3d.vertex.PoseStack poseStack = context.matrixStack();
        if (poseStack == null) return;

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (!living.isAlive()) continue;
            if (living.isSpectator()) continue;
            if (!selfChams && entity == self) continue;
            if (teamCheck && entity instanceof Player other && isFriendly(self, other)) continue;

            renderEntityChams(poseStack, context, living);
        }
    }

    private void renderEntityChams(
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext context,
            LivingEntity entity) {

        com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
        com.mojang.blaze3d.systems.RenderSystem.disableCull();

        // Through-wall pass
        drawEntityBox(poseStack, context, entity, occludedColor);

        com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();

        // Visible pass
        drawEntityBox(poseStack, context, entity, visibleColor);

        com.mojang.blaze3d.systems.RenderSystem.enableCull();
    }

    private void drawEntityBox(
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext context,
            LivingEntity entity,
            Color color) {

        net.minecraft.world.phys.AABB box = entity.getBoundingBox();
        net.minecraft.world.phys.Vec3 camPos = context.camera().getPosition();

        double x = (box.minX + box.maxX) / 2.0 - camPos.x;
        double y = box.minY - camPos.y;
        double z = (box.minZ + box.maxZ) / 2.0 - camPos.z;
        double hw = (box.maxX - box.minX) / 2.0;
        double h  = box.maxY - box.minY;
        double hd = (box.maxZ - box.minZ) / 2.0;

        poseStack.pushPose();
        poseStack.translate(x, y, z);

        com.mojang.blaze3d.vertex.BufferBuilder buf = com.mojang.blaze3d.vertex.Tesselator.getInstance().begin(
                filledChams
                        ? com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS
                        : com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINES,
                com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR
        );

        float r = color.getRed()   / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue()  / 255f;
        float a = color.getAlpha() / 255f;

        com.mojang.math.Matrix4f mat = poseStack.last().pose();

        // 6 faces of the AABB
        float x0 = (float)-hw, x1 = (float)hw;
        float y0 = 0,          y1 = (float)h;
        float z0 = (float)-hd, z1 = (float)hd;

        // bottom
        buf.addVertex(mat, x0, y0, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y0, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y0, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x0, y0, z1).setColor(r, g, b, a);
        // top
        buf.addVertex(mat, x0, y1, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x0, y1, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y1, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y1, z0).setColor(r, g, b, a);
        // north
        buf.addVertex(mat, x0, y0, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x0, y1, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y1, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y0, z0).setColor(r, g, b, a);
        // south
        buf.addVertex(mat, x0, y0, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y0, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y1, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x0, y1, z1).setColor(r, g, b, a);
        // west
        buf.addVertex(mat, x0, y0, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x0, y0, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x0, y1, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x0, y1, z0).setColor(r, g, b, a);
        // east
        buf.addVertex(mat, x1, y0, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y1, z0).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y1, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x1, y0, z1).setColor(r, g, b, a);

        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
        net.minecraft.client.renderer.GameRenderer.getPositionColorShader().apply();
        com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(buf.buildOrThrow());
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private boolean isFriendly(LocalPlayer self, Player other) {
        if (self.getTeam() == null) return false;
        return self.getTeam().equals(other.getTeam());
    }

    // ---- getters / setters for ClickGui ----
    public boolean isEnabled()                       { return enabled; }
    public void    setEnabled(boolean v)             { enabled = v; }
    public Color   getVisibleColor()                 { return visibleColor; }
    public void    setVisibleColor(Color c)          { visibleColor = c; }
    public Color   getOccludedColor()                { return occludedColor; }
    public void    setOccludedColor(Color c)         { occludedColor = c; }
    public boolean isTeamCheck()                     { return teamCheck; }
    public void    setTeamCheck(boolean v)           { teamCheck = v; }
    public boolean isSelfChams()                     { return selfChams; }
    public void    setSelfChams(boolean v)           { selfChams = v; }
    public boolean isFilledChams()                   { return filledChams; }
    public void    setFilledChams(boolean v)         { filledChams = v; }
    public float   getLineWidth()                    { return lineWidth; }
    public void    setLineWidth(float v)             { lineWidth = v; }
}
