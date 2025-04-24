package sinus.hack.events.impl;

import net.minecraft.util.math.BlockPos;
import sinus.hack.events.Event;

public class EventBreakBlock extends Event {
    private BlockPos bp;

    public EventBreakBlock(BlockPos bp) {
        this.bp = bp;
    }

    public BlockPos getPos() {
        return bp;
    }
}
