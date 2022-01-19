package youyihj.herodotusutils.util;

import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;
import youyihj.herodotusutils.network.NetworkHandler;
import youyihj.herodotusutils.network.TaintSyncMessage;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
@ZenClass("mods.hdsutils.ITaint")
public interface ITaint {
    int ORIGIN_MAX_VALUE = 100;
    int MODIFIED_VALUE_BOUND = 20;
    int UP_MAX_VALUE_BY_MODIFYING_TAINT = 5;

    @ZenGetter("maxValue")
    int getMaxValue();

    @ZenSetter("maxValue")
    void setMaxValue(int value);

    @ZenMethod
    void addMaxValue(int value);

    @ZenGetter("infected")
    int getInfectedTaint();

    @ZenGetter("permanent")
    int getPermanentTaint();

    @ZenGetter("sticky")
    int getStickyTaint();

    @ZenSetter("infected")
    void setInfectedTaint(int value);

    @ZenSetter("permanent")
    void setPermanentTaint(int value);

    @ZenSetter("sticky")
    void setStickyTaint(int value);

    int getModifiedValue();

    void setModifiedValue(int modifiedValue);

    @ZenGetter("total")
    default int getTotalValue() {
        return getInfectedTaint() + getPermanentTaint() + getStickyTaint();
    }

    @ZenMethod
    void addPermanentTaint(int value);

    @ZenMethod
    void addStickyTaint(int value);

    @ZenMethod
    void addInfectedTaint(int value);

    void copyFrom(ITaint from);

    @ZenMethod
    void clear();

    @ZenGetter("scale")
    default float getScale() {
        return (0.0f + getTotalValue()) / getMaxValue();
    }

    @ZenMethod
    default boolean moreThanScale(float value) {
        return getScale() >= value;
    }

    void markDirty();

    void syncToClientWhenNeeded();

    class Impl implements ITaint {
        private final @Nullable
        EntityPlayer player;
        private int maxValue = ORIGIN_MAX_VALUE;
        private int infected = 0;
        private int permanent = 0;
        private int sticky = 0;
        private int modifiedValue = 0;
        private boolean dirty = false;

        public Impl(@Nullable EntityPlayer player) {
            this.player = player;
        }

        public Impl() {
            this(null);
        }

        @Override
        public int getMaxValue() {
            return maxValue;
        }

        @Override
        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
            markDirty();
        }

        @Override
        public void addMaxValue(int value) {
            double originStickyScale = ((double) sticky) / ((double) maxValue);
            maxValue += value;
            sticky *= originStickyScale;
            markDirty();
        }

        @Override
        public int getInfectedTaint() {
            return infected;
        }

        @Override
        public int getPermanentTaint() {
            return permanent;
        }

        @Override
        public int getStickyTaint() {
            return sticky;
        }

        @Override
        public void setInfectedTaint(int value) {
            infected = value;
        }

        @Override
        public void setPermanentTaint(int value) {
            permanent = value;
        }

        @Override
        public void setStickyTaint(int value) {
            sticky = value;
        }

        @Override
        public int getModifiedValue() {
            return modifiedValue;
        }

        public void setModifiedValue(int modifiedValue) {
            this.modifiedValue = modifiedValue;
        }

        @Override
        public void addPermanentTaint(int value) {
            addModifiedValue(value);
            permanent += restrictValueToMaxValue(value);
            markDirty();
        }

        @Override
        public void addStickyTaint(int value) {
            addModifiedValue(value);
            sticky += restrictValueToMaxValue(value);
            markDirty();
        }

        @Override
        public void addInfectedTaint(int value) {
            addModifiedValue(value);
            infected += restrictValueToMaxValue(value);
            markDirty();
        }

        @Override
        public void markDirty() {
            dirty = true;
        }

        @Override
        public void syncToClientWhenNeeded() {
            if (dirty) {
                sendClientSyncMessage();
                dirty = false;
            }
        }

        private void addModifiedValue(int value) {
            modifiedValue += value;
            if (modifiedValue >= MODIFIED_VALUE_BOUND || modifiedValue <= -MODIFIED_VALUE_BOUND) {
                maxValue += modifiedValue / MODIFIED_VALUE_BOUND * UP_MAX_VALUE_BY_MODIFYING_TAINT;
                if (maxValue < ORIGIN_MAX_VALUE) {
                    maxValue = ORIGIN_MAX_VALUE;
                }
                modifiedValue %= MODIFIED_VALUE_BOUND;
            }
        }

        public void clear() {
            this.maxValue = ORIGIN_MAX_VALUE;
            this.infected = 0;
            this.permanent = 0;
            this.sticky = 0;
            this.modifiedValue = 0;
        }

        @Override
        public void copyFrom(ITaint from) {
            this.maxValue = from.getMaxValue();
            this.infected = from.getInfectedTaint();
            this.sticky = from.getStickyTaint();
            this.permanent = from.getPermanentTaint();
            this.modifiedValue = from.getModifiedValue();
        }

        private int restrictValueToMaxValue(int value) {
            if (value + getTotalValue() >= getMaxValue()) {
                return getMaxValue() - getTotalValue();
            } else return value;
        }

        private void sendClientSyncMessage() {
            if (player != null && !player.world.isRemote) {
                NetworkHandler.INSTANCE.sendMessageToPlayer(new TaintSyncMessage().setTaint(this), player);
            }
        }
    }
}
