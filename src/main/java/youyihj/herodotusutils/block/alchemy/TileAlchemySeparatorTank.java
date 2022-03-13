package youyihj.herodotusutils.block.alchemy;

import net.minecraft.util.EnumFacing;
import youyihj.herodotusutils.alchemy.IAlchemyModule;
import youyihj.herodotusutils.alchemy.IHasAlchemyFluidModule;

/**
 * @author youyihj
 */
public class TileAlchemySeparatorTank extends AbstractHasAlchemyFluidTileEntity implements IHasAlchemyFluidModule {
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
        return EnumFacing.DOWN;
    }
}
