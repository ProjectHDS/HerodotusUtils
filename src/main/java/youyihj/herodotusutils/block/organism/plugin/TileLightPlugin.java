package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.Condition;
import youyihj.herodotusutils.organism.ConditionManager;
import youyihj.herodotusutils.organism.ConditionType;
import youyihj.herodotusutils.organism.IConditionPlugin;

import java.util.Arrays;

/**
 * @author youyihj
 */
public class TileLightPlugin extends AbstractTileConditionPlugin {
    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new IConditionPlugin() {
            @Override
            public ConditionType getType() {
                return ConditionType.LIGHT;
            }

            @Override
            public int getBaseValue() {
                return 50;
            }

            @Override
            public Operation getOperation() {
                return Operation.ADD;
            }

            @Override
            public int getPriority() {
                return 100;
            }

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
