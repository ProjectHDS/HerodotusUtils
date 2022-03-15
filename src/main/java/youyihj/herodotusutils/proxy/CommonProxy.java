package youyihj.herodotusutils.proxy;

import com.bloodnbonesgaming.topography.Topography;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.block.BlockOreBase;
import youyihj.herodotusutils.block.BlockRegistry;
import youyihj.herodotusutils.fluid.FluidAlchemyWaste;
import youyihj.herodotusutils.fluid.FluidMana;
import youyihj.herodotusutils.fluid.FluidMercury;
import youyihj.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;
import youyihj.herodotusutils.modsupport.thaumcraft.AspectHandler;
import youyihj.herodotusutils.modsupport.topography.HackTopographyDummyProxy;
import youyihj.herodotusutils.network.GuiHandler;
import youyihj.herodotusutils.util.Capabilities;
import youyihj.herodotusutils.world.AncientVoidDimensionProvider;

public class CommonProxy implements IProxy {
    public static DimensionType ANCIENT_VOID_DIMENSION;
    public static final int ANCIENT_VOID_DIMENSION_ID = 943;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        FluidRegistry.registerFluid(FluidMana.INSTANCE);
        FluidRegistry.registerFluid(FluidMercury.INSTANCE);
        FluidRegistry.addBucketForFluid(FluidMana.INSTANCE);
        FluidRegistry.addBucketForFluid(FluidMercury.INSTANCE);
        Capabilities.register();
        FluidRegistry.registerFluid(FluidAlchemyWaste.INSTANCE);
        FluidRegistry.addBucketForFluid(FluidAlchemyWaste.INSTANCE);
        NetworkRegistry.INSTANCE.registerGuiHandler(HerodotusUtils.MOD_ID, GuiHandler.INSTANCE);
        ANCIENT_VOID_DIMENSION = DimensionType.register("ancient_void", "_ancient_void", ANCIENT_VOID_DIMENSION_ID, AncientVoidDimensionProvider.class, false);
        DimensionManager.registerDimension(ANCIENT_VOID_DIMENSION_ID, ANCIENT_VOID_DIMENSION);
        AspectHandler.initAspects();
        Topography.proxy = new HackTopographyDummyProxy();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        BloodAltarStructures.loadStructures();
        BlockRegistry.ORES.forEach(BlockOreBase::registerOreDict);
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "youyihj.herodotusutils.modsupport.theoneprobe.TOPHandler");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }
}
