package projecthds.herodotusutils.mixins.mods.thaumcraft;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.tiles.crafting.TileCrucible;

/**
 * @author youyihj
 */
@Mixin(TileCrucible.class)
public class MixinTileCrucible {
    /**
     * @author youyihj
     * @reason to disable crucible smelt
     */
    @Overwrite(remap = false)
    public ItemStack attemptSmelt(ItemStack item, String username) {
        return item;
    }
}
