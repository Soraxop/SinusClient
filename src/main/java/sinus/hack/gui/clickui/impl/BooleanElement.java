package sinus.hack.gui.clickui.impl;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import sinus.hack.core.Managers;
import sinus.hack.features.modules.client.HudEditor;
import sinus.hack.gui.clickui.AbstractElement;
import sinus.hack.gui.clickui.ClickGUI;
import sinus.hack.gui.font.FontRenderers;
import sinus.hack.setting.Setting;
import sinus.hack.utility.render.Render2DEngine;

import java.awt.*;

import static sinus.hack.core.manager.IManager.mc;
import static sinus.hack.utility.render.animation.AnimationUtility.fast;

public class BooleanElement extends AbstractElement {
    public BooleanElement(Setting setting) {
        super(setting);
    }

    float animation = 0f;
    float animation2 = 0f;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        Render2DEngine.drawRound(context.getMatrices(), x + width - 21, y + height / 2 - 4, 15, 8, 1, 7f * animation > 4 ? HudEditor.getColor(0) : new Color(0x28FFFFFF, true));

        animation = fast(animation, (boolean) setting.getValue() ? 1 : 0, 20f);
        animation2 = fast(animation2, (boolean) setting.getValue() ? 1 : 0, 8f);
        Render2DEngine.drawRound(context.getMatrices(), x + width - 20 + 7f * animation, y + height / 2 - 3, 6, 6, 1, new Color(-1));
        Render2DEngine.drawRound(context.getMatrices(), x + width - 20 + 7f * animation2, y + height / 2 - 3, 6, 6, 1, new Color(-1));

        if (7f * animation > 4) {
            FontRenderers.sf_bold_mini.drawString(context.getMatrices(), "v", x + width - 19f, y + height / 2 - 2f, new Color(-1).getRGB());
        } else {
            FontRenderers.sf_bold_mini.drawString(context.getMatrices(), "x", x + width - 12f, y + height / 2 - 2f, new Color(-1).getRGB());
        }

        if (Render2DEngine.isHovered(mouseX, mouseY, x + width - 21, y + height / 2 - 4, 15, 8)) {
            if (GLFW.glfwGetPlatform() != GLFW.GLFW_PLATFORM_WAYLAND) {
                GLFW.glfwSetCursor(mc.getWindow().getHandle(),
                        GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR));
            }
            ClickGUI.anyHovered = true;
        }

        FontRenderers.sf_medium_mini.drawString(context.getMatrices(), setting.getName(), (setting.group != null ? 2f : 0f) + (x + 6), (y + height / 2 - 3) + 2, new Color(-1).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovered && button == 0) {
            setting.setValue(!((Boolean) setting.getValue()));
            Managers.SOUND.playBoolean();
        }

        super.mouseClicked(mouseX, mouseY, button);
    }
}