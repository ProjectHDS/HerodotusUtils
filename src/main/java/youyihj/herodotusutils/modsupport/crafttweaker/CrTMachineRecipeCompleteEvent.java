package youyihj.herodotusutils.modsupport.crafttweaker;

import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import youyihj.herodotusutils.modsupport.modularmachinery.event.MachineRecipeCompleteEvent;

@ZenClass("mods.hdsutils.MachineRecipeCompleteEvent")
public class CrTMachineRecipeCompleteEvent {
    public CrTMachineRecipeCompleteEvent(MachineRecipeCompleteEvent event) {
        this.event = event;
    }

    private final MachineRecipeCompleteEvent event;

    @ZenGetter("pos")
    public IBlockPos getPos() {
        return CraftTweakerMC.getIBlockPos(event.getPos());
    }

    @ZenGetter("world")
    public IWorld getWorld() {
        return CraftTweakerMC.getIWorld(event.getWorld());
    }

    @ZenGetter("recipeID")
    public String getRecipeID() {
        return event.getRecipeID().toString();
    }

    @ZenGetter("machineID")
    public String getMachineID() {
        return event.getMachineID().toString();
    }
}
