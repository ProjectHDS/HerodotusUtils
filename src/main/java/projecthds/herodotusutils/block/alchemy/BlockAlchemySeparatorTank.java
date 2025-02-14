package projecthds.herodotusutils.block.alchemy;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import projecthds.herodotusutils.util.Util;

import javax.annotation.Nonnull;

/**
 * @author youyihj
 */
public class BlockAlchemySeparatorTank extends AbstractPipeBlock {
    /* package-private */ static final PropertyInteger NUMBER = PropertyInteger.create("number", 1, 4);

    private BlockAlchemySeparatorTank() {
        super("alchemy_separator_tank");
    }

    public static final BlockAlchemySeparatorTank INSTANCE = new BlockAlchemySeparatorTank();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE) {
        @Override
        public String getTranslationKey(ItemStack stack) {
            return super.getTranslationKey(stack) + "." + stack.getMetadata();
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }
    }.setRegistryName("alchemy_separator_tank").setHasSubtypes(true);

    private static final AxisAlignedBB BOUNDING_BOX = Util.createAABBFromModelPos(3, 0, 3, 13, 12, 13);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NUMBER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(NUMBER, meta + 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(NUMBER) - 1;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(meta);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 4; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Nonnull
    @Override
    public AbstractPipeTileEntity createTileEntity(World world, IBlockState state) {
        return new TileAlchemySeparatorTank();
    }
}
