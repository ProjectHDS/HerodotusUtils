package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.Condition;
import youyihj.herodotusutils.organism.ConditionManager;
import youyihj.herodotusutils.organism.ConditionType;
import youyihj.herodotusutils.organism.IConditionPlugin;

/**
 * @author youyihj
 */
public class TilePressurePlugin extends AbstractTileConditionPlugin {
    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new IConditionPlugin() {
            @Override
            public ConditionType getType() {
                return ConditionType.PRESSURE;
            }

            @Override
            public int getBaseValue() {
                return 5;
            }

            @Override
            public Operation getOperation() {
                return Operation.MULTIPLY;
            }

            @Override
            public int getPriority() {
                return 600;
            }

            @Override
            public double getModifierAmount(World world, BlockPos pos, ConditionManager manager, Condition condition) {
                return 1.0;
            }
        };
    }
}
