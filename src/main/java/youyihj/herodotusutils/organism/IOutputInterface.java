package youyihj.herodotusutils.organism;

/**
 * @author youyihj
 */
public interface IOutputInterface<T> {

    IngredientType<T> getIngredientType();

    boolean checkCapacity(T ingredient);

    void insertIngredient(T ingredient);
}
