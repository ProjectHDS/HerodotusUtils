package youyihj.herodotusutils.block.organism;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.organism.StructureTier;
import youyihj.herodotusutils.util.Util;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class BlockItemOutputInterface extends BlockStructure {
    public static final BlockItemOutputInterface BRASS = new BlockItemOutputInterface(StructureTier.BRASS);

    public BlockItemOutputInterface(StructureTier tier) {
        super("item_output_interface", tier);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileItemOutputInterface();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        Util.onBreakContainer(worldIn, pos);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && !playerIn.isSneaking()) {
            playerIn.openGui(HerodotusUtils.MOD_ID, 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
}
