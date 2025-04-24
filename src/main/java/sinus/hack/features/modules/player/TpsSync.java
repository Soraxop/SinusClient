package sinus.hack.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import sinus.hack.SinusHack;
import sinus.hack.core.Managers;
import sinus.hack.core.manager.client.ModuleManager;
import sinus.hack.events.impl.EventTick;
import sinus.hack.features.modules.Module;

public class TpsSync extends Module {
    public TpsSync() {
        super("TpsSync", Module.Category.PLAYER);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTick(EventTick e) {
        if (ModuleManager.timer.isEnabled()) return;
        if (Managers.SERVER.getTPS() > 1)
            SinusHack.TICK_TIMER = Managers.SERVER.getTPS() / 20f;
        else SinusHack.TICK_TIMER = 1f;
    }

    @Override
    public void onDisable() {
        SinusHack.TICK_TIMER = 1f;
    }
}
