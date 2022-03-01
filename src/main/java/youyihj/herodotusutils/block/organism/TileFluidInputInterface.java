package youyihj.herodotusutils.block.organism;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import youyihj.herodotusutils.organism.IInputInterface;
import youyihj.herodotusutils.organism.IngredientType;
import youyihj.herodotusutils.util.Capabilities;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class TileFluidInputInterface extends TileEntity {
    private final FluidTank tank = new FluidTank(8000) {
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };
    private final IInputInterface<FluidStack> inputInterface = new IInputInterface<FluidStack>() {
        @Override
        public IngredientType<FluidStack> getIngredientType() {
            return IngredientType.FLUID;
        }

        @Override
        public boolean checkCount(FluidStack ingredient) {
            return ingredient.isFluidStackIdentical(tank.drain(ingredient, false));
        }

        @Override
        public void extractIngredient(FluidStack ingredient) {
            tank.drain(ingredient, true);
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound.getCompoundTag("fluid"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound fluidNBT = tank.writeToNBT(new NBTTagCompound());
        compound.setTag("fluid", fluidNBT);
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
                capability == Capabilities.INPUT_INTERFACE_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        } else if (capability == Capabilities.INPUT_INTERFACE_CAPABILITY) {
            return Capabilities.INPUT_INTERFACE_CAPABILITY.cast(inputInterface);
        } else {
            return super.getCapability(capability, facing);
        }
    }
}
