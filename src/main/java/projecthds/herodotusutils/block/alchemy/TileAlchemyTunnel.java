package projecthds.herodotusutils.block.alchemy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import projecthds.herodotusutils.alchemy.IAlchemyModule;
import projecthds.herodotusutils.alchemy.IHasAlchemyFluid;
import projecthds.herodotusutils.alchemy.IHasAlchemyFluidModule;
import projecthds.herodotusutils.alchemy.InputResult;

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
        workInternal();
    }

    protected InputResult workInternal() {
        EnumFacing outputSide = getTransportDirection().getOutputSide();
        TileEntity tileEntity = world.getTileEntity(pos.offset(outputSide));
        if (tileEntity instanceof IHasAlchemyFluid)
            return IAlchemyModule.transferFluid(this, ((IHasAlchemyFluid) tileEntity), outputSide);
        else return InputResult.NO_OPERATION;
    }
}
