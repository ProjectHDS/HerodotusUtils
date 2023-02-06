package projecthds.herodotusutils.client;

import net.minecraft.item.ItemStack;

/**
 * @author youyihj
 */
public interface IItemHasColor {
    int getColorFromItemStack(ItemStack stack, int tintIndex);
}
