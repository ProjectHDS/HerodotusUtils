package projecthds.herodotusutils.modsupport.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import projecthds.herodotusutils.util.Capabilities;
import projecthds.herodotusutils.util.interfaces.ITaint;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import projecthds.herodotusutils.world.AncientVoidTeleporter;

/**
 * @author youyihj
 */
@ZenRegister
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
