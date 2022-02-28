package youyihj.herodotusutils.organism;

import crafttweaker.api.item.IItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author youyihj
 */
public class IngredientType<T> {

    public static final IngredientType<IItemStack> ITEM = new IngredientType<>(IItemStack.class);
    public static final IngredientType<FluidStack> FLUID = new IngredientType<>(FluidStack.class);
    public static final IngredientType<Integer> ENERGY = new IngredientType<>(int.class);
    public static final IngredientType<Integer> IMPETUS = new IngredientType<>(int.class);

    private final Class<T> ingredientClass;

    public IngredientType(Class<T> ingredientClass) {
        this.ingredientClass = ingredientClass;
    }

    public Class<T> getIngredientClass() {
        return ingredientClass;
    }
}
