package youyihj.herodotusutils.modsupport.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import hellfirepvp.modularmachinery.common.integration.crafttweaker.RecipePrimer;
import hellfirepvp.modularmachinery.common.machine.IOType;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.aspects.Aspect;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.ingredient.Impetus;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.requirement.RequirementAspectList;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.requirement.RequirementImpetus;
import youyihj.herodotusutils.modsupport.modularmachinery.tile.TileAspectListProvider;

import java.util.Map;

@ZenExpansion("mods.modularmachinery.RecipePrimer")
public class HDSUtilsPrimer {

    @ZenMethod
    public static RecipePrimer addAspectsInput(RecipePrimer primer, Map<String, Integer> aspects) {
        if (aspects.size() > TileAspectListProvider.MAX_ASPECT)
            CraftTweakerAPI.logError("Max Size was " + TileAspectListProvider.MAX_ASPECT);
        else
            aspects.forEach((k, v) -> {
                Aspect aspect = Aspect.getAspect(k);
                if (aspect != null)
                    primer.appendComponent(new RequirementAspectList(IOType.INPUT, v, aspect));
                else
                    CraftTweakerAPI.logError("Invalid aspect name : " + k);
            });
        return primer;
    }

    @ZenMethod
    public static RecipePrimer addImpetusInput(RecipePrimer primer, int amount) {
        primer.appendComponent(new RequirementImpetus(IOType.INPUT, new Impetus(amount)));
        return primer;
    }

    @ZenMethod
    public static RecipePrimer addImpetusOutput(RecipePrimer primer, int amount) {
        primer.appendComponent(new RequirementImpetus(IOType.OUTPUT, new Impetus(amount)));
        return primer;
    }
}
