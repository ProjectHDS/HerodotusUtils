package projecthds.herodotusutils.block.alchemy;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import projecthds.herodotusutils.alchemy.IAdjustableBlock;
import projecthds.herodotusutils.util.Util;

import javax.annotation.Nonnull;

/**
 * @author youyihj
 */
public class BlockAlchemyOutputHatch extends AbstractPipeBlock implements IAdjustableBlock {
    private BlockAlchemyOutputHatch() {
        super("alchemy_output_hatch");
    }

    public static final BlockAlchemyOutputHatch INSTANCE = new BlockAlchemyOutputHatch();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("alchemy_output_hatch");
    private static final AxisAlignedBB BOUNDING_BOX = Util.createAABBFromModelPos(2, 0, 2, 13, 11, 13);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Nonnull
    @Override
    public AbstractPipeTileEntity createTileEntity(World world, IBlockState state) {
        return new TileAlchemyOutputHatch();
    }

    @Override
    public IBlockState getAdjustedResult(IBlockState previous) {
        return getDefaultState().withProperty(BlockHorizontal.FACING, Util.getCycledNextElement(EnumFacing.HORIZONTALS, previous.getValue(BlockHorizontal.FACING)));
    }
}
