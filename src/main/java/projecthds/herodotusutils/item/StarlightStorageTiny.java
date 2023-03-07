package projecthds.herodotusutils.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.client.IItemHasColor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * @author youyihj
 */
public class StarlightStorageTiny extends Item implements IItemHasColor {
    private StarlightStorageTiny() {
        this.setRegistryName("tiny_starlight_storage");
        this.setUnlocalizedName(HerodotusUtils.MOD_ID + ".tiny_starlight_storage");
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);
        this.hasSubtypes = true;
    }

    public static final String TAG_STARLIGHT = "star_light";
    public static final StarlightStorageTiny INSTANCE = new StarlightStorageTiny();
    public static final int CAPACITY = 2000;

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        ItemStack item = entityItem.getItem();
        if (item.getMetadata() != 1) {
            if (!item.hasTagCompound()) {
                item.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound nbt = item.getTagCompound();
            int starlight = nbt.getInteger(TAG_STARLIGHT);
            if (!entityItem.world.isDaytime()) {
                nbt.setInteger(TAG_STARLIGHT, ++starlight);
            }
            if (starlight > CAPACITY) {
                entityItem.setItem(new ItemStack(this, 1, 1));
            }
        }
        return false;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
            items.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getMetadata() == 1)
            return;
        tooltip.add(I18n.format("hdsutils.star_light_collect"));
        int starlight = Optional.ofNullable(stack.getTagCompound())
                .map(nbt -> nbt.getInteger(StarlightStorageTiny.TAG_STARLIGHT))
                .orElse(0);
        tooltip.add(I18n.format("hdsutils.star_light_progress", starlight, CAPACITY));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack.getMetadata() != 1) {
            return super.getUnlocalizedName(stack);
        } else {
            return super.getUnlocalizedName(stack) + "_full";
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int tintIndex) {
        if (stack.getMetadata() != 1 && tintIndex == 1) {
            int starlight = Optional.ofNullable(stack.getTagCompound())
                    .map(nbt -> nbt.getInteger(StarlightStorageTiny.TAG_STARLIGHT))
                    .orElse(0);
            double percent = starlight / (double) StarlightStorageTiny.CAPACITY;
            int red = (int) (0xff * percent);
            int green = (int) (0xff - percent * (0xff - 0x95));
            int blue = (int) ((1.0 - percent) * 0xd0);
            return red << 16 | green << 8 | blue;
        }
        return -1;
    }
}
