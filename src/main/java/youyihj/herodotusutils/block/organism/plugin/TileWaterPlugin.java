package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.*;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author youyihj
 */
public class TileWaterPlugin extends AbstractTileConditionPlugin {
    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new AbstractConditionPlugin(ConditionType.WATER, 10, 60, IConditionPlugin.Operation.MULTIPLY) {
            @Override
            public double getModifierAmount(World world, BlockPos pos, ConditionManager manager, Condition condition) {
                boolean nextToTemperature = Arrays.stream(EnumFacing.values())
                        .map(pos::offset)
                        .map(manager::getPlugin)
                        .filter(Objects::nonNull)
                        .map(IConditionPlugin::getType)
                        .anyMatch(type -> type == ConditionType.TEMPERATURE);
                return nextToTemperature ? 0.0 : 1.0;
            }
        };
    }
}
