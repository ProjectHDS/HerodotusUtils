package youyihj.herodotusutils.modsupport.jei;

import mezz.jei.api.recipe.IIngredientType;
import youyihj.herodotusutils.alchemy.AlchemyEssenceStack;

/**
 * @author youyihj
 */
public class ModIngredientTypes {
    public static final IIngredientType<AlchemyEssenceStack> ALCHEMY_ESSENCE = () -> AlchemyEssenceStack.class;
}
