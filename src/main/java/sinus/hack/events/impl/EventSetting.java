package sinus.hack.events.impl;

import sinus.hack.events.Event;
import sinus.hack.setting.Setting;

public class EventSetting extends Event {
    final Setting<?> setting;

    public EventSetting(Setting<?> setting){
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return setting;
    }
}
