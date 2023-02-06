package projecthds.herodotusutils.recipe;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import net.minecraft.block.state.IBlockState;
import projecthds.herodotusutils.block.TileManaCatalyst;
import projecthds.herodotusutils.util.Util;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youyihj
 */
@ZenRegister
@ZenClass("mods.hdsutils.ManaCatalystTransform")
public class ManaCatalystTransform {
    private static final Map<IBlockState, IBlockState> transformRules = new HashMap<>();

    public static IBlockState getResult(IBlockState state) {
        return transformRules.get(state);
    }

    @ZenMethod
    public static void register(crafttweaker.api.block.IBlockState from, crafttweaker.api.block.IBlockState to) {
        transformRules.put(CraftTweakerMC.getBlockState(from), CraftTweakerMC.getBlockState(to));
    }

    @ZenMethod
    public static void summonBurst(IWorld world, IBlockPos pos) {
        Util.getTileEntity(CraftTweakerMC.getWorld(world), CraftTweakerMC.getBlockPos(pos), TileManaCatalyst.class)
                .ifPresent(TileManaCatalyst::summonBurst);
    }
}
