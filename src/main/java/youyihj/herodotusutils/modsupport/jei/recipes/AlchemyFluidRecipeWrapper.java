package youyihj.herodotusutils.modsupport.jei.recipes;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import youyihj.herodotusutils.alchemy.AlchemyFluid;
import youyihj.herodotusutils.alchemy.IAlchemyExternalHatch;
import youyihj.herodotusutils.modsupport.jei.ModIngredientTypes;
import youyihj.herodotusutils.recipe.AlchemyRecipes;

import java.util.ArrayList;

/**
 * @author youyihj
 */
public class AlchemyFluidRecipeWrapper implements IRecipeWrapper {
    private final Fluid fluid;
    private final AlchemyFluid alchemyFluid;

    public AlchemyFluidRecipeWrapper(Fluid fluid) {
        this.fluid = fluid;
        this.alchemyFluid = AlchemyRecipes.normalToAlchemy(fluid);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.FLUID, new FluidStack(fluid, IAlchemyExternalHatch.FLUID_UNIT));
        ingredients.setInputs(ModIngredientTypes.ALCHEMY_ESSENCE, new ArrayList<>(alchemyFluid.getEssenceStacks()));
    }

    public Fluid getFluid() {
        return fluid;
    }

    public AlchemyFluid getAlchemyFluid() {
        return alchemyFluid;
    }
}
