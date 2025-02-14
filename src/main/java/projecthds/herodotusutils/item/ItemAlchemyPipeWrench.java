package projecthds.herodotusutils.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import projecthds.herodotusutils.block.alchemy.AbstractPipeBlock;
import projecthds.herodotusutils.util.Util;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.alchemy.IAdjustableBlock;
import projecthds.herodotusutils.alchemy.IAdjustableTileEntity;

/**
 * @author youyihj
 */
public class ItemAlchemyPipeWrench extends Item {
    public static final String NAME = "alchemy_pipe_wrench";
    public static final ItemAlchemyPipeWrench INSTANCE = new ItemAlchemyPipeWrench();

    private ItemAlchemyPipeWrench() {
        this.setRegistryName(NAME);
        this.setTranslationKey(HerodotusUtils.MOD_ID + "." + NAME);
        this.setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    @SubscribeEvent
    public void rightClick(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        if (player.getHeldItem(event.getHand()).getItem() == this) {
            IBlockState blockState = world.getBlockState(event.getPos());
            Block block = blockState.getBlock();
            if (!player.isSneaking()) {
                if (block instanceof IAdjustableBlock) {
                    IAdjustableBlock adjustableBlock = (IAdjustableBlock) block;
                    IBlockState result = adjustableBlock.getAdjustedResult(blockState);
                    if (result != blockState) {
                        world.setBlockState(event.getPos(), result);
                    }
                    if (!world.isRemote) {
                        player.sendStatusMessage(adjustableBlock.getAdjustedMessage(result), true);
                    }
                    event.setUseBlock(Event.Result.DENY);
                    event.setUseItem(Event.Result.ALLOW);
                }
            } else {
                if (block instanceof AbstractPipeBlock) {
                    world.setBlockToAir(event.getPos());
                    block.dropBlockAsItem(world, event.getPos(), blockState, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public void leftClick(PlayerInteractEvent.LeftClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        if (player.getHeldItem(event.getHand()).getItem() == this && !world.isRemote) {
            Util.getTileEntity(world, event.getPos(), IAdjustableTileEntity.class).ifPresent(te -> {
                te.adjust(event.getFace(), event.getHitVec().subtract(new Vec3d(event.getPos())));
                event.setUseBlock(Event.Result.DENY);
                event.setUseItem(Event.Result.ALLOW);
            });
        }
    }
}
