package projecthds.herodotusutils.modsupport.topography;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.proxy.CommonProxy;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

/**
 * @author youyihj
 */
public class HackTopographyDummyProxy extends CommonProxy {
    @Override
    public void registerEventHandlers() {
        // NO-OP
    }

    @Override
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        ConfigurationManager.setup();
    }
}
