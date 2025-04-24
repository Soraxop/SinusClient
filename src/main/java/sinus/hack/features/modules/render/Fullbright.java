package sinus.hack.features.modules.render;

import sinus.hack.features.modules.Module;
import sinus.hack.setting.Setting;

public class Fullbright extends Module {
    public Fullbright() {
        super("Fullbright", Category.RENDER);
    }

    public static Setting<Float> minBright = new Setting<>("MinBright", 0.5f, 0f, 1f);
}
