package youyihj.herodotusutils.block.alchemy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import youyihj.herodotusutils.alchemy.IAlchemyModule;
import youyihj.herodotusutils.alchemy.IHasAlchemyFluid;
import youyihj.herodotusutils.alchemy.IHasAlchemyFluidModule;

/**
 * @author youyihj
 */
public class TileAlchemyTunnel extends AbstractHasAlchemyFluidTileEntity implements IHasAlchemyFluidModule {
    private BlockPlainAlchemyTunnel.TransferDirection getTransportDirection() {
        IBlockState blockState = world.getBlockState(pos);
        return ((BlockPlainAlchemyTunnel) blockState.getBlock()).getDirection(blockState);
    }

    @Override
    public EnumFacing inputSide() {
        return getTransportDirection().getInputSide();
    }

    @Override
    public EnumFacing outputSide() {
        return getTransportDirection().getOutputSide();
    }

    @Override
    public void work() {
        EnumFacing outputSide = getTransportDirection().getOutputSide();
        TileEntity tileEntity = world.getTileEntity(pos.offset(outputSide));
        if (tileEntity instanceof IHasAlchemyFluid)
            IAlchemyModule.transferFluid(this, ((IHasAlchemyFluid) tileEntity), outputSide);
    }
}
