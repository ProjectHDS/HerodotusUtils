package youyihj.herodotusutils.mixins.mods.thaumcraft;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.tiles.crafting.TileCrucible;

/**
 * @author youyihj
 */
@Mixin(value = TileCrucible.class, remap = false)
public class MixinTileCrucible {
    /**
     * @author youyihj
     * @reason to disable crucible smelt
     */
    @Overwrite
    public ItemStack attemptSmelt(ItemStack item, String username) {
        return item;
    }
}
