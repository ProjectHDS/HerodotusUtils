package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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
public class TileWaterPlugin extends AbstractTileConditionPlugin {
    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new IConditionPlugin() {
            @Override
            public ConditionType getType() {
                return ConditionType.WATER;
            }

            @Override
            public int getBaseValue() {
                return 10;
            }

            @Override
            public Operation getOperation() {
                return Operation.MULTIPLY;
            }

            @Override
            public int getPriority() {
                return 60;
            }

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
