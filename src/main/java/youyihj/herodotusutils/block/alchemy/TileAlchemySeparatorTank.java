package youyihj.herodotusutils.block.alchemy;

import net.minecraft.util.EnumFacing;
import youyihj.herodotusutils.alchemy.IAlchemyModule;

/**
 * @author youyihj
 */
public class TileAlchemySeparatorTank extends AbstractHasAlchemyFluidTileEntity implements IAlchemyModule {
    @Override
    public void work() {
        EnumFacing outputSide = outputSide();
        if (outputSide != null) {
            IAlchemyModule.transferFluid(this, world, pos, outputSide);
        }
    }

    @Override
    public EnumFacing inputSide() {
        return null;
    }

    @Override
    public EnumFacing outputSide() {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (world.getTileEntity(pos.offset(facing)) instanceof TileAlchemySeparator) {
                return facing.getOpposite();
            }
        }
        return null;
    }
}
