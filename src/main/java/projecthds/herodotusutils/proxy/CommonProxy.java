package projecthds.herodotusutils.proxy;

import com.bloodnbonesgaming.topography.Topography;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.block.BlockOreBase;
import projecthds.herodotusutils.block.BlockRegistry;
import projecthds.herodotusutils.fluid.FluidAlchemyWaste;
import projecthds.herodotusutils.fluid.FluidMana;
import projecthds.herodotusutils.fluid.FluidMercury;
import projecthds.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;
import projecthds.herodotusutils.modsupport.thaumcraft.AspectHandler;
import projecthds.herodotusutils.modsupport.topography.HackTopographyDummyProxy;
import projecthds.herodotusutils.network.GuiHandler;
import projecthds.herodotusutils.util.Capabilities;
import projecthds.herodotusutils.world.AncientVoidDimensionProvider;

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
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "projecthds.herodotusutils.modsupport.theoneprobe.TOPHandler");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }
}
