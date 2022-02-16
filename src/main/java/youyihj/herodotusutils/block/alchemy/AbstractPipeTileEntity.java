package youyihj.herodotusutils.block.alchemy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.alchemy.IPipe;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public abstract class AbstractPipeTileEntity extends TileEntity implements IPipe {
    protected TileAlchemyController controller;

    @Nullable
    @Override
    public TileAlchemyController getLinkedController() {
        return controller;
    }

    @Override
    public void setLinkedController(TileAlchemyController tileAlchemyController) {
        controller = tileAlchemyController;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }
}
