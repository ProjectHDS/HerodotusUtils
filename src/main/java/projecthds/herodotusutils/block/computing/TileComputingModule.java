package projecthds.herodotusutils.block.computing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import projecthds.herodotusutils.recipe.ClipManager;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author youyihj
 */
public class TileComputingModule extends TileEntity implements ITickable {
    private ClipManager.ClipInfo clipInfo;
    private boolean needRecalc = true;
    private static final String TAG_CLIP_DURATION = "clip_duration";
    private int clientPower;
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            TileComputingModule.this.markDirty();
            TileComputingModule.this.needRecalc = true;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.itemStackHandler.deserializeNBT(compound.getCompoundTag("item"));
        super.readFromNBT(compound);
        calcClipInfo();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("item", this.itemStackHandler.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }

    public int getGeneratePower() {
        if (world.isRemote) {
            return clientPower;
        }
        return clipInfo == null ? 0 : clipInfo.getPower();
    }

    private void calcClipInfo() {
        ItemStack stack = itemStackHandler.getStackInSlot(0);
        this.clipInfo = ClipManager.clipInfos.stream()
                .filter(info -> info.getClip().isItemEqual(stack))
                .findFirst()
                .orElse(null);
        if (world != null) {
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), Constants.BlockFlags.SEND_TO_CLIENTS);
        }
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("power", getGeneratePower());
        return new SPacketUpdateTileEntity(this.pos, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.clientPower = pkt.getNbtCompound().getInteger("power");
    }

    @Override
    public void update() {
        if (needRecalc) {
            needRecalc = false;
            calcClipInfo();
        }
        if (clipInfo != null) {
            ItemStack stack = itemStackHandler.getStackInSlot(0);
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound nbt = Objects.requireNonNull(stack.getTagCompound());
            if (!nbt.hasKey(TAG_CLIP_DURATION)) {
                nbt.setInteger(TAG_CLIP_DURATION, 0);
            }
            int stackDuration = nbt.getInteger(TAG_CLIP_DURATION);
            nbt.setInteger(TAG_CLIP_DURATION, ++stackDuration);
            if (stackDuration >= clipInfo.getDuration()) {
                itemStackHandler.setStackInSlot(0, clipInfo.getBreakingResult());
            }
        }
    }
}
