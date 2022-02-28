package youyihj.herodotusutils.organism;

/**
 * @author youyihj
 */
public interface IInputInterface<T> {

    IngredientType<T> getIngredientType();

    boolean checkCount(T ingredient);

    void extractIngredient(T ingredient);
}
