package sinus.hack.injection;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sinus.hack.features.modules.client.ClientSettings;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sinus.hack.core.manager.client.ModuleManager;
import sinus.hack.utility.render.Render2DEngine;
import sinus.hack.utility.render.TextureStorage;

import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

import static sinus.hack.features.modules.Module.mc;

@Mixin(SplashOverlay.class)
public abstract class MixinSplashOverlay {
    @Final @Shadow private boolean reloading;
    @Shadow private float progress;
    @Shadow private long reloadCompleteTime = -1L;
    @Shadow private long reloadStartTime = -1L;
    @Final @Shadow private ResourceReload reload;
    @Final @Shadow private Consumer<Optional<Throwable>> exceptionHandler;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(ModuleManager.unHook.isEnabled() || !ClientSettings.customLoadingScreen.getValue())
            return;
        ci.cancel();
        renderCustom(context, mouseX, mouseY, delta);
    }

    public void renderCustom(DrawContext context, int mouseX, int mouseY, float delta) {
        int screenWidth = mc.getWindow().getScaledWidth();
        int screenHeight = mc.getWindow().getScaledHeight();
        long time = Util.getMeasuringTimeMs();

        if (reloading && reloadStartTime == -1L) reloadStartTime = time;

        float completeTime = reloadCompleteTime > -1L ? (time - reloadCompleteTime) / 1000.0F : -1.0F;
        float startTime = reloadStartTime > -1L ? (time - reloadStartTime) / 500.0F : -1.0F;
        float alpha;

        if (completeTime >= 1.0F) {
            if (mc.currentScreen != null) mc.currentScreen.render(context, 0, 0, delta);
            int fade = MathHelper.ceil((1.0F - MathHelper.clamp(completeTime - 1.0F, 0.0F, 1.0F)) * 255.0F);
            context.fill(0, 0, screenWidth, screenHeight, withAlpha(new Color(0x070015).getRGB(), fade));
            alpha = 1.0F - MathHelper.clamp(completeTime - 1.0F, 0.0F, 1.0F);
        } else if (reloading) {
            if (mc.currentScreen != null && startTime < 1.0F)
                mc.currentScreen.render(context, mouseX, mouseY, delta);

            int fade = MathHelper.ceil(MathHelper.clamp(startTime, 0.15, 1.0) * 255.0);
            context.fill(0, 0, screenWidth, screenHeight, withAlpha(new Color(0x070015).getRGB(), fade));
            alpha = MathHelper.clamp(startTime, 0.0F, 1.0F);
        } else {
            int bg = new Color(0x070015).getRGB();
            float r = (float) (bg >> 16 & 255) / 255.0F;
            float g = (float) (bg >> 8 & 255) / 255.0F;
            float b = (float) (bg & 255) / 255.0F;
            GlStateManager._clearColor(r, g, b, 1.0F);
            GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
            alpha = 1.0F;
        }

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // Логотип
        int logoSize = 180;
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        context.drawTexture(TextureStorage.sinusLogo, centerX - logoSize / 2, centerY - logoSize / 2 - 20, 0, 0, logoSize, logoSize, logoSize, logoSize);

        // Прогресс-бар (с glow-пульсацией)
        float barProgress = this.reload.getProgress();
        this.progress = MathHelper.clamp(this.progress * 0.95F + barProgress * 0.050000012F, 0.0F, 1.0F);
        int barWidth = 220;
        int barHeight = 6;
        int barX = centerX - barWidth / 2;
        int barY = centerY + logoSize / 2 + 5;
        int glowWidth = 220;
        int glowHeight = 6;
        context.drawTexture(TextureStorage.sinusBar, barX, barY, 0, 0, barWidth, barHeight, barWidth, barHeight);
        // эффект glow
        float glowAlpha = (float) (0.5F + 0.5F * Math.sin(Util.getMeasuringTimeMs() / 200.0));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, glowAlpha);
        context.drawTexture(TextureStorage.sinusBar, barX - 10, barY - 6, 0, 0, barWidth + 20, barHeight + 12, barWidth + 20, barHeight + 12);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.disableBlend();

        // Завершение
        if (completeTime >= 2.0F) mc.setOverlay(null);

        if (reloadCompleteTime == -1L && reload.isComplete() && (!reloading || startTime >= 2.0F)) {
            try {
                reload.throwException();
                exceptionHandler.accept(Optional.empty());
            } catch (Throwable t) {
                exceptionHandler.accept(Optional.of(t));
            }
            reloadCompleteTime = Util.getMeasuringTimeMs();
            if (mc.currentScreen != null) {
                mc.currentScreen.init(mc, screenWidth, screenHeight);
            }
        }
    }

    private static int withAlpha(int color, int alpha) {
        return color & 16777215 | alpha << 24;
    }
}
