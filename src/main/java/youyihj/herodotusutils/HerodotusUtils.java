package youyihj.herodotusutils;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import youyihj.herodotusutils.proxy.IProxy;

@Mod(
        modid = HerodotusUtils.MOD_ID,
        name = HerodotusUtils.MOD_NAME,
        version = HerodotusUtils.VERSION,
        dependencies = "before:contenttweaker"
)
public class HerodotusUtils {

    public static final String MOD_ID = "hdsutils";
    public static final String MOD_NAME = "HerodotusUtils";
    public static final String VERSION = "1.0";

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static Logger logger;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static HerodotusUtils INSTANCE;

    @SidedProxy(serverSide = "youyihj.herodotusutils.proxy.CommonProxy", clientSide = "youyihj.herodotusutils.proxy.ClientProxy")
    public static IProxy proxy;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("welcome to herodotus modpack!");
        proxy.preInit(event);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
