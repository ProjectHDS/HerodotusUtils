package projecthds.herodotusutils.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import projecthds.herodotusutils.HerodotusUtils;

public class ItemLithiumQuartzPowder extends Item {
    public static final ItemLithiumQuartzPowder INSTANCE = new ItemLithiumQuartzPowder();

    private ItemLithiumQuartzPowder() {
        this.setRegistryName("lithium_quartz_powder");
        this.setTranslationKey(HerodotusUtils.MOD_ID + ".lithium_quartz_powder");
        this.setCreativeTab(CreativeTabs.MISC);
    }

}
