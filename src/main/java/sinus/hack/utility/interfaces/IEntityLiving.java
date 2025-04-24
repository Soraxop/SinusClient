package sinus.hack.utility.interfaces;

import sinus.hack.features.modules.combat.Aura;

import java.util.List;

public interface IEntityLiving {
    double getPrevServerX();

    double getPrevServerY();

    double getPrevServerZ();

    List<Aura.Position> getPositionHistory();
}
