package projecthds.herodotusutils.alchemy;

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
     * @return the result of the input operation
     */
    InputResult handleInput(AlchemyFluid input, EnumFacing inputSide);

    void emptyFluid();

    EnumFacing inputSide();

    EnumFacing outputSide();

    void setEmptyCallback(AlchemyModuleCallback callback);
}
