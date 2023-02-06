package projecthds.herodotusutils.mixins.mods.agricraft;

import com.agricraft.agricore.plant.AgriPlant;
import net.minecraft.util.text.translation.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("deprecation")
@Mixin(value = AgriPlant.class, remap = false)
public abstract class MixinAgriPlant {

    @Shadow
    @Final
    private String id;

    @Inject(method = "getSeedName", at = @At(value = "RETURN"), cancellable = true)
    public void injectGetSeedName(CallbackInfoReturnable<String> ci) {

        String translationKey = "hdsutils.agricraft." + this.id + ".seed";
        if (I18n.canTranslate(translationKey)) {
            ci.setReturnValue(I18n.translateToLocal(translationKey));
        }
    }

    @Inject(method = "getPlantName", at = @At(value = "RETURN"), cancellable = true)
    public void injectGetPlantName(CallbackInfoReturnable<String> ci) {
        String translationKey = "hdsutils.agricraft." + this.id + ".plant";
        if (I18n.canTranslate(translationKey)) {
            ci.setReturnValue(I18n.translateToLocal(translationKey));
        }
    }
}
