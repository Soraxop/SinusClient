package sinus.hack.features.modules.render;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import sinus.hack.core.Managers;
import sinus.hack.core.manager.client.ModuleManager;
import sinus.hack.events.impl.EventAttack;
import sinus.hack.injection.accesors.IClientPlayerEntity;
import sinus.hack.features.modules.Module;
import sinus.hack.setting.Setting;
import sinus.hack.utility.Timer;

import java.util.ArrayList;

import static sinus.hack.utility.render.Render2DEngine.drawBubble;

public class HitBubbles extends Module {
    public HitBubbles() {
        super("HitBubbles", Category.RENDER);
    }

    public final Setting<Integer> lifeTime = new Setting<>("LifeTime", 30, 1, 150);

    private final ArrayList<HitBubble> bubbles = new ArrayList<>();

    @EventHandler
    public void onHit(EventAttack e) {
        Vec3d point = Managers.PLAYER.getRtxPoint(((IClientPlayerEntity) mc.player).getLastYaw(), ((IClientPlayerEntity) mc.player).getLastPitch(), ModuleManager.aura.attackRange.getValue());
        if (point != null && !e.isPre())
            bubbles.add(new HitBubble((float) point.x, (float) point.y, (float) point.z, -((IClientPlayerEntity) mc.player).getLastYaw(), ((IClientPlayerEntity) mc.player).getLastPitch(), new Timer()));
    }

    public void onRender3D(MatrixStack matrixStack) {
        RenderSystem.disableDepthTest();
        ArrayList<HitBubble> bubblesCopy = Lists.newArrayList(bubbles);
        bubblesCopy.forEach(b -> {
            matrixStack.push();
            matrixStack.translate(b.x - mc.getEntityRenderDispatcher().camera.getPos().getX(), b.y - mc.getEntityRenderDispatcher().camera.getPos().getY(), b.z - mc.getEntityRenderDispatcher().camera.getPos().getZ());
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(b.yaw));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(b.pitch));
            drawBubble(matrixStack, -b.life.getPassedTimeMs() / 4f, b.life.getPassedTimeMs() / 1500f);
            matrixStack.pop();
        });
        RenderSystem.enableDepthTest();
        bubbles.removeIf(b -> b.life.passedMs(lifeTime.getValue() * 50));
    }

    public record HitBubble(float x, float y, float z, float yaw, float pitch, Timer life) {
    }
}
