package youyihj.herodotusutils.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import youyihj.herodotusutils.block.*;
import youyihj.herodotusutils.block.alchemy.*;
import youyihj.herodotusutils.block.computing.BlockCalculatorController;
import youyihj.herodotusutils.block.computing.BlockCalculatorStructure;
import youyihj.herodotusutils.block.computing.BlockComputingModule;
import youyihj.herodotusutils.block.computing.BlockTransporter;
import youyihj.herodotusutils.modsupport.modularmachinery.block.BlockAspectListProviderInput;
import youyihj.herodotusutils.modsupport.modularmachinery.block.BlockImpetusHatch;

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
                StarlightStorageTiny.INSTANCE,
                ItemOilAIOT.INSTANCE,
                ItemRiftFeed.INSTANCE,
                ItemPenumbraRing.INSTANCE,
                BlockAlchemyController.ITEM_BLOCK,
                BlockPlainAlchemyTunnel.VERTICAL_ITEM,
                BlockPlainAlchemyTunnel.RIGHT_ANGLE_ITEM,
                BlockPlainAlchemyTunnel.STRAIGHT_ITEM,
                BlockAlchemyInputHatch.ITEM_BLOCK,
                BlockAlchemyOutputHatch.ITEM_BLOCK,
                BlockAlchemyRoundRobinTunnel.ITEM_BLOCK,
                BlockLazyAlchemyTunnel.ITEM_BLOCK,
                BlockAlchemyCrafter.ITEM_BLOCK,
                BlockAspectListProviderInput.ITEM_BLOCK,
                BlockImpetusHatch.Input.ITEM_BLOCK,
                BlockImpetusHatch.Output.ITEM_BLOCK,
                BlockAlchemyCrafter.ITEM_BLOCK,
                BlockAlchemySeparator.ITEM_BLOCK,
                BlockAlchemySeparatorTank.ITEM_BLOCK,
                BlockImpetusHatch.Output.ITEM_BLOCK,
                BlockCreatureDataAnalyzer.ITEM_BLOCK,
                BlockCreatureDataReEncodeInterface.ITEM_BLOCK,
                BlockCatalyzedAltar.ITEM_BLOCK,
                BlockPrimordialCharger.ITEM_BLOCK
        );
        BlockRegistry.ORES.stream().map(BlockOreBase::getItem).forEach(registry::register);
        BlockTransporter.getItemBlockMap().values().forEach(registry::register);
    }
}
