package projecthds.herodotusutils.mixins.mods.railcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import mods.railcraft.common.core.BetaMessageTickHandler;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

@Mixin(value = BetaMessageTickHandler.class, remap = false)
public class MixinBetaWarning {
    /**
    * @author Gary Bryson Luis Jr.
    * @reason disable rc dev version warning
    */
    @Overwrite
    public void tick(LivingUpdateEvent event) {
        // So I wonder why there wasn't a config item for this.
    }
}
