package youyihj.herodotusutils.block.alchemy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import youyihj.herodotusutils.alchemy.AlchemyFluid;
import youyihj.herodotusutils.alchemy.IAlchemyExternalHatch;
import youyihj.herodotusutils.alchemy.IAlchemyModule;
import youyihj.herodotusutils.recipe.AlchemyRecipes;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author youyihj
 */
public class TileAlchemyInputHatch extends AbstractPipeTileEntity implements IAlchemyExternalHatch {
    private final FluidTank tank = new CustomFluidTank();

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        tank.writeToNBT(compound);
        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if ((facing == EnumFacing.UP || facing == null) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if ((facing == EnumFacing.UP || facing == null) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void work() {
        IAlchemyModule.transferFluid(this, world, pos, EnumFacing.DOWN);
    }

    @Override
    @Nullable
    public AlchemyFluid getContainedFluid() {
        return Optional.ofNullable(tank.getFluid())
                .filter(IAlchemyExternalHatch::isEnoughAmount)
                .map(FluidStack::getFluid)
                .map(AlchemyRecipes::normalToAlchemy)
                .orElse(null);
    }

    @Override
    public boolean handleInput(AlchemyFluid input, EnumFacing inputSide) {
        return false;
    }

    @Override
    public void emptyFluid() {
        tank.drain(FLUID_UNIT, true);
    }

    @Override
    public EnumFacing inputSide() {
        return null;
    }

    @Override
    public EnumFacing outputSide() {
        return EnumFacing.DOWN;
    }

    private class CustomFluidTank extends FluidTank {

        public CustomFluidTank() {
            super(FLUID_UNIT);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            FluidStack content = getFluid();
            final int contentAmount = content == null ? 0 : content.amount;
            final int inputAmount = resource.amount;
            if (!doFill) {
                return Math.min(inputAmount, contentAmount + FLUID_UNIT);
            } else {
                if (content != null && content.getFluid() != resource.getFluid()) {
                    int handleAmount = 0;
                    FluidStack drain = this.drain(inputAmount, true);
                    if (drain != null) {
                        handleAmount += drain.amount;
                    }
                    if (inputAmount > handleAmount) {
                        handleAmount += super.fill(new FluidStack(resource.getFluid(), inputAmount - handleAmount), true);
                    }
                    return handleAmount;
                }
                return super.fill(resource, true);
            }
        }

        @Override
        protected void onContentsChanged() {
            TileAlchemyInputHatch.this.markDirty();
        }
    }
}
