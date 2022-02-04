package youyihj.herodotusutils.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author ikexing
 */
public class Starvation extends Potion {

    public static final Starvation INSTANCE = new Starvation();

    protected Starvation() {
        super(true, 5797459);
        this.setRegistryName("starvation");
        this.setPotionName("effect.starvation");
    }

    @Override
    public void performEffect(EntityLivingBase living, int amplifier) {
        if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            if (player.getFoodStats().getFoodLevel() > 4) {
                player.addExhaustion(0.9F * (float) (amplifier + 1));
            }
        }

        if (living.isPotionActive(MobEffects.HUNGER)) {
            living.removePotionEffect(MobEffects.HUNGER);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect) {
        return false;
    }

}
