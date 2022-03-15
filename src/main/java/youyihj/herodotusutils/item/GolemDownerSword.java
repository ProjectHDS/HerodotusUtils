package youyihj.herodotusutils.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.entity.golem.IGolem;

/**
 * @author youyihj
 */
public class GolemDownerSword extends Item {
    public static final GolemDownerSword INSTANCE = new GolemDownerSword();

    private GolemDownerSword() {
        this.setRegistryName("golem_downer_sword");
        this.setUnlocalizedName(HerodotusUtils.MOD_ID + ".golem_downer_sword");
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
            if (golem.getLevel() > 1) {
                BlockPos pos = target.getPosition();
                world.removeEntity(target);
                for (int i = 0; i < 2; i++) {
                    BlockPos pos1 = pos.north(MathHelper.getInt(world.rand, -2, 2)).east(MathHelper.getInt(world.rand, -2, 2));
                    IGolem newGolem = golem.copy();
                    newGolem.setLevel(golem.getLevel() - 1);
                    EntityLivingBase entity = newGolem.getEntity();
                    entity.setHealth(target.getHealth());
                    entity.setPosition(pos1.getX(), pos1.getY(), pos1.getZ());
                    world.spawnEntity(entity);
                }
            } else {
                target.onKillCommand();
            }
            stack.damageItem(1, attacker);
        }
        return true;
    }
}
