package youyihj.herodotusutils.organism;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author youyihj
 */
public interface IConditionPlugin {
    ConditionType getType();

    int getBaseValue();

    Operation getOperation();

    int getPriority();

    double getModifierAmount(World world, BlockPos pos, ConditionManager manager, Condition condition);

    enum Operation {
        ADD,
        MULTIPLY
    }
}
