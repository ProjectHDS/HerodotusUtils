package youyihj.herodotusutils.organism;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author youyihj
 */
public class ConditionType {

    private static final List<ConditionType> TYPES = new ArrayList<>();

    public static List<ConditionType> getTypes() {
        return Collections.unmodifiableList(TYPES);
    }

    public static Map<Integer, ConditionType> getIndexTypes() {
        ImmutableMap.Builder<Integer, ConditionType> builder = ImmutableMap.builder();
        for (ConditionType type : TYPES) {
            builder.put(type.id, type);
        }
        return builder.build();
    }

    public static final ConditionType TEMPERATURE = new ConditionType(0, "temperature");
    public static final ConditionType LIGHT = new ConditionType(1, "light");
    public static final ConditionType HUMIDITY = new ConditionType(2, "humidity");
    public static final ConditionType PRESSURE = new ConditionType(3, "pressure");
    public static final ConditionType OXYGEN = new ConditionType(4, "oxygen");
    public static final ConditionType WATER = new ConditionType(5, "water");

    private final String name;
    private final int id;

    public ConditionType(int id, String name) {
        this.name = name;
        this.id = id;
        TYPES.add(this);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getLocalizationKey() {
        return "hdsutils.organism.condition." + getName();
    }
}
