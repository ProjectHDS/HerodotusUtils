package projecthds.herodotusutils.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import projecthds.herodotusutils.HerodotusUtils;

public class ItemLithiumQuartz extends Item {
    public static final ItemLithiumQuartz INSTANCE = new ItemLithiumQuartz();

    private ItemLithiumQuartz() {
        this.setRegistryName("lithium_quartz");
        this.setTranslationKey(HerodotusUtils.MOD_ID + ".lithium_quartz");
        this.setCreativeTab(CreativeTabs.MISC);
    }
}
