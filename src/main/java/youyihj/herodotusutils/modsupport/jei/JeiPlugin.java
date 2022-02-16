package youyihj.herodotusutils.modsupport.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import youyihj.herodotusutils.alchemy.AlchemyEssence;
import youyihj.herodotusutils.alchemy.AlchemyEssenceStack;
import youyihj.herodotusutils.block.alchemy.BlockAlchemyController;
import youyihj.herodotusutils.block.BlockCatalyzedAltar;
import youyihj.herodotusutils.modsupport.jei.helper.AlchemyEssenceHelper;
import youyihj.herodotusutils.modsupport.jei.helper.ImpetusHelper;
import youyihj.herodotusutils.modsupport.jei.recipes.AlchemyFluidRecipeCategory;
import youyihj.herodotusutils.modsupport.jei.recipes.AlchemyFluidRecipeWrapper;
import youyihj.herodotusutils.modsupport.jei.recipes.TransformRuleCategory;
import youyihj.herodotusutils.modsupport.jei.recipes.TransformRuleWrapper;
import youyihj.herodotusutils.modsupport.jei.render.AlchemyEssenceRender;
import youyihj.herodotusutils.modsupport.jei.render.ImpetusRender;
import youyihj.herodotusutils.recipe.AlchemyRecipes;

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
        registry.addRecipeCatalyst(new ItemStack(BlockAlchemyController.ITEM_BLOCK), "alchemy_fluid");
        List<AlchemyFluidRecipeWrapper> wrappers = AlchemyRecipes.getNormalFluidToAlchemyMap().keySet().stream()
                .map(AlchemyFluidRecipeWrapper::new)
                .collect(Collectors.toList());
        registry.addRecipes(wrappers, "alchemy_fluid");
        registry.addRecipeCatalyst(new ItemStack(BlockCatalyzedAltar.ITEM_BLOCK), "transform_rule");
        List<TransformRuleWrapper> transformRuleWrappers = BlockCatalyzedAltar.TRANSFORM_RULES.values().stream().map(TransformRuleWrapper::new).collect(Collectors.toList());
        registry.addRecipes(transformRuleWrappers, "transform_rule");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new AlchemyFluidRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new TransformRuleCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
        registry.register(ModIngredientTypes.IMPETUS, Collections.emptyList(), new ImpetusHelper(), new ImpetusRender());
        List<AlchemyEssenceStack> essenceStacks = AlchemyEssence.getUsedEssences().stream().map(it -> new AlchemyEssenceStack(it, 1)).collect(Collectors.toList());
        registry.register(ModIngredientTypes.ALCHEMY_ESSENCE, essenceStacks, new AlchemyEssenceHelper(), new AlchemyEssenceRender());
    }
}
