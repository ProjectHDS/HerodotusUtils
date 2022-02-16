package youyihj.herodotusutils.modsupport.jei.recipes;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.block.BlockCatalyzedAltar;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author youyihj
 */
public class TransformRuleCategory implements IRecipeCategory<TransformRuleWrapper> {
    private final String localizedTitle = Translator.translateToLocal("hdsutils.jei.transform_rule.title");
    private final IDrawable background;
    private final IDrawable icon;

    public TransformRuleCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(BlockCatalyzedAltar.ITEM_BLOCK));
        ResourceLocation backgroundTexture = HerodotusUtils.rl("textures/gui/jei/transform_rule.png");
        this.background = guiHelper.createDrawable(backgroundTexture, 0, 0, 90, 90);
    }

    @Override
    public String getUid() {
        return "transform_rule";
    }

    @Override
    public String getTitle() {
        return localizedTitle;
    }

    @Override
    public String getModName() {
        return HerodotusUtils.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setRecipe(IRecipeLayout recipeLayout, TransformRuleWrapper recipeWrapper, IIngredients ingredients) {
        List<List<AspectList>> inputs = ingredients.getInputs(AspectList.class);
        List<AspectList> result = ingredients.getOutputs(AspectList.class).get(0);
        IGuiIngredientGroup<AspectList> group = recipeLayout.getIngredientsGroup(AspectList.class);
        for (int i = 0; i < 9; i++) {
            int x = i % 3;
            int y = i / 3;
            List<AspectList> toDisplay = i == 4 ? result : inputs.get(i);
            group.init(i, i != 4, x * 36 + 1, y * 36 + 1);
            group.set(i, toDisplay);
        }
    }
}
