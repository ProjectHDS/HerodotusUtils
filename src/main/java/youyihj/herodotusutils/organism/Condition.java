package youyihj.herodotusutils.organism;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author youyihj
 */
public class Condition {
    private final Map<ConditionType, Integer> map;

    public Condition() {
        map = new IdentityHashMap<>();
        for (ConditionType type : ConditionType.getTypes()) {
            map.put(type, 0);
        }
    }

    public int getValue(ConditionType type) {
        return map.get(type);
    }

    public Map<ConditionType, Integer> getMap() {
        return map;
    }

    public void addConditionValue(ConditionType type, int value) {
        map.merge(type, value, Integer::sum);
    }

    public int[] toIntList() {
        IntList list = new IntArrayList();
        map.forEach((type, value) -> {
            list.add(type.getId());
            list.add(value);
        });
        return list.toIntArray();
    }

    public void fromIntList(int[] ints) {
        Map<Integer, ConditionType> indexTypes = ConditionType.getIndexTypes();
        for (int i = 0; i < ints.length; i += 2) {
            ConditionType type = indexTypes.get(ints[i]);
            int value = ints[i + 1];
            map.put(type, value);
        }
    }
}
