package youyihj.herodotusutils.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.function.Supplier;

/**
 * @author youyihj
 */
public class ItemDropSupplier extends Lazy<ItemStack, ItemStack> {

    private ItemDropSupplier(Supplier<ItemStack> supplier) {
        super(supplier, Util.not(ItemStack::isEmpty), ItemStack::copy);
    }

    public static ItemDropSupplier of(Supplier<ItemStack> supplier) {
        return new ItemDropSupplier(supplier);
    }

    public static ItemDropSupplier ofItem(Item item) {
        return new ItemDropSupplier(() -> new ItemStack(item));
    }

    public static ItemDropSupplier ofOreDict(String oreDict) {
        return new ItemDropSupplier(() -> {
            NonNullList<ItemStack> ores = OreDictionary.getOres(oreDict);
            if (ores.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                return ores.get(0);
            }
        });
    }

    @Override
    public ItemStack get() {
        return getOptional().orElse(ItemStack.EMPTY);
    }
}
