package projecthds.herodotusutils.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemCarverUpgrade extends Item {

    public static final ItemCarverUpgrade INSTANCE = new ItemCarverUpgrade();

    private ItemCarverUpgrade() {
        this.setRegistryName("carver_upgrade");
        this.setTranslationKey("hdsutils.carver_upgrade");
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    @ParametersAreNonnullByDefault
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("hdsutils.carver_update.tooltip"));
    }

}
