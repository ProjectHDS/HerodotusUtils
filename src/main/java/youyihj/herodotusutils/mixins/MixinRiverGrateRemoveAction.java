package youyihj.herodotusutils.mixins;

import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.item.MCItemStack;
import dalapo.factech.auxiliary.MachineRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author youyihj
 */
@Mixin(targets = "dalapo.factech.plugins.crafttweaker.RiverGrate$Remove", remap = false)
public abstract class MixinRiverGrateRemoveAction {
    @Shadow
    private IItemStack output;

    /**
     * @author youyihj
     * @reason origin implementation is totally wrong
     */
    @Overwrite
    public void apply() {
        MachineRecipes.RIVER_GRATE.removeIf(recipe -> this.output.matches(new MCItemStack(recipe.getOutputStack())));
    }
}
