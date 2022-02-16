package youyihj.herodotusutils.recipe;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraftforge.fluids.Fluid;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import youyihj.herodotusutils.alchemy.AlchemyEssence;
import youyihj.herodotusutils.alchemy.AlchemyEssenceStack;
import youyihj.herodotusutils.alchemy.AlchemyFluid;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author youyihj
 */
@ZenClass("mods.hdsutils.Alchemy")
public class AlchemyRecipes {
    private static final BiMap<Fluid, AlchemyFluid> normalFluidToAlchemyMap = HashBiMap.create();

    @ZenMethod
    public static void setAlchemyFluid(ILiquidStack liquid, Map<Integer, Integer> essences) {
        AlchemyEssenceStack[] stacks = essences.entrySet().stream()
                .map((entry) -> new AlchemyEssenceStack(AlchemyEssence.indexOf(entry.getKey()), entry.getValue()))
                .toArray(AlchemyEssenceStack[]::new);
        normalFluidToAlchemyMap.put(CraftTweakerMC.getFluid(liquid.getDefinition()), new AlchemyFluid(stacks));
    }

    public static BiMap<Fluid, AlchemyFluid> getNormalFluidToAlchemyMap() {
        return normalFluidToAlchemyMap;
    }

    @Nullable
    public static Fluid alchemyToNormal(AlchemyFluid alchemyFluid) {
        return normalFluidToAlchemyMap.inverse().get(alchemyFluid);
    }

    @Nullable
    public static AlchemyFluid normalToAlchemy(Fluid fluid) {
        return normalFluidToAlchemyMap.get(fluid);
    }

}
