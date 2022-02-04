package youyihj.herodotusutils.mixins.mods.agricraft;

import com.infinityraider.agricraft.api.v1.util.FuzzyStack;
import com.infinityraider.agricraft.core.JsonPlant;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * @author youyihj
 */
@Mixin(value = JsonPlant.class, remap = false)
public abstract class MixinJsonPlant {
    @Shadow
    public abstract Collection<FuzzyStack> getSeedItems();

    @Inject(method = "getSeed", at = @At("RETURN"))
    private void mergeOriginTag(CallbackInfoReturnable<ItemStack> cir) {
        Optional<NBTTagCompound> originTag = getSeedItems().stream().map(FuzzyStack::getTagCompound).findFirst();
        ItemStack item = cir.getReturnValue();
        if (item.hasTagCompound() && originTag.isPresent()) {
            Objects.requireNonNull(item.getTagCompound()).merge(originTag.get());
        }
    }
}
