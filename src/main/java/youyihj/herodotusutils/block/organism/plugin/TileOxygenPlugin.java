package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.*;

/**
 * @author youyihj
 */
public class TileOxygenPlugin extends AbstractTileConditionPlugin {
    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new AbstractConditionPlugin(ConditionType.OXYGEN, 10, 100, IConditionPlugin.Operation.MULTIPLY) {
            @Override
            public double getModifierAmount(World world, BlockPos pos, ConditionManager manager, Condition condition) {
                return 1.0;
            }
        };
    }
}
