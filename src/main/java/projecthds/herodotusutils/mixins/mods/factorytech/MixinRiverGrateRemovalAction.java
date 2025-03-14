package projecthds.herodotusutils.mixins.mods.factorytech;

import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.item.MCItemStack;
import dalapo.factech.auxiliary.MachineRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author youyihj
 */
@Mixin(targets = "dalapo.factech.plugins.crafttweaker.RiverGrate$Remove")
public abstract class MixinRiverGrateRemovalAction {
    @Shadow(remap = false)
    private IItemStack output;

    /**
     * @author youyihj
     * @reason origin implementation is totally wrong
     */
    @Overwrite(remap = false)
    public void apply() {
        MachineRecipes.RIVER_GRATE.removeIf(recipe -> this.output.matches(new MCItemStack(recipe.getOutputStack())));
    }
}
