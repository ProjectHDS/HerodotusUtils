package projecthds.herodotusutils.modsupport.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethodStatic;
import thaumcraft.api.crafting.IArcaneRecipe;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
@ZenRegister
@ModOnly("modtweaker")
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
