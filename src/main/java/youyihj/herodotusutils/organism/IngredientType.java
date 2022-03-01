package youyihj.herodotusutils.organism;

import crafttweaker.api.item.IItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author youyihj
 */
public class IngredientType<T> {

    public static final IngredientType<IItemStack> ITEM = new IngredientType<>(IItemStack.class, (item, modifier) -> item.withAmount((int) (item.getAmount() * modifier)));
    public static final IngredientType<FluidStack> FLUID = new IngredientType<>(FluidStack.class, (fluid, modifier) -> {
        FluidStack copy = fluid.copy();
        copy.amount = (int) (fluid.amount * modifier);
        return copy;
    });
    public static final IngredientType<Integer> ENERGY = new IngredientType<>(int.class, (a, b) -> ((int) (a * b)));
    public static final IngredientType<Integer> IMPETUS = new IngredientType<>(int.class, (a, b) -> ((int) (a * b)));

    private final Class<T> ingredientClass;
    private final ModifierApplier<T> modifierApplier;

    public IngredientType(Class<T> ingredientClass, ModifierApplier<T> modifierApplier) {
        this.ingredientClass = ingredientClass;
        this.modifierApplier = modifierApplier;
    }

    public Class<T> getIngredientClass() {
        return ingredientClass;
    }

    public ModifierApplier<T> getModifierApplier() {
        return modifierApplier;
    }

    @FunctionalInterface
    public interface ModifierApplier<T> {
        T apply(T origin, double modifier);
    }
}
