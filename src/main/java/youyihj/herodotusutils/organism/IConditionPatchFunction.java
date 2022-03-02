package youyihj.herodotusutils.organism;

/**
 * @author youyihj
 */
@FunctionalInterface
public interface IConditionPatchFunction {
    void apply(ConditionManager manager, Condition condition);
}
