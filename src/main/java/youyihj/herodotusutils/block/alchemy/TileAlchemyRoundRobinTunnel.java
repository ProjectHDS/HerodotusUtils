package youyihj.herodotusutils.block.alchemy;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import youyihj.herodotusutils.alchemy.IAdjustableTileEntity;
import youyihj.herodotusutils.alchemy.IAlchemyModule;
import youyihj.herodotusutils.alchemy.IHasAlchemyFluidModule;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author youyihj
 */
public class TileAlchemyRoundRobinTunnel extends AbstractHasAlchemyFluidTileEntity implements IHasAlchemyFluidModule, IAdjustableTileEntity {
    private final EnumFacing[] facingQuery = new EnumFacing[]{null, null, null, null};
    private byte nextIndex = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("nextIndex", nextIndex);
        byte[] temp = new byte[facingQuery.length];
        for (int i = 0; i < facingQuery.length; i++) {
            EnumFacing enumFacing = facingQuery[i];
            temp[i] = (byte) ((enumFacing == null) ? -1 : enumFacing.getHorizontalIndex());
        }
        compound.setByteArray("facingQuery", temp);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        nextIndex = compound.getByte("nextIndex");
        byte[] facingQuerySource = compound.getByteArray("facingQuery");
        for (int i = 0; i < facingQuerySource.length; i++) {
            byte b = facingQuerySource[i];
            if (b != -1) {
                facingQuery[i] = EnumFacing.byIndex(b);
            } else {
                facingQuery[i] = null;
            }
        }
    }

    @Override
    public void work() {
        if (content == null)
            return;
        EnumFacing nextOutputSide = outputSide();
        if (nextOutputSide != null) {
            IAlchemyModule.transferFluid(this, world, pos, nextOutputSide);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nonnull
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
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

    @Override
    public EnumFacing inputSide() {
        return EnumFacing.UP;
    }

    @Override
    public EnumFacing outputSide() {
        return getNextOutputSide();
    }

    public void putFacing(EnumFacing facing) {
        int firstNullIndex = -1;
        for (int i = 0; i < facingQuery.length; i++) {
            if (facingQuery[i] == null && firstNullIndex == -1) {
                firstNullIndex = i;
            }
            if (facingQuery[i] == facing) {
                facingQuery[i] = null;
                return;
            }
        }
        facingQuery[firstNullIndex] = facing;
        markDirty();
        syncToTrackingClients();
    }

    public EnumFacing getNextOutputSide() {
        if (Arrays.stream(facingQuery).allMatch(Objects::isNull))
            return null;
        do {
            if (nextIndex >= facingQuery.length)
                nextIndex = 0;
        } while (facingQuery[nextIndex++] == null);
        return facingQuery[--nextIndex];
    }

    public EnumFacing[] getFacingQuery() {
        return facingQuery.clone();
    }

    @Override
    public void afterModuleMainWork() {
        super.afterModuleMainWork();
        nextIndex++;
        if (nextIndex >= facingQuery.length)
            nextIndex = 0;
    }

    @Override
    public void adjust(EnumFacing facing, Vec3d hitPosition) {
        double hitX = hitPosition.x;
        double hitZ = hitPosition.z;
        EnumFacing toSet;
        if (facing.getAxis().getPlane() == EnumFacing.Plane.HORIZONTAL) {
            toSet = facing;
        } else {
            toSet = (hitX + hitZ < 1.0) ?
                    (hitX > hitZ) ? EnumFacing.NORTH : EnumFacing.WEST
                    :
                    (hitX > hitZ) ? EnumFacing.EAST : EnumFacing.SOUTH;
        }
        putFacing(toSet);
    }
}
