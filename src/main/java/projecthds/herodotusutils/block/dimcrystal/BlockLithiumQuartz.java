package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.block.PlainBlock;
import projecthds.herodotusutils.item.ItemLithiumQuartz;
import projecthds.herodotusutils.item.ItemLithiumQuartzPowder;

import javax.annotation.Nullable;

public class BlockLithiumQuartz extends PlainBlock {
    public static final BlockLithiumQuartz INSTANCE = new BlockLithiumQuartz();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("lithium_quartz_block");

    private BlockLithiumQuartz() {
        super(Material.GLASS, "lithium_quartz_block");
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

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TileLithiumQuartz(); }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }



    @Mod.EventBusSubscriber
    public class Logic{
        private static long latesetTickTriggering = -1;
        @SubscribeEvent
        public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event){
            World world = event.getWorld();
            BlockPos pos = event.getPos();

            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos neighborPos = pos.offset(facing);
                IBlockState neighborState = world.getBlockState(neighborPos);

                if (neighborState.getBlock() instanceof BlockLithiumQuartz) {
                    long timeCurrent = world.getWorldTime();
                    if (latesetTickTriggering == timeCurrent) {return;}
                    latesetTickTriggering = timeCurrent;

                    TileLithiumQuartz tileLithiumQuartz = (TileLithiumQuartz) world.getTileEntity(neighborPos);
                    System.out.println("//time is " + timeCurrent);
                    System.out.println("pos is "+ neighborPos + " //");
                    if (tileLithiumQuartz.timeRecorded == -1
                            || ((timeCurrent - tileLithiumQuartz.timeRecorded) / 20) >= 1  ){
                        ItemStack stackToDrop = new ItemStack(ItemLithiumQuartzPowder.INSTANCE,RANDOM.nextInt(3)+1);
                        EntityItem entityItem = new EntityItem(world,neighborPos.getX(),neighborPos.getY(),neighborPos.getZ(),stackToDrop);
                        world.spawnEntity(entityItem);
                    }
                    else{
                        world.newExplosion(null,neighborPos.getX(),neighborPos.getY(),neighborPos.getZ(),3.0F,false,true);
                    }

                    tileLithiumQuartz.timeRecorded = timeCurrent;

                }
            }

        }

    }

}
