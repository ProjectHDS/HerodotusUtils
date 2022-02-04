package youyihj.herodotusutils.block;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import youyihj.herodotusutils.recipe.CreatureData;
import youyihj.herodotusutils.util.IntPair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class TileCreatureDataAnalyzer extends TileEntity implements ITickable {
    public static final Multimap<Integer, TileCreatureDataReEncodeInterface> INTERFACES_BY_CHANNEL = HashMultimap.create();
    public static final Int2IntArrayMap TYPES_BY_CHANNEL = new Int2IntArrayMap();
    public static final int UNDETERMINED_CHANNEL = -1;
    private int channel;
    private final ItemStackHandler content = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final IItemHandler onlyInsertContent = new IItemHandler() {
        @Override
        public int getSlots() {
            return content.getSlots();
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return content.getStackInSlot(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return content.insertItem(slot, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return content.getSlotLimit(slot);
        }
    };
    private int type;
    private int timer;
    private boolean recalc = true;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("channel", channel);
        compound.setTag("content", content.serializeNBT());
        compound.setInteger("time", timer);
        compound.setInteger("type", type);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        timer = compound.getInteger("time");
        channel = compound.getInteger("channel");
        type = compound.getInteger("type");
        content.deserializeNBT(compound.getCompoundTag("content"));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            IItemHandler itemHandler = facing == null ? content : onlyInsertContent;
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        recalc = true;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public int getTimer() {
        return timer;
    }

    public int getType() {
        return type;
    }

    public void toggleType(int type) {
        this.type = type;
        TYPES_BY_CHANNEL.put(channel, type);
        INTERFACES_BY_CHANNEL.get(channel).forEach(it -> it.toggleType(type));
    }

    @Override
    public void onLoad() {
        TYPES_BY_CHANNEL.put(channel, type);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        toggleType(0);
    }

    @Override
    public void update() {
        if (world.getTotalWorldTime() % 20 == 0 && !world.isRemote) {
            if (timer == 0) {
                if (recalc) {
                    recalc = false;
                    IntPair result = CreatureData.get(content.extractItem(0, 1, true));
                    if (result != null) {
                        content.extractItem(0, 1, false);
                        timer = result.getB();
                        if (type != result.getA()) {
                            toggleType(result.getA());
                        }
                    } else if (type != 0) {
                        toggleType(0);
                    }
                }
            } else {
                timer--;
            }
        }
    }
}
