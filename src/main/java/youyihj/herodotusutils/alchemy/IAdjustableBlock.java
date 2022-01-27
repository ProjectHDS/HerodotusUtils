package youyihj.herodotusutils.alchemy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * @author youyihj
 */
public interface IAdjustableBlock {
    IBlockState getAdjustedResult(IBlockState previous);

    default ITextComponent getAdjustedMessage(IBlockState state) {
        return new TextComponentString("");
    }
}
