package projecthds.herodotusutils.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiConsumer;

public class TransferUtils {

    public static void forEachItems(IItemHandler handler, BiConsumer<Integer, ItemStack> consumer) {
        for (int i = 0; i < handler.getSlots(); i++) {
            consumer.accept(i, handler.getStackInSlot(i));
        }
    }

}
