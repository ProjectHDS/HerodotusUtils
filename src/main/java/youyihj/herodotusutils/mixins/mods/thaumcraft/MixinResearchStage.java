package youyihj.herodotusutils.mixins.mods.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.api.research.ResearchStage;

/**
 * @author youyihj
 */
@Mixin(value = ResearchStage.class, remap = false)
public class MixinResearchStage {
    /**
     * @author youyihj
     * @reason disable warp gain when unlocked research
     */
    @Overwrite
    public int getWarp() {
        return 0;
    }
}
