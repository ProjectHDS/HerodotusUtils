package projecthds.herodotusutils.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public interface IBlockHasColor {
    int getColorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex);
}
