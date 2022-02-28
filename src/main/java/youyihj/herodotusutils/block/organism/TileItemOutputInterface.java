package youyihj.herodotusutils.block.organism;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import youyihj.herodotusutils.organism.IOutputInterface;
import youyihj.herodotusutils.organism.IngredientType;
import youyihj.herodotusutils.util.Capabilities;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class TileItemOutputInterface extends TileEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(16) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final IOutputInterface<IItemStack> outputInterface = new IOutputInterface<IItemStack>() {
        @Override
        public IngredientType<IItemStack> getIngredientType() {
            return IngredientType.ITEM;
        }

        @Override
        public boolean checkCapacity(IItemStack ingredient) {
            return ItemHandlerHelper.insertItemStacked(inventory, CraftTweakerMC.getItemStack(ingredient), true).isEmpty();
        }

        @Override
        public void insertIngredient(IItemStack ingredient) {
            ItemHandlerHelper.insertItemStacked(inventory, CraftTweakerMC.getItemStack(ingredient), false);
        }
    };

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("item", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("item"));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                capability == Capabilities.OUTPUT_INTERFACE_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        } else if (capability == Capabilities.OUTPUT_INTERFACE_CAPABILITY) {
            return Capabilities.OUTPUT_INTERFACE_CAPABILITY.cast(outputInterface);
        } else return super.getCapability(capability, facing);
    }
}
