package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.*;

import java.util.Arrays;

/**
 * @author youyihj
 */
public class TileLightPlugin extends AbstractTileConditionPlugin {
    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new AbstractConditionPlugin(ConditionType.LIGHT, 50, 100, IConditionPlugin.Operation.ADD) {
            @Override
            public double getModifierAmount(World world, BlockPos pos, ConditionManager manager, Condition condition) {
                long opaqueCubes = Arrays.stream(EnumFacing.values())
                        .map(pos::offset)
                        .map(world::getBlockState)
                        .filter(IBlockState::isOpaqueCube)
                        .count();
                return opaqueCubes * -5.0;
            }
        };
    }
}
