package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author youyihj
 */
public class TileTemperaturePlugin extends AbstractTileConditionPlugin implements ITickable {

    private Group group = new Group();
    private boolean firstTick = true;

    @Override
    protected IConditionPlugin createConditionPlugin() {
        return new AbstractConditionPlugin(ConditionType.TEMPERATURE, 10, 300, IConditionPlugin.Operation.MULTIPLY) {
            @Override
            public double getModifierAmount(World world, BlockPos pos, ConditionManager manager, Condition condition) {
                double value = 0.9 + 0.1 * group.poses.size();
                int oxygen = condition.getValue(ConditionType.OXYGEN);
                boolean nextToHumidity = Arrays.stream(EnumFacing.values())
                        .map(pos::offset)
                        .map(manager::getPlugin)
                        .filter(Objects::nonNull)
                        .map(IConditionPlugin::getType)
                        .anyMatch(it -> it == ConditionType.HUMIDITY);
                if (nextToHumidity) value /= 2;
                if (oxygen >= 100 && value >= 3.0) {
                    return 0.0;
                }
                if (oxygen >= 200 && value >= 2.0) {
                    return 0.0;
                }
                if (oxygen >= 300 && value >= 1.1) {
                    return 0.0;
                }
                return value;
            }
        };
    }

    @Override
    public void invalidate() {
        super.invalidate();
        group.poses.remove(pos);
    }

    @Override
    public void update() {
        if (firstTick) { // when all tile entities are ready
            if (group.poses.isEmpty()) {
                group.poses.add(pos);
                for (EnumFacing facing : EnumFacing.values()) {
                    scanGroup(pos.offset(facing));
                }
            }
            firstTick = false;
        }
    }

    private void scanGroup(BlockPos position) {
        boolean success = addToGroup(position);
        if (!success)
            return;
        for (EnumFacing enumFacing : EnumFacing.values()) {
            BlockPos offset = position.offset(enumFacing);
            scanGroup(offset);
        }
    }

    private boolean addToGroup(BlockPos position) {
        TileEntity tileEntity = world.getTileEntity(position);
        if (tileEntity instanceof TileTemperaturePlugin) {
            TileTemperaturePlugin plugin = (TileTemperaturePlugin) tileEntity;
            if (plugin.group != group) {
                group.poses.add(position);
                plugin.group = group;
                return true;
            }
        }
        return false;
    }

    private static class Group {
        private final Set<BlockPos> poses = new HashSet<>();
    }
}
