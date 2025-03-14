package projecthds.herodotusutils.mixins.mods.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.api.research.ResearchStage;

/**
 * @author youyihj
 */
@Mixin(ResearchStage.class)
public class MixinResearchStage {
    /**
     * @author youyihj
     * @reason disable warp gain when unlocked research
     */
    @Overwrite(remap = false)
    public int getWarp() {
        return 0;
    }
}
