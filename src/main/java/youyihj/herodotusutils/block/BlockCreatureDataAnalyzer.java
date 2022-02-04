package youyihj.herodotusutils.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.util.Util;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class BlockCreatureDataAnalyzer extends PlainBlock {
    private BlockCreatureDataAnalyzer() {
        super(Material.IRON, NAME);
    }

    public static final BlockCreatureDataAnalyzer INSTANCE = new BlockCreatureDataAnalyzer();
    public static final String NAME = "creature_data_analyzer";
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE) {
        @Override
        public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
            boolean result = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
            if (result) {
                int channel = stack.hasTagCompound() ? stack.getTagCompound().getInteger("channel") : TileCreatureDataAnalyzer.UNDETERMINED_CHANNEL;
                Util.getTileEntity(world, pos, TileCreatureDataAnalyzer.class).ifPresent(te -> {
                    te.setChannel(channel);
                });
                return true;
            } else {
                return false;
            }
        }
    }.setRegistryName(NAME);

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        Util.onBreakContainer(worldIn, pos);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ItemStack stack = new ItemStack(ITEM_BLOCK);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCreatureDataAnalyzer) {
            int channel = ((TileCreatureDataAnalyzer) tileEntity).getChannel();
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("channel", channel);
            stack.setTagCompound(nbt);
        }
        drops.add(stack);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && !playerIn.isSneaking()) {
            playerIn.openGui(HerodotusUtils.MOD_ID, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCreatureDataAnalyzer();
    }
}
