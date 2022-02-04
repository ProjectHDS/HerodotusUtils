package youyihj.herodotusutils.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import youyihj.herodotusutils.util.Util;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class BlockCreatureDataReEncodeInterface extends PlainBlock {
    private BlockCreatureDataReEncodeInterface() {
        super(Material.IRON, NAME);
    }

    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 15);
    public static final BlockCreatureDataReEncodeInterface INSTANCE = new BlockCreatureDataReEncodeInterface();
    public static final String NAME = "creature_data_re_encode_interface";
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE) {
        @Override
        public String getUnlocalizedName(ItemStack stack) {
            return super.getUnlocalizedName(stack) + "." + stack.getMetadata();
        }

        @Override
        public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
            boolean result = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
            if (result) {
                int channel = stack.hasTagCompound() ? stack.getTagCompound().getInteger("channel") : TileCreatureDataAnalyzer.UNDETERMINED_CHANNEL;
                Util.getTileEntity(world, pos, TileCreatureDataReEncodeInterface.class).ifPresent(te -> {
                    te.setChannel(channel);
                });
                return true;
            } else {
                return false;
            }
        }
    }.setRegistryName(NAME);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ItemStack stack = new ItemStack(ITEM_BLOCK);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCreatureDataReEncodeInterface) {
            int channel = ((TileCreatureDataReEncodeInterface) tileEntity).getChannel();
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("channel", channel);
            stack.setTagCompound(nbt);
        }
        drops.add(stack);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCreatureDataReEncodeInterface();
    }
}
