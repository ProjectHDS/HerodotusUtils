package projecthds.herodotusutils.util;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.oredict.MCOreDictEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import projecthds.herodotusutils.modsupport.crafttweaker.MaterialPartOreExpansion;

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

    public static ItemDropSupplier ofItem(String id) {
        return new ItemDropSupplier(() -> new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id))));
    }

    public static ItemDropSupplier ofOreDict(String oreDict) {
        return new ItemDropSupplier(() -> {
            NonNullList<ItemStack> ores = OreDictionary.getOres(oreDict);
            if (ores.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                IItemStack itemStack = MaterialPartOreExpansion.materialPart(new MCOreDictEntry(oreDict));
                return CraftTweakerMC.getItemStack(itemStack);
            }
        });
    }

    @Override
    public ItemStack get() {
        return getOptional().orElse(ItemStack.EMPTY);
    }
}
