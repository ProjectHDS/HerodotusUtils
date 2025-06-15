package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import projecthds.herodotusutils.block.PlainBlock;

public class BlockLithiumQuartz extends PlainBlock {
    public static final BlockLithiumQuartz INSTANCE = new BlockLithiumQuartz();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("lithium_quartz_block");
    private BlockLithiumQuartz() {
        super(Material.ROCK,"lithium_quartz_block");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {return true;}

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileLithiumQuartzBlock();
    }

    @Mod.EventBusSubscriber
    public class Logic{
        private static long latestTime = -1;


        @SubscribeEvent
        public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event){
            World world = event.getWorld();
            BlockPos pos = event.getPos();

            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos neighborPos = pos.offset(facing);
                IBlockState neighborState = world.getBlockState(neighborPos);

                if (neighborState.getBlock() instanceof BlockLithiumQuartz) {
                    TileLithiumQuartzBlock tile = (TileLithiumQuartzBlock) world.getTileEntity(neighborPos);
                    long time = world.getWorldTime();

                    if (latestTime == time) {return;}
                    latestTime = time;

                    if(tile.updateCount < 7){
                        tile.updateCount += 1;
                    }
                    else {
                        float randomNumber = RANDOM.nextFloat();
                        if(randomNumber <= 0.25){
                            world.newExplosion(null,neighborPos.getX(),neighborPos.getY(),neighborPos.getZ(),3.0F,false,true);
                        }

                    }

                }
            }
        }


    }

}
