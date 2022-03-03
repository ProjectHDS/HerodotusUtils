package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.*;

/**
 * @author youyihj
 */
public class TilePressurePlugin extends AbstractTileConditionPlugin {
    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new AbstractConditionPlugin(ConditionType.PRESSURE, 5, 600, IConditionPlugin.Operation.MULTIPLY) {
            @Override
            public double getModifierAmount(World world, BlockPos pos, ConditionManager manager, Condition condition) {
                return 1.0;
            }
        };
    }
}
