package youyihj.herodotusutils.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class TaintCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {
    private final ITaint taint;

    public TaintCapabilityProvider(EntityPlayer player) {
        this.taint = new ITaint.Impl(player);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == Capabilities.TAINT_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == Capabilities.TAINT_CAPABILITY) {
            return Capabilities.TAINT_CAPABILITY.cast(taint);
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("max", taint.getMaxValue());
        nbt.setInteger("infected", taint.getInfectedTaint());
        nbt.setInteger("permanent", taint.getPermanentTaint());
        nbt.setInteger("sticky", taint.getStickyTaint());
        nbt.setInteger("modified", taint.getModifiedValue());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        taint.setInfectedTaint(nbt.getInteger("infected"));
        taint.setPermanentTaint(nbt.getInteger("permanent"));
        taint.setStickyTaint(nbt.getInteger("sticky"));
        taint.setModifiedValue(nbt.getInteger("modified"));
        taint.setMaxValue(nbt.getInteger("max"));
    }
}
