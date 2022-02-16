package youyihj.herodotusutils.modsupport.jei.recipes;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.alchemy.AlchemyEssenceStack;
import youyihj.herodotusutils.alchemy.IAlchemyExternalHatch;
import youyihj.herodotusutils.block.alchemy.BlockAlchemyController;
import youyihj.herodotusutils.modsupport.jei.ModIngredientTypes;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class AlchemyFluidRecipeCategory implements IRecipeCategory<AlchemyFluidRecipeWrapper> {
    private final IDrawable icon;
    private final IDrawable background;
    private final int width = 100;
    private final int height = 100;

    public AlchemyFluidRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(BlockAlchemyController.ITEM_BLOCK));
        this.background = guiHelper.createBlankDrawable(width, height);
    }

    @Override
    public String getUid() {
        return "alchemy_fluid";
    }

    @Override
    public String getTitle() {
        return I18n.translateToLocal("hdsutils.jei.alchemy_fluid.title");
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public String getModName() {
        return HerodotusUtils.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlchemyFluidRecipeWrapper recipeWrapper, IIngredients ingredients) {
        AlchemyEssenceStack[] essences = recipeWrapper.getAlchemyFluid().getEssenceStacks().toArray(new AlchemyEssenceStack[0]);
        IGuiFluidStackGroup fluidGroup = recipeLayout.getFluidStacks();
        IGuiIngredientGroup<AlchemyEssenceStack> essenceGroup = recipeLayout.getIngredientsGroup(ModIngredientTypes.ALCHEMY_ESSENCE);
        float f = (float) (Math.PI * 2 / essences.length);
        float radian = 0.0f;
        for (int i = 0; i < essences.length; i++) {
            int essenceRoundRadius = 40;
            int x = width / 2 + Math.round(MathHelper.sin(radian) * essenceRoundRadius) - 8;
            int y = height / 2 - Math.round(MathHelper.cos(radian) * essenceRoundRadius) - 8;
            essenceGroup.init(i, true, x, y);
            essenceGroup.set(i, essences[i]);
            radian += f;
        }
        fluidGroup.init(0, false, width / 2 - 8, height / 2 - 8);
        fluidGroup.set(0, new FluidStack(recipeWrapper.getFluid(), IAlchemyExternalHatch.FLUID_UNIT));
    }
}
