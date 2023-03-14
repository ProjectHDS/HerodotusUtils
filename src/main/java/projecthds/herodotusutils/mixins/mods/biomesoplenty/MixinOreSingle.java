package projecthds.herodotusutils.mixins.mods.biomesoplenty;

import biomesoplenty.api.config.IConfigObj;
import biomesoplenty.common.world.generator.GeneratorOreSingle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GeneratorOreSingle.class)
public class MixinOreSingle {
    /**
     * @author Gary Bryson Luis Jr.
     * @reason Weirdly stop BOP ore gen.
     * */
    @Overwrite(remap = false)
    public void configure(IConfigObj conf) {
        // no
    }
}
