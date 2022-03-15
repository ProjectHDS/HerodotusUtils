package youyihj.herodotusutils.mixins.interfaces;

import WayofTime.bloodmagic.altar.AltarTier;

/**
 * @author youyihj
 */
public interface IBloodAltarPatch {
    void setBuildingTier(AltarTier tier);

    boolean isBuilding();
}
