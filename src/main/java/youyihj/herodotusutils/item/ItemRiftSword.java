package youyihj.herodotusutils.item;

import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import youyihj.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
public class ItemRiftSword extends ItemSword {
    private static final ToolMaterial VOID_METAL = EnumHelper.addToolMaterial("VOID_METAL", 3, 512, 3.0f, 4.5f, 18);
    public static final ItemRiftSword INSTANCE = new ItemRiftSword();

    public ItemRiftSword() {
        super(VOID_METAL);
        this.setRegistryName("rift_sword");
        this.setUnlocalizedName(HerodotusUtils.MOD_ID + ".rift_sword");
        this.setFull3D();
    }
}
