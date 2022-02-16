package youyihj.herodotusutils.alchemy;

import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public interface IHasAlchemyFluid extends IPipe {
    @Nullable
    AlchemyFluid getContainedFluid();

    /**
     * @param input the input stack
     * @return if the input operator is success
     */
    boolean handleInput(AlchemyFluid input, EnumFacing inputSide);

    void emptyFluid();

    EnumFacing inputSide();

    EnumFacing outputSide();
}
