package projecthds.herodotusutils.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.Constants;

/**
 * @author youyihj
 */
public class SharedRiftAction {
    public static final DamageSource RIFT = new DamageSource("rift").setDamageBypassesArmor().setMagicDamage();

    public static boolean attackEntity(Entity entity, float amount) {
        if (entity.getClass() == EntityEndermite.class) return false;
        boolean flag = false;
        boolean attackResult = false;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            ITaint taint = living.getCapability(Capabilities.TAINT_CAPABILITY, null);
            if (taint != null && taint.moreThanScale(0.5f)) {
                amount *= 2;
                flag = true;
            }
            attackResult = living.attackEntityFrom(RIFT, amount);
            if (!flag && attackResult) {
                IAttributeInstance maxHealth = living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
                maxHealth.applyModifier(new AttributeModifier("Rift Minus", -amount, Constants.AttributeModifierOperation.ADD));
            }
        }
        return attackResult;
    }
}
