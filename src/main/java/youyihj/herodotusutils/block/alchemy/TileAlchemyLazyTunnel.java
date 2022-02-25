package youyihj.herodotusutils.block.alchemy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import youyihj.herodotusutils.alchemy.IAdjustableTileEntity;
import youyihj.herodotusutils.alchemy.InputResult;

import javax.annotation.Nonnull;

/**
 * @author youyihj
 */
public class TileAlchemyLazyTunnel extends TileAlchemyTunnel implements IAdjustableTileEntity {
    private static final int MAX_BOUND = 8;
    private static final int MIN_BOUND = 2;
    private int bound = MIN_BOUND;
    private int counter;
    private EnumFacing cacheOutputFacing;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("bound", bound);
        compound.setInteger("counter", counter);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.setBound(compound.getInteger("bound"), false);
        this.counter = compound.getInteger("counter");
    }

    @Override
    public void work() {
        if (content == null)
            return;
        if (bound > counter) {
            counter++;
        }
        if (bound == counter) {
            InputResult result = super.workInternal();
            if (result == InputResult.SUCCESS) {
                counter = 0;
            }
        }
        syncToTrackingClients();
    }

    @Override
    public void  callBackWork() {
        if (bound == counter) {
            InputResult result = super.workInternal();
            if (result == InputResult.SUCCESS) {
                counter = 0;
            }
        }
        syncToTrackingClients();
    }

    public void setBound(int bound, boolean refresh) {
        this.bound = MathHelper.clamp(bound, MIN_BOUND, MAX_BOUND);
        if (refresh) {
            syncToTrackingClients();
            markDirty();
        }
    }

    public void updateBound() {
        if (bound >= MAX_BOUND) {
            this.setBound(0, true);
        } else {
            this.setBound(bound + 1, true);
        }
    }

    @Override
    public EnumFacing outputSide() {
        if (cacheOutputFacing != null) {
            return cacheOutputFacing;
        } else {
            return cacheOutputFacing = super.outputSide();
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        cacheOutputFacing = null;
        return super.shouldRefresh(world, pos, oldState, newSate);
    }

    public int getBound() {
        return bound;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public void adjust(EnumFacing facing, Vec3d hitPosition) {
        updateBound();
    }

    @Nonnull
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    private void syncToTrackingClients() {
        if (!this.world.isRemote) {
            SPacketUpdateTileEntity packet = this.getUpdatePacket();
            PlayerChunkMapEntry trackingEntry = ((WorldServer)this.world).getPlayerChunkMap().getEntry(this.pos.getX() >> 4, this.pos.getZ() >> 4);
            if (trackingEntry != null) {
                for (EntityPlayerMP player : trackingEntry.getWatchingPlayers()) {
                    player.connection.sendPacket(packet);
                }
            }
        }
    }
}
