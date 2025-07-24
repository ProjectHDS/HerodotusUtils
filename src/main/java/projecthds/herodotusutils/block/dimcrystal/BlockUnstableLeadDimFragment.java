package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import projecthds.herodotusutils.HerodotusUtils;

import static projecthds.herodotusutils.config.HDSUConfig.BlockUnstableLeadDimFragmentActivationDelay;


public class BlockUnstableLeadDimFragment extends BlockFalling {
    public static final BlockUnstableLeadDimFragment  INSTANCE = new BlockUnstableLeadDimFragment();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("unstable_lead_dim_fragment");
    public BlockUnstableLeadDimFragment() {
        super(Material.ROCK);
        this.setRegistryName("unstable_lead_dim_fragment");
        this.setCreativeTab(CreativeTabs.MISC);
        this.blockHardness = 5.0f;
        this.blockResistance = 50.0f;
        this.setTranslationKey(HerodotusUtils.MOD_ID + "." + "unstable_lead_dim_fragment");
    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, BlockUnstableLeadDimFragmentActivationDelay);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();
        return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
}
