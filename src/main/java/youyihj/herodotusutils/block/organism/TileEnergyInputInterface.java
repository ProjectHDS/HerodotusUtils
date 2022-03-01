package youyihj.herodotusutils.block.organism;

import com.teamacronymcoders.base.capability.energy.EnergyStorageSerializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import youyihj.herodotusutils.organism.IInputInterface;
import youyihj.herodotusutils.organism.IngredientType;
import youyihj.herodotusutils.util.Capabilities;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class TileEnergyInputInterface extends TileEntity {
    // TODO: tier based
    private static final int CAPACITY = 100_000;
    private final EnergyStorageSerializable energy = new EnergyStorageSerializable(CAPACITY, CAPACITY);
    private final IInputInterface<Integer> inputInterface = new IInputInterface<Integer>() {
        @Override
        public IngredientType<Integer> getIngredientType() {
            return IngredientType.ENERGY;
        }

        @Override
        public boolean checkCount(Integer ingredient) {
            return energy.extractEnergy(ingredient, true) == ingredient;
        }

        @Override
        public void extractIngredient(Integer ingredient) {
            energy.extractEnergy(ingredient, false);
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy.deserializeNBT(compound.getCompoundTag("energy"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbt = energy.serializeNBT();
        compound.setTag("energy", nbt);
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY ||
                capability == Capabilities.INPUT_INTERFACE_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energy);
        } else if (capability == Capabilities.INPUT_INTERFACE_CAPABILITY) {
            return Capabilities.INPUT_INTERFACE_CAPABILITY.cast(inputInterface);
        } else {
            return super.getCapability(capability, facing);
        }
    }
}
