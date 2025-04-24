package sinus.hack.features.modules.player;

import sinus.hack.features.modules.Module;
import sinus.hack.setting.Setting;

public class NoInteract extends Module {
    public NoInteract() {
        super("NoInteract", Category.PLAYER);
    }

    public static Setting<Boolean> onlyAura = new Setting<>("OnlyAura", false);
}
