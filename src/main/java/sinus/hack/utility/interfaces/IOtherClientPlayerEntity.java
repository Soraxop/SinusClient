package sinus.hack.utility.interfaces;

import sinus.hack.features.modules.combat.Aura;

public interface IOtherClientPlayerEntity {
    void resolve(Aura.Resolver mode);

    void releaseResolver();
}
