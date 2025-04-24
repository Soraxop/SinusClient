package sinus.hack.features.modules.render;

import sinus.hack.features.modules.Module;
import sinus.hack.setting.Setting;

public class FOV extends Module {
    public FOV() {
        super("FOV", Category.RENDER);
    }

    public final Setting<Integer> fovModifier = new Setting<>("FOV modifier", 120, 0, 358);
    public final Setting<Boolean> itemFov = new Setting<>("Item Fov", false);
    public final Setting<Integer> itemFovModifier = new Setting<>("Item FOV modifier", 120, 0, 358);
}