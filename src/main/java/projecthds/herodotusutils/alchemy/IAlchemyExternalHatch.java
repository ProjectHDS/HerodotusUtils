package projecthds.herodotusutils.alchemy;

import net.minecraftforge.fluids.FluidStack;

/**
 * @author youyihj
 */
public interface IAlchemyExternalHatch extends IHasAlchemyFluidModule {
    int FLUID_UNIT = 1000;

    static boolean isEnoughAmount(FluidStack fluidStack) {
        return fluidStack.amount >= FLUID_UNIT;
    }
}
