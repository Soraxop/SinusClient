package sinus.hack.events.impl;

import net.minecraft.entity.Entity;
import sinus.hack.events.Event;

public class EventEntityRemoved extends Event {
    public Entity entity;

    public EventEntityRemoved(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
