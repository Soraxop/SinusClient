package sinus.hack.features.modules.render;

import sinus.hack.features.modules.Module;
import sinus.hack.setting.Setting;

public class AspectRatio extends Module {
    public AspectRatio() {
        super("AspectRatio", Category.RENDER);
    }

    public Setting<Float> ratio = new Setting<>("Ratio", 1.78f, 0.1f, 5f);
}
