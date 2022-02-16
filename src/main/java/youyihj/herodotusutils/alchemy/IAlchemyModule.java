package youyihj.herodotusutils.alchemy;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.util.Util;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author youyihj
 */
@ParametersAreNonnullByDefault
public interface IAlchemyModule extends IPipe {
    static void transferFluid(IHasAlchemyFluid from, IHasAlchemyFluid to, EnumFacing outputSide) {
        AlchemyFluid containedFluid = from.getContainedFluid();
        if (containedFluid != null && to.handleInput(containedFluid, outputSide.getOpposite())) {
            from.emptyFluid();
        }
    }

    static void transferFluid(IHasAlchemyFluid from, World world, BlockPos pos, EnumFacing outputSide) {
        Util.getTileEntity(world, pos.offset(outputSide), IHasAlchemyFluid.class).ifPresent(iHasAlchemyFluid -> transferFluid(from, iHasAlchemyFluid, outputSide));
    }

    void work();
}
