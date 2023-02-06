package projecthds.herodotusutils.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import projecthds.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
public class ItemRiftFeed extends Item {
    private ItemRiftFeed() {
        this.setRegistryName("rift_feed");
        this.setTranslationKey(HerodotusUtils.MOD_ID + ".rift_feed");
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public static final ItemRiftFeed INSTANCE = new ItemRiftFeed();
}
