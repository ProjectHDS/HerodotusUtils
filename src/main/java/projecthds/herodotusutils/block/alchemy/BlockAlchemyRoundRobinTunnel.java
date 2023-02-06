package projecthds.herodotusutils.block.alchemy;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import projecthds.herodotusutils.util.Util;

import javax.annotation.Nonnull;

/**
 * @author youyihj
 */
public class BlockAlchemyRoundRobinTunnel extends AbstractPipeBlock implements IProbeInfoAccessor {
    private BlockAlchemyRoundRobinTunnel() {
        super("round_robin_tunnel");
    }

    public static final BlockAlchemyRoundRobinTunnel INSTANCE = new BlockAlchemyRoundRobinTunnel();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("round_robin_tunnel");

    @Nonnull
    @Override
    public AbstractPipeTileEntity createTileEntity(World world, IBlockState state) {
        return new TileAlchemyRoundRobinTunnel();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        Util.getTileEntity(world, data.getPos(), TileAlchemyRoundRobinTunnel.class).ifPresent(te -> {
            EnumFacing[] facingQuery = te.getFacingQuery();
            for (int i = 0; i < facingQuery.length; i++) {
                EnumFacing facing = facingQuery[i];
                probeInfo.text(I18n.format("hdsutils.alchemy.round.index", i, getFacingLocalizedInfo(facing)));
            }
            probeInfo.text(I18n.format("hdsutils.alchemy.round.next", getFacingLocalizedInfo(te.getNextOutputSide())));
        });
    }

    private String getFacingLocalizedInfo(EnumFacing facing) {
        return I18n.format("hdsutils.alchemy." + (facing == null ? "null" : facing.getName()));
    }
}
