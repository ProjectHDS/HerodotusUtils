package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import projecthds.herodotusutils.block.PlainBlock;
import projecthds.herodotusutils.config.HDSUConfig;
import projecthds.herodotusutils.item.ItemLithiumQuartz;
import projecthds.herodotusutils.item.ItemLithiumQuartzPowder;
import projecthds.herodotusutils.util.LogHelper;

import javax.annotation.Nullable;
import java.util.*;

public class BlockLithiumQuartzPowder extends PlainBlock {
    public static final BlockLithiumQuartzPowder INSTANCE = new BlockLithiumQuartzPowder();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("lithium_quartz_powder_block");

    private BlockLithiumQuartzPowder() {
        super(Material.GLASS, "lithium_quartz_powder_block");
        setTickRandomly(true);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileLithiumQuartzPowderBlock();
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        TileLithiumQuartzPowderBlock tile = (TileLithiumQuartzPowderBlock) worldIn.getTileEntity(pos);
        assert tile != null;
        if (tile.randomTickedCount < HDSUConfig.RequiredRandomTicksForQuartz) {
            tile.randomTickedCount = (short) (tile.randomTickedCount + 1);
        }
        else{
            worldIn.setBlockToAir(pos);
            worldIn.spawnEntity(
                    new EntityItem(
                            worldIn,pos.getX(),pos.getY(),pos.getZ(),
                            new ItemStack(ItemLithiumQuartzPowder.INSTANCE,HDSUConfig.PowderDropCountForQuartz)
                    )
            );
            if(tile.updatedTimes>= HDSUConfig.MinUpdatesForQuartz && tile.updatedTimes<=HDSUConfig.MaxUpdatesForQuartz){
                worldIn.spawnEntity(
                        new EntityItem(
                                worldIn,pos.getX(),pos.getY(),pos.getZ(),
                                new ItemStack(ItemLithiumQuartz.INSTANCE,1)
                        )
                );
            }
        }
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

                if (neighborState.getBlock() instanceof BlockLithiumQuartzPowder) {
                    TileLithiumQuartzPowderBlock tile = (TileLithiumQuartzPowderBlock) world.getTileEntity(neighborPos);
                    long time = world.getWorldTime();

                    if (latestTime == time) {return;}
                    latestTime = time;
                    tile.updatedTimes += 1;
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event){
            if(!(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockLithiumQuartzPowder) || event.getWorld().isRemote) {return;}

            if(event.getEntity() instanceof EntityPlayer){
                if(!event.getEntityPlayer().capabilities.isCreativeMode){return;}

                TileLithiumQuartzPowderBlock tile = (TileLithiumQuartzPowderBlock) event.getWorld().getTileEntity(event.getPos());
                event.getEntityPlayer().sendMessage(new TextComponentTranslation("hdsutils.message.block_lithium_quartz_powder_interacted",tile.updatedTimes));
            }
        }

    }

}