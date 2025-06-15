package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import projecthds.herodotusutils.block.PlainBlock;
import projecthds.herodotusutils.item.ItemLithiumQuartz;
import projecthds.herodotusutils.item.ItemLithiumQuartzPowder;

public class BlockPowderedLithiumQuartz extends PlainBlock {

    public static final BlockPowderedLithiumQuartz INSTANCE = new BlockPowderedLithiumQuartz();

    private BlockPowderedLithiumQuartz() {
        super(Material.ROCK, "powdered_lithium_quartz_block");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {return true;}

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePowderedLithiumQuartzBlock();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        drops.add(1,new ItemStack(BlockLithiumQuartz.ITEM_BLOCK));
    }

    @Mod.EventBusSubscriber
    public class Logic{

        private static final IBlockState powdered_lithium_quartz_block_bs = BlockPowderedLithiumQuartz.INSTANCE.blockState.getBaseState();
        private static final IBlockState lithium_quartz_block_bs = BlockLithiumQuartz.INSTANCE.blockState.getBaseState();


        @SubscribeEvent
        public static void onRightClicked(PlayerInteractEvent.RightClickBlock event){
            if(event.getWorld().isRemote) {return;}

            if(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockLithiumQuartz
                && event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemLithiumQuartzPowder){
                    event.getEntityPlayer().getHeldItemMainhand().shrink(1);
                    event.getWorld().setBlockState(event.getPos(), powdered_lithium_quartz_block_bs);
            }
        }

        private static long latestTime = -1;
        @SubscribeEvent
        public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event){
            World world = event.getWorld();
            BlockPos pos = event.getPos();

            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos neighborPos = pos.offset(facing);
                IBlockState neighborState = world.getBlockState(neighborPos);

                if (neighborState.getBlock() instanceof BlockPowderedLithiumQuartz) {
                    TilePowderedLithiumQuartzBlock tile = (TilePowderedLithiumQuartzBlock) world.getTileEntity(neighborPos);
                    long time = world.getWorldTime();

                    if (latestTime == time) {return;}
                    latestTime = time;

                    tile.updateCount += 1;

                    if(tile.updateCount >= 10){
                        event.getWorld().spawnEntity(
                                new EntityItem(
                                        event.getWorld(),
                                        neighborPos.getX(),neighborPos.getY(),neighborPos.getZ(),
                                        new ItemStack(ItemLithiumQuartz.INSTANCE,1)
                                )
                        );
                        event.getWorld().setBlockState(neighborPos,lithium_quartz_block_bs);
                    }

                }
            }
        }

    }

}
