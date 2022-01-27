package youyihj.herodotusutils.item;

import crafttweaker.mc1120.util.MCPosition3f;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.alchemy.IAdjustableBlock;
import youyihj.herodotusutils.alchemy.IAdjustableTileEntity;
import youyihj.herodotusutils.util.Util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author youyihj
 */
public class ItemAlchemyPipeWrench extends Item {
    public static final String NAME = "alchemy_pipe_wrench";
    public static final ItemAlchemyPipeWrench INSTANCE = new ItemAlchemyPipeWrench();

    private ItemAlchemyPipeWrench() {
        this.setRegistryName(NAME);
        this.setUnlocalizedName(HerodotusUtils.MOD_ID + "." + NAME);
        this.setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        World world = player.world;
        boolean sneaking = player.isSneaking();
        IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        AtomicBoolean adjusted = new AtomicBoolean(false);
        if (!sneaking && block instanceof IAdjustableBlock) {
            IAdjustableBlock adjustableBlock = (IAdjustableBlock) block;
            IBlockState result = adjustableBlock.getAdjustedResult(blockState);
            if (result != blockState) {
                world.setBlockState(pos, result);
                if (!world.isRemote) {
                    player.sendStatusMessage(adjustableBlock.getAdjustedMessage(result), true);
                }
                adjusted.set(true);
            }
        }
        if (sneaking) {
            Util.getTileEntity(world, pos, IAdjustableTileEntity.class).ifPresent(te -> {
                te.adjust(facing, new MCPosition3f(hitX, hitY, hitZ));
                adjusted.set(true);
            });
        }
        return adjusted.get() ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }
}
