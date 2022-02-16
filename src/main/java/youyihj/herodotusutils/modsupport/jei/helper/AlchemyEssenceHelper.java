package youyihj.herodotusutils.modsupport.jei.helper;

import mezz.jei.api.ingredients.IIngredientHelper;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.alchemy.AlchemyEssenceStack;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class AlchemyEssenceHelper implements IIngredientHelper<AlchemyEssenceStack> {

    @Nullable
    @Override
    public AlchemyEssenceStack getMatch(Iterable<AlchemyEssenceStack> ingredients, AlchemyEssenceStack ingredientToMatch) {
        for (AlchemyEssenceStack ingredient : ingredients) {
            if (ingredient.equals(ingredientToMatch)) {
                return ingredient;
            }
        }
        return null;
    }

    @Override
    public String getDisplayName(AlchemyEssenceStack ingredient) {
        return ingredient.getDisplayName();
    }

    @Override
    public String getUniqueId(AlchemyEssenceStack ingredient) {
        return "alchemy essence: " + ingredient.getEssence().getIndex();
    }

    @Override
    public String getWildcardId(AlchemyEssenceStack ingredient) {
        return getUniqueId(ingredient);
    }

    @Override
    public String getModId(AlchemyEssenceStack ingredient) {
        return HerodotusUtils.MOD_ID;
    }

    @Override
    public String getResourceId(AlchemyEssenceStack ingredient) {
        return HerodotusUtils.MOD_ID;
    }

    @Override
    public AlchemyEssenceStack copyIngredient(AlchemyEssenceStack ingredient) {
        return ingredient.copy();
    }

    @Override
    public String getErrorInfo(@Nullable AlchemyEssenceStack ingredient) {
        return "Error: alchemy essence";
    }
}
