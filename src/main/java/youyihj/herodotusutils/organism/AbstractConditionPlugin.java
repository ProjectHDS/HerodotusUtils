package youyihj.herodotusutils.organism;

/**
 * @author youyihj
 */
public abstract class AbstractConditionPlugin implements IConditionPlugin {
    private final ConditionType type;
    private final int baseValue;
    private final Operation operation;
    private final int priority;

    public AbstractConditionPlugin(ConditionType type, int baseValue, int priority, Operation operation) {
        this.type = type;
        this.baseValue = baseValue;
        this.operation = operation;
        this.priority = priority;
    }

    @Override
    public ConditionType getType() {
        return type;
    }

    @Override
    public int getBaseValue() {
        return baseValue;
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
