package youyihj.herodotusutils.modsupport.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import youyihj.herodotusutils.block.BlockCatalyzedAltar;
import youyihj.herodotusutils.modsupport.jei.helper.ImpetusHelper;
import youyihj.herodotusutils.modsupport.jei.recipes.TransformRuleCategory;
import youyihj.herodotusutils.modsupport.jei.recipes.TransformRuleWrapper;
import youyihj.herodotusutils.modsupport.jei.render.ImpetusRender;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.ingredient.Impetus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
@JEIPlugin
public class JeiPlugin implements IModPlugin {
    public static IJeiHelpers JEI_HELPER;

    @Override
    public void register(IModRegistry registry) {
        JEI_HELPER = registry.getJeiHelpers();
        registry.addRecipeCatalyst(new ItemStack(BlockCatalyzedAltar.ITEM_BLOCK), "transform_rule");
        List<TransformRuleWrapper> transformRuleWrappers = BlockCatalyzedAltar.TRANSFORM_RULES.values().stream().map(TransformRuleWrapper::new).collect(Collectors.toList());
        registry.addRecipes(transformRuleWrappers, "transform_rule");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
        registry.register(() -> Impetus.class, Collections.emptyList(), new ImpetusHelper(), new ImpetusRender());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new TransformRuleCategory(registry.getJeiHelpers().getGuiHelper()));
    }
}
