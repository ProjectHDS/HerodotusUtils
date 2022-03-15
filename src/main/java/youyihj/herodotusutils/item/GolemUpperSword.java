package youyihj.herodotusutils.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.entity.golem.IGolem;

/**
 * @author youyihj
 */
public class GolemUpperSword extends Item {
    public static final GolemUpperSword INSTANCE = new GolemUpperSword();

    private GolemUpperSword() {
        this.setRegistryName("golem_upper_sword");
        this.setUnlocalizedName(HerodotusUtils.MOD_ID + ".golem_upper_sword");
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setMaxDamage(10);
        this.setFull3D();
        this.maxStackSize = 1;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        World world = target.world;
        if (!world.isRemote && target instanceof IGolem && target.getMaxHealth() <= 20.2f) {
            IGolem golem = (IGolem) target;
            if (golem.getLevel() < 3) {
                IGolem newGolem = golem.copy();
                newGolem.getEntity().setHealth(target.getHealth());
                newGolem.setLevel(golem.getLevel() + 1);
                world.removeEntity(target);
                world.spawnEntity(newGolem.getEntity());
            } else {
                target.onKillCommand();
            }
            stack.damageItem(1, attacker);
        }
        return true;
    }
}
