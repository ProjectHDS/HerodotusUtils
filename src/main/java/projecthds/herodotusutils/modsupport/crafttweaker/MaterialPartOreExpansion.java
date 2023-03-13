package projecthds.herodotusutils.modsupport.crafttweaker;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import java.util.List;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * @author youyihj
 */
public class MaterialPartOreExpansion {
    @ZenGetter
    @ZenMethod
    public static IItemStack materialPart(IOreDictEntry oreDictEntry) {
        List<IItemStack> items = oreDictEntry.getItems();
        if (items.isEmpty()) {
            throw new NullPointerException(oreDictEntry + " is empty!");
        }
        for (IItemStack item : items) {
            if (item.getDefinition().getOwner().equals("contenttweaker")) {
                return item;
            }
        }
        return items.get(0);
    }
}
