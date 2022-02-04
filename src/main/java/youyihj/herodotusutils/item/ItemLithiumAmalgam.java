package youyihj.herodotusutils.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.potion.LithiumAmalgamInfected;

/**
 * @author youyihj
 */
public class ItemLithiumAmalgam extends Item {
    private ItemLithiumAmalgam() {
        this.setRegistryName("lithium_amalgam");
        this.setUnlocalizedName(HerodotusUtils.MOD_ID + ".lithium_amalgam");
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public static final ItemLithiumAmalgam INSTANCE = new ItemLithiumAmalgam();

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (playerIn.world.isRemote) {
            return false;
        }
        if (target instanceof EntityAnimal) {
            target.addPotionEffect(new PotionEffect(LithiumAmalgamInfected.INSTANCE, 2400, 1));
            stack.shrink(1);
            return true;
        }
        return false;
    }
}
