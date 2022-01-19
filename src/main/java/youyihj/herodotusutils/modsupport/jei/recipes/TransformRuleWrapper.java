package youyihj.herodotusutils.modsupport.jei.recipes;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import youyihj.herodotusutils.block.BlockCatalyzedAltar;

import java.util.Arrays;

/**
 * @author youyihj
 */
public class TransformRuleWrapper implements IRecipeWrapper {
    private final AspectList result;
    private final AspectList[] in = new AspectList[9];

    public TransformRuleWrapper(BlockCatalyzedAltar.TransformRule transformRule) {
        this.result = new AspectList().add(transformRule.getResult(), 1);
        for (Pair<BlockPos, Aspect> pair : transformRule.getIn()) {
            BlockPos pos = pair.getLeft();
            Aspect aspect = pair.getRight();
            this.in[(pos.getZ() + 1) * 3 + pos.getX() + 1] = new AspectList().add(aspect, 1);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(AspectList.class, Arrays.asList(in));
        ingredients.setOutput(AspectList.class, result);
    }
}
