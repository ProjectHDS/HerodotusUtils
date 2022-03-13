package youyihj.herodotusutils.modsupport.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Method;

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
        try {
            Class<?> xpUtil = Class.forName("crazypants.enderio.base.xp.XpUtil");
            Method getPlayerXPMethod = xpUtil.getMethod("getPlayerXP", EntityPlayer.class);
            return ((int) getPlayerXPMethod.invoke(null, CraftTweakerMC.getPlayer(player)));
        } catch (Exception e) {
            return 0;
        }
    }

    @ZenMethod
    public static void addPlayerXP(IPlayer player, int amount) {
        try {
            Class<?> xpUtil = Class.forName("crazypants.enderio.base.xp.XpUtil");
            Method addPlayerXPMethod = xpUtil.getMethod("addPlayerXP", EntityPlayer.class, int.class);
            addPlayerXPMethod.invoke(null, CraftTweakerMC.getPlayer(player), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ZenMethod
    public static void removePlayerXP(IPlayer player, int amount) {
        addPlayerXP(player, -amount);
    }
}
