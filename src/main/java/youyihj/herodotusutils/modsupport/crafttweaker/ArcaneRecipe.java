package youyihj.herodotusutils.modsupport.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.recipes.ICraftingInventory;
import crafttweaker.api.recipes.ICraftingRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import thaumcraft.api.crafting.IArcaneRecipe;

/**
 * @author youyihj
 */
@ZenClass("mods.thaumcraft.ArcaneRecipe")
public class ArcaneRecipe implements ICraftingRecipe {
    private final IArcaneRecipe recipe;
    private final boolean shaped;

    public ArcaneRecipe(IArcaneRecipe recipe) {
        this.recipe = recipe;
        this.shaped = recipe instanceof IShapedRecipe;
    }

    @Override
    public String getName() {
        return recipe.getRegistryName().getResourcePath();
    }

    @Override
    public String getFullResourceName() {
        return recipe.getRegistryName().toString();
    }

    @Override
    public String getResourceDomain() {
        return recipe.getRegistryName().getResourceDomain();
    }

    @Override
    public String toCommandString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasTransformers() {
        return false;
    }

    @Override
    public boolean hasRecipeAction() {
        return false;
    }

    @Override
    public boolean hasRecipeFunction() {
        return false;
    }

    @Override
    public boolean matches(ICraftingInventory inventory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IItemStack getCraftingResult(ICraftingInventory inventory) {
        return getOutput();
    }

    @Override
    public void applyTransformers(ICraftingInventory inventory, IPlayer byPlayer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IIngredient[] getIngredients1D() {
        return CraftTweakerMC.getIIngredients(recipe.getIngredients());
    }

    @Override
    public IIngredient[][] getIngredients2D() {
        IIngredient[] ingredients = getIngredients1D();
        if (!shaped) {
            return new IIngredient[][]{ingredients};
        }
        IShapedRecipe shapedRecipe = (IShapedRecipe) recipe;
        int heigth = shapedRecipe.getRecipeHeight();
        int width = shapedRecipe.getRecipeWidth();
        IIngredient[][] out = new IIngredient[heigth][width];

        for (int row = 0; row < heigth; row++) {
            if (width >= 0) System.arraycopy(ingredients, row * width, out[row], 0, width);
        }
        return out;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isShaped() {
        return shaped;
    }

    @Override
    public IItemStack getOutput() {
        return CraftTweakerMC.getIItemStack(recipe.getRecipeOutput());
    }

    @ZenGetter("vis")
    public int getVis() {
        return recipe.getVis();
    }
}
