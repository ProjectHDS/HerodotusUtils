package projecthds.herodotusutils.alchemy;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import projecthds.herodotusutils.util.Util;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author youyihj
 */
@ParametersAreNonnullByDefault
public interface IAlchemyModule extends IPipe {
    static InputResult transferFluid(IHasAlchemyFluidModule from, IHasAlchemyFluid to, EnumFacing outputSide) {
        AlchemyFluid containedFluid = from.getContainedFluid();
        if (containedFluid != null) {
            InputResult result = to.handleInput(containedFluid, outputSide.getOpposite());
            switch (result) {
                case SUCCESS:
                    from.emptyFluid();
                    break;
                case BLOCK:
                    to.setEmptyCallback(new AlchemyModuleCallback(from));
                    break;
            }
            return result;
        }
        return InputResult.NO_OPERATION;
    }

    static void transferFluid(IHasAlchemyFluidModule from, World world, BlockPos pos, EnumFacing outputSide) {
        Util.getTileEntity(world, pos.offset(outputSide), IHasAlchemyFluid.class).ifPresent(iHasAlchemyFluid -> transferFluid(from, iHasAlchemyFluid, outputSide));
    }

    void work();

    default void callBackWork() {
        work();
    }
}
