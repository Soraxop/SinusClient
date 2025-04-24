package sinus.hack.utility.interfaces;

import net.minecraft.util.math.BlockPos;
import sinus.hack.features.modules.render.Trails;

import java.util.List;

public interface IEntity {
    List<Trails.Trail> getTrails();

    BlockPos sinusHack$getVelocityBP();
}
