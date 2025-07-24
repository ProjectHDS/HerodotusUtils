package projecthds.herodotusutils.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import projecthds.herodotusutils.block.dimcrystal.*;
import projecthds.herodotusutils.fluid.FluidMana;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.block.alchemy.*;
import projecthds.herodotusutils.block.computing.*;

/**
 * @author youyihj
 */
@Mod.EventBusSubscriber
public class BlockRegistry {
    private static final Block FLUID_MANA_BLOCK = new BlockFluidClassic(FluidMana.INSTANCE, Material.WATER).setRegistryName("fluid_mana");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.registerAll(
                FLUID_MANA_BLOCK,
                BlockMercury.INSTANCE,
                BlockMercurySteam.INSTANCE,
                BlockManaLiquidizer.INSTANCE,
                BlockCalculatorStructure.STRUCTURE_BLOCK_1,
                BlockCalculatorStructure.STRUCTURE_BLOCK_2,
                BlockCalculatorStructure.STRUCTURE_BLOCK_3,
                BlockCalculatorController.CONTROLLER_1,
                BlockCalculatorController.CONTROLLER_2,
                BlockCalculatorController.CONTROLLER_3,
                BlockComputingModule.INSTANCE,
                BlockAlchemyController.INSTANCE,
                BlockPlainAlchemyTunnel.STRAIGHT,
                BlockPlainAlchemyTunnel.HORIZONTAL_RIGHT_ANGLE,
                BlockPlainAlchemyTunnel.VERTICAL_RIGHT_ANGLE,
                BlockAlchemyInputHatch.INSTANCE,
                BlockAlchemyOutputHatch.INSTANCE,
                BlockAlchemyRoundRobinTunnel.INSTANCE,
                BlockLazyAlchemyTunnel.INSTANCE,
                BlockAlchemyCrafter.INSTANCE,
                BlockAlchemySeparator.INSTANCE,
                BlockAlchemySeparatorTank.INSTANCE,
                BlockCreatureDataReEncodeInterface.INSTANCE,
                BlockCreatureDataAnalyzer.INSTANCE,
                BlockCatalyzedAltar.INSTANCE,
                BlockPrimordialCharger.INSTANCE,
                BlockManaCatalyst.INSTANCE,
                BlockPlainDimCrystal.INSTANCE,
                BlockLithiumQuartzOre.INSTANCE,
                BlockLithiumQuartzPowder.INSTANCE,
                BlockPowderedLithiumQuartz.INSTANCE,
                BlockLithiumQuartz.INSTANCE,
                BlockUnstableLeadDimFragment.INSTANCE,
                BlockRedstoneAmalgam.INSTANCE
        );
        BlockTransporter.getBlockMap().values().forEach(registry::register);
        BlockGolemCore.BLOCKS.forEach(registry::register);
        BlockOreDimCrystal.BLOCKS.forEach(registry::register);
        GameRegistry.registerTileEntity(TileManaLiquidizer.class, HerodotusUtils.rl("mana_liquidizer"));
        GameRegistry.registerTileEntity(TileCalculatorController.class, HerodotusUtils.rl("calculator_controller"));
        GameRegistry.registerTileEntity(TileComputingModule.class, HerodotusUtils.rl("computing_module"));
        GameRegistry.registerTileEntity(TileTransporter.class, HerodotusUtils.rl("transporter"));
        GameRegistry.registerTileEntity(TileAlchemyController.class, HerodotusUtils.rl("alchemy_controller"));
        GameRegistry.registerTileEntity(TileAlchemyTunnel.class, HerodotusUtils.rl("alchemy_tunnel"));
        GameRegistry.registerTileEntity(TileAlchemyInputHatch.class, HerodotusUtils.rl("alchemy_input_hatch"));
        GameRegistry.registerTileEntity(TileAlchemyOutputHatch.class, HerodotusUtils.rl("alchemy_output_hatch"));
        GameRegistry.registerTileEntity(TileAlchemyRoundRobinTunnel.class, HerodotusUtils.rl("alchemy_round_robin_tunnel"));
        GameRegistry.registerTileEntity(TileAlchemyLazyTunnel.class, HerodotusUtils.rl("alchemy_lazy_tunnel"));
        GameRegistry.registerTileEntity(TileAlchemyCrafter.class, HerodotusUtils.rl("alchemy_crafter"));
        GameRegistry.registerTileEntity(TileCreatureDataAnalyzer.class, HerodotusUtils.rl("creature_data_analyzer"));
        GameRegistry.registerTileEntity(TileCreatureDataReEncodeInterface.class, HerodotusUtils.rl("creature_encode_interface"));
        GameRegistry.registerTileEntity(TilePrimordialCharger.class, HerodotusUtils.rl("primordial_changer"));
        GameRegistry.registerTileEntity(TileAlchemySeparatorTank.class, HerodotusUtils.rl("alchemy_separator_tank"));
        GameRegistry.registerTileEntity(TileAlchemySeparator.class, HerodotusUtils.rl("alchemy_separator"));
        GameRegistry.registerTileEntity(TileGolemCore.class, HerodotusUtils.rl("golem_core"));
        GameRegistry.registerTileEntity(TileManaCatalyst.class, HerodotusUtils.rl("mana_catalyst"));
        GameRegistry.registerTileEntity(TileLithiumQuartzOre.class, HerodotusUtils.rl("lithium_quartz_ore"));
        GameRegistry.registerTileEntity(TileLithiumQuartzBlock.class, HerodotusUtils.rl("lithium_quartz_block"));
        GameRegistry.registerTileEntity(TileLithiumQuartzPowderBlock.class, HerodotusUtils.rl("lithium_quartz_powder_block"));
        GameRegistry.registerTileEntity(TilePowderedLithiumQuartzBlock.class, HerodotusUtils.rl("powdered_lithium_quartz_block"));
        GameRegistry.registerTileEntity(TileRedstoneAmalgam.class, HerodotusUtils.rl("redstone_amalgam"));
    }
}
