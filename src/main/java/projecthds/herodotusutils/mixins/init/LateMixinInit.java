package projecthds.herodotusutils.mixins.init;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

public class LateMixinInit implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("late_mixins.hdsutils.json");
    }
}
