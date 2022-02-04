package youyihj.herodotusutils.mixins.init;

import org.spongepowered.asm.mixin.Mixins;
import zone.rong.mixinbooter.MixinLoader;

@MixinLoader
public class MixinInit {
    public MixinInit() {
        Mixins.addConfiguration("mixins.hdsutils.json");
    }
}
