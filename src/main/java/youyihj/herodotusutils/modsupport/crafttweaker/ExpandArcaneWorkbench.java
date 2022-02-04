package youyihj.herodotusutils.modsupport.crafttweaker;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethodStatic;
import thaumcraft.api.crafting.IArcaneRecipe;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
@ZenExpansion("mods.thaumcraft.ArcaneWorkbench")
public class ExpandArcaneWorkbench {
    @ZenMethodStatic
    public static List<ArcaneRecipe> getAll() {
        return ForgeRegistries.RECIPES.getValuesCollection().stream()
                .filter(IArcaneRecipe.class::isInstance)
                .map(IArcaneRecipe.class::cast)
                .map(ArcaneRecipe::new)
                .collect(Collectors.toList());
    }
}
