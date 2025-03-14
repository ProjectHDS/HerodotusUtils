package projecthds.herodotusutils.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import projecthds.herodotusutils.block.computing.BlockCalculatorController;
import projecthds.herodotusutils.block.computing.BlockCalculatorStructure;
import projecthds.herodotusutils.block.computing.BlockComputingModule;
import projecthds.herodotusutils.block.computing.BlockTransporter;
import projecthds.herodotusutils.block.*;
import projecthds.herodotusutils.block.alchemy.*;
import projecthds.herodotusutils.block.dimcrystal.*;

/**
 * @author youyihj
 */
@Mod.EventBusSubscriber
public class ItemRegistry {
    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(
                BlockManaLiquidizer.ITEM_BLOCK,
                RefinedBottle.INSTANCE,
                ItemCopperBucket.INSTANCE,
                ItemTaintChecker.INSTANCE,
                ItemAlchemyPipeWrench.INSTANCE,
                BlockCalculatorStructure.STRUCTURE_BLOCK_1_ITEM,
                BlockCalculatorStructure.STRUCTURE_BLOCK_2_ITEM,
                BlockCalculatorStructure.STRUCTURE_BLOCK_3_ITEM,
                BlockCalculatorController.ITEM_BLOCK_1,
                BlockCalculatorController.ITEM_BLOCK_2,
                BlockCalculatorController.ITEM_BLOCK_3,
                BlockComputingModule.ITEM_BLOCK,
                ItemLithiumAmalgam.INSTANCE,
                ItemRiftSword.INSTANCE,
                StarlightStorageTiny.INSTANCE,
                ItemOilAIOT.INSTANCE,
                ItemRiftFeed.INSTANCE,
                ItemPenumbraRing.INSTANCE,
                ItemLithiumQuartzPowder.INSTANCE,
                ItemLithiumQuartz.INSTANCE,
                GolemUpperSword.INSTANCE,
                GolemDownerSword.INSTANCE,
                BlockAlchemyController.ITEM_BLOCK,
                BlockPlainAlchemyTunnel.VERTICAL_ITEM,
                BlockPlainAlchemyTunnel.RIGHT_ANGLE_ITEM,
                BlockPlainAlchemyTunnel.STRAIGHT_ITEM,
                BlockAlchemyInputHatch.ITEM_BLOCK,
                BlockAlchemyOutputHatch.ITEM_BLOCK,
                BlockAlchemyRoundRobinTunnel.ITEM_BLOCK,
                BlockLazyAlchemyTunnel.ITEM_BLOCK,
                BlockAlchemyCrafter.ITEM_BLOCK,
                BlockAlchemyCrafter.ITEM_BLOCK,
                BlockAlchemySeparator.ITEM_BLOCK,
                BlockAlchemySeparatorTank.ITEM_BLOCK,
                BlockCreatureDataAnalyzer.ITEM_BLOCK,
                BlockCreatureDataReEncodeInterface.ITEM_BLOCK,
                BlockCatalyzedAltar.ITEM_BLOCK,
                BlockPrimordialCharger.ITEM_BLOCK,
                BlockManaCatalyst.Item.INSTANCE,
                BlockPlainDimCrystal.ITEM_BLOCK,
                BlockLithiumQuartz.ITEM_BLOCK,
                BlockLithiumQuartzPowderBlock.ITEM_BLOCK,
                BlockRedstoneAmalgam.ITEM_BLOCK
        );
        BlockGolemCore.ITEM_BLOCKS.forEach(registry::register);
        BlockOreDimCrystal.ITEM_BLOCKS.forEach(registry::register);
        BlockTransporter.getItemBlockMap().values().forEach(registry::register);
    }
}
