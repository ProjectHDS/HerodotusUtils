package youyihj.herodotusutils.block.alchemy;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import youyihj.herodotusutils.util.Util;

import javax.annotation.Nonnull;

/**
 * @author youyihj
 */
public class BlockLazyAlchemyTunnel extends BlockPlainAlchemyTunnel implements IProbeInfoAccessor {
    private BlockLazyAlchemyTunnel() {
        super("lazy_tunnel");
    }

    public static final BlockLazyAlchemyTunnel INSTANCE = new BlockLazyAlchemyTunnel();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("lazy_tunnel");

    @Nonnull
    @Override
    public AbstractPipeTileEntity createTileEntity(World world, IBlockState state) {
        return new TileAlchemyLazyTunnel();
    }

    @Override
    protected TunnelType getTunnelType() {
        return TunnelType.STRAIGHT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return STRAIGHT_AABB.getBoundingBox(state.getValue(property).getInputSide());
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        // TODO: Localization
        Util.getTileEntity(world, data.getPos(), TileAlchemyLazyTunnel.class).ifPresent(te -> {
            probeInfo.text("Bound: " + te.getBound())
                    .text("Counter: " + te.getCounter());
        });
    }
}
