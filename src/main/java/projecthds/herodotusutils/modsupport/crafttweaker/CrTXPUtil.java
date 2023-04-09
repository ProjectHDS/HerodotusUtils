package projecthds.herodotusutils.modsupport.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static org.joor.Reflect.onClass;

/**
 * @author youyihj
 */
@ZenRegister
@ZenClass("mods.hdsutils.XPUtil")
@ModOnly("enderio")
@SuppressWarnings("unused")
public class CrTXPUtil {
    @ZenMethod
    public static int getPlayerXP(IPlayer player) {
        return onClass("crazypants.enderio.base.xp.XpUtil").call("getPlayerXP", CraftTweakerMC.getPlayer(player)).get();
    }

    @ZenMethod
    public static void addPlayerXP(IPlayer player, int amount) {
        onClass("crazypants.enderio.base.xp.XpUtil").call("addPlayerXP", CraftTweakerMC.getPlayer(player), amount);
    }

    @ZenMethod
    public static void removePlayerXP(IPlayer player, int amount) {
        addPlayerXP(player, -amount);
    }
}
