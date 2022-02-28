package youyihj.herodotusutils.block.organism;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import crafttweaker.mc1120.oredict.MCOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import youyihj.herodotusutils.organism.IInputInterface;
import youyihj.herodotusutils.organism.IngredientType;
import youyihj.herodotusutils.util.Capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class TileItemInputInterface extends TileEntity {
    private static final IOreDictEntry organism = new MCOreDictEntry("itemOrganism");
    private static final IOreDictEntry feed = new MCOreDictEntry("itemOrganismFeed");

    private final ItemStackHandler inventory = new ItemStackHandler(12) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            IItemStack matching = CraftTweakerMC.getIItemStackForMatching(stack);
            if (slot < 3) {
                return organism.matches(matching);
            } else if (slot < 6) {
                return feed.matches(matching);
            } else {
                return !organism.matches(matching) && !feed.matches(matching);
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    private final IInputInterface<IItemStack> inputInterface = new IInputInterface<IItemStack>() {
        @Override
        public IngredientType<IItemStack> getIngredientType() {
            return IngredientType.ITEM;
        }

        @Override
        public boolean checkCount(IItemStack ingredient) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack result = inventory.extractItem(i, ingredient.getAmount(), true);
                if (ingredient.matches(CraftTweakerMC.getIItemStackForMatching(result))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void extractIngredient(IItemStack ingredient) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack result = inventory.extractItem(i, ingredient.getAmount(), true);
                if (ingredient.matches(CraftTweakerMC.getIItemStackForMatching(result))) {
                    inventory.extractItem(i, ingredient.getAmount(), false);
                    return;
                }
            }
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
                capability == Capabilities.INPUT_INTERFACE_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        } else if (capability == Capabilities.INPUT_INTERFACE_CAPABILITY) {
            return Capabilities.INPUT_INTERFACE_CAPABILITY.cast(inputInterface);
        } else return super.getCapability(capability, facing);
    }
}
