package youyihj.herodotusutils.organism;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author youyihj
 */
public class ConditionManager {
    public static final ConditionManager EMPTY = new ConditionManager();

    private final Condition condition = new Condition();
    private final List<Pair<BlockPos, IConditionPlugin>> plugins = new ArrayList<>();
    private final Map<BlockPos, IConditionPlugin> pluginPos = new HashMap<>();

    public Condition getCondition() {
        return condition;
    }

    public void addPlugin(IConditionPlugin plugin, BlockPos pos) {
        plugins.add(Pair.of(pos, plugin));
        pluginPos.put(pos, plugin);
    }

    @Nullable
    public IConditionPlugin getPlugin(BlockPos pos) {
        return pluginPos.get(pos);
    }

    public void calculate(World world) {
        sortPlugins();
        Map<ConditionType, Integer> map = condition.getMap();
        plugins.forEach(pair -> {
            BlockPos pos = pair.getLeft();
            IConditionPlugin plugin = pair.getRight();
            int baseValue = plugin.getBaseValue();
            double modifierAmount = plugin.getModifierAmount(world, pos, this, condition);
            int value;
            switch (plugin.getOperation()) {
                case ADD:
                    value = ((int) (baseValue + modifierAmount));
                    break;
                case MULTIPLY:
                    value = ((int) (baseValue * modifierAmount));
                    break;
                default:
                    value = baseValue;
            }
            map.merge(plugin.getType(), value, Integer::sum);
        });
    }

    private void sortPlugins() {
        plugins.sort(PluginEntryComparator.INSTANCE);
    }

    private enum PluginEntryComparator implements Comparator<Pair<BlockPos, IConditionPlugin>> {
        INSTANCE;

        @Override
        public int compare(Pair<BlockPos, IConditionPlugin> o1, Pair<BlockPos, IConditionPlugin> o2) {
            return Integer.compare(o2.getValue().getPriority(), o1.getValue().getPriority());
        }
    }
}
