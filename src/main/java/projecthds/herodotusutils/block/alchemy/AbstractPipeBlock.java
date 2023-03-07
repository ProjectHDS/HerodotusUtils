package projecthds.herodotusutils.block.alchemy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import projecthds.herodotusutils.alchemy.IHasAlchemyFluid;
import projecthds.herodotusutils.alchemy.IPipe;
import projecthds.herodotusutils.block.PlainBlock;
import projecthds.herodotusutils.util.Util;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author youyihj
 */
public abstract class AbstractPipeBlock extends PlainBlock {

    protected AbstractPipeBlock(String name) {
        super(Material.IRON, name);
        this.setHarvestLevel("pickaxe", 2);
        this.fullBlock = false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public abstract AbstractPipeTileEntity createTileEntity(World world, IBlockState state);

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        recalculatePipes(worldIn, pos);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        recalculatePipes(worldIn, pos);
        if (isNoPipeConnected(worldIn, pos)) {
            Util.getTileEntity(worldIn, pos, IHasAlchemyFluid.class).ifPresent(IHasAlchemyFluid::emptyFluid);
        }
    }

    private void recalculatePipes(World world, BlockPos pos) {
        Util.getTileEntity(world, pos, IPipe.class)
                .map(IPipe::getLinkedController)
                .ifPresent(TileAlchemyController::startScanPipes);
    }

    private boolean isNoPipeConnected(World world, BlockPos pos) {
        return Arrays.stream(EnumFacing.values())
                .map(pos::offset)
                .map(world::getTileEntity)
                .noneMatch(IPipe.class::isInstance);
    }
}
