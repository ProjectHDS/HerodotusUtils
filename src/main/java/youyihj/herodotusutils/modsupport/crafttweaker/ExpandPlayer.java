package youyihj.herodotusutils.modsupport.crafttweaker;

import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import youyihj.herodotusutils.util.Capabilities;
import youyihj.herodotusutils.util.ITaint;
import youyihj.herodotusutils.world.AncientVoidTeleporter;

/**
 * @author youyihj
 */
@ZenExpansion("crafttweaker.player.IPlayer")
public class ExpandPlayer {
    @ZenGetter("taint")
    public static ITaint getTaint(IPlayer player) {
        return CraftTweakerMC.getPlayer(player).getCapability(Capabilities.TAINT_CAPABILITY, null);
    }

    @ZenMethod
    public static void teleportToRift(IPlayer player) {
        AncientVoidTeleporter.teleport(CraftTweakerMC.getPlayer(player), CraftTweakerMC.getBlockPos(player.getPosition()));
    }
}
