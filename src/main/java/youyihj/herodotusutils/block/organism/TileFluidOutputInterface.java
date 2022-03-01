package youyihj.herodotusutils.block.organism;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import youyihj.herodotusutils.organism.IOutputInterface;
import youyihj.herodotusutils.organism.IngredientType;
import youyihj.herodotusutils.util.Capabilities;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class TileFluidOutputInterface extends TileEntity {
    private Fluid mark;
    private final FluidTank tank = new FluidTank(8000) {
        @Override
        public int fillInternal(FluidStack resource, boolean doFill) {
            if (doFill) {
                mark = resource.getFluid();
            }
            return super.fillInternal(resource, doFill);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return mark == null || fluid.getFluid() == mark;
        }

        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    private final IOutputInterface<FluidStack> outputInterface = new IOutputInterface<FluidStack>() {
        @Override
        public IngredientType<FluidStack> getIngredientType() {
            return IngredientType.FLUID;
        }

        @Override
        public boolean checkCapacity(FluidStack ingredient) {
            return tank.fill(ingredient, false) == ingredient.amount;
        }

        @Override
        public void insertIngredient(FluidStack ingredient) {
            tank.fill(ingredient, true);
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound.getCompoundTag("fluid"));
        String markNBT = compound.getString("mark");
        mark = FluidRegistry.getFluid(markNBT);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound fluidNBT = tank.writeToNBT(new NBTTagCompound());
        compound.setTag("fluid", fluidNBT);
        if (mark != null) {
            compound.setString("mark", FluidRegistry.getFluidName(mark));
        }
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
                capability == Capabilities.OUTPUT_INTERFACE_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        } else if (capability == Capabilities.OUTPUT_INTERFACE_CAPABILITY) {
            return Capabilities.OUTPUT_INTERFACE_CAPABILITY.cast(outputInterface);
        } else {
            return super.getCapability(capability, facing);
        }
    }
}
