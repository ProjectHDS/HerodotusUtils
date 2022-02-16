package youyihj.herodotusutils.modsupport.jei;

import mezz.jei.api.recipe.IIngredientType;
import youyihj.herodotusutils.alchemy.AlchemyEssenceStack;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.ingredient.Impetus;

/**
 * @author youyihj
 */
public class ModIngredientTypes {
    public static final IIngredientType<Impetus> IMPETUS = () -> Impetus.class;
    public static final IIngredientType<AlchemyEssenceStack> ALCHEMY_ESSENCE = () -> AlchemyEssenceStack.class;
}
