package projecthds.herodotusutils.alchemy;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

/**
 * @author youyihj
 */
public interface IAdjustableTileEntity {
    void adjust(EnumFacing facing, Vec3d hitPosition);
}
