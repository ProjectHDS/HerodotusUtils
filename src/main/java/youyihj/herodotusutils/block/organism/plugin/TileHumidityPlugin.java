package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.Condition;
import youyihj.herodotusutils.organism.ConditionManager;
import youyihj.herodotusutils.organism.ConditionType;
import youyihj.herodotusutils.organism.IConditionPlugin;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author youyihj
 */
public class TileHumidityPlugin extends AbstractTileConditionPlugin {
    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new IConditionPlugin() {
            @Override
            public ConditionType getType() {
                return ConditionType.HUMIDITY;
            }

            @Override
            public int getBaseValue() {
                return 1;
            }

            @Override
            public Operation getOperation() {
                return Operation.MULTIPLY;
            }

            @Override
            public int getPriority() {
                return 0;
            }

            @Override
            public double getModifierAmount(World world, BlockPos pos, ConditionManager manager, Condition condition) {
                long count = Arrays.stream(EnumFacing.values())
                        .map(pos::offset)
                        .map(manager::getPlugin)
                        .filter(Objects::nonNull)
                        .map(IConditionPlugin::getType)
                        .filter(type -> type == ConditionType.TEMPERATURE)
                        .count();
                double pressurePercent = ((double) manager.getPlugins(ConditionType.PRESSURE).size()) / manager.getPluginCount();
                return count > MathHelper.floor(pressurePercent * 20) ? 10.0 : 1.0;
            }
        };
    }
}
