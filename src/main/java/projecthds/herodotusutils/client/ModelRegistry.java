package projecthds.herodotusutils.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.block.computing.BlockCalculatorController;
import projecthds.herodotusutils.block.computing.BlockCalculatorStructure;
import projecthds.herodotusutils.block.computing.BlockComputingModule;
import projecthds.herodotusutils.block.computing.BlockTransporter;
import projecthds.herodotusutils.block.dimcrystal.*;
import projecthds.herodotusutils.entity.EntityRedSlime;
import projecthds.herodotusutils.entity.RenderRedSlime;
import projecthds.herodotusutils.entity.golem.EntityExtraIronGolem;
import projecthds.herodotusutils.entity.golem.EntityExtraSnowman;
import projecthds.herodotusutils.entity.golem.RenderExtraIronGolem;
import projecthds.herodotusutils.entity.golem.RenderExtraSnowman;
import projecthds.herodotusutils.fluid.FluidMana;
import projecthds.herodotusutils.fluid.FluidMercury;
import projecthds.herodotusutils.block.*;
import projecthds.herodotusutils.block.alchemy.*;
import projecthds.herodotusutils.client.render.*;
import projecthds.herodotusutils.item.*;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author youyihj
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ModelRegistry {
    @SubscribeEvent
    public static void register(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(FluidMana.INSTANCE.getBlock(), new StateMapperBase() {
            @Override
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(HerodotusUtils.rl(FluidMana.INSTANCE.getName()), "defaults");
            }
        });
        ModelLoader.setCustomStateMapper(FluidMercury.INSTANCE.getBlock(), new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(HerodotusUtils.rl(FluidMercury.INSTANCE.getName()), "defaults");
            }
        });
        ModelLoader.setCustomStateMapper(BlockAlchemyController.INSTANCE,
                new StateMap.Builder().ignore(BlockAlchemyController.WORK_TYPE_PROPERTY).build());
        registerMultipleItemsModel(
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
                ItemRiftSword.INSTANCE,
                ItemLithiumQuartzPowder.INSTANCE,
                ItemLithiumQuartz.INSTANCE,
                GolemUpperSword.INSTANCE,
                GolemDownerSword.INSTANCE,
                BlockAlchemyController.ITEM_BLOCK,
                BlockAlchemyInputHatch.ITEM_BLOCK,
                BlockPlainAlchemyTunnel.RIGHT_ANGLE_ITEM,
                BlockPlainAlchemyTunnel.STRAIGHT_ITEM,
                BlockPlainAlchemyTunnel.VERTICAL_ITEM,
                BlockAlchemyOutputHatch.ITEM_BLOCK,
                BlockAlchemyRoundRobinTunnel.ITEM_BLOCK,
                BlockLazyAlchemyTunnel.ITEM_BLOCK,
                BlockAlchemyCrafter.ITEM_BLOCK,
                BlockCatalyzedAltar.ITEM_BLOCK,
                BlockPrimordialCharger.ITEM_BLOCK,
                BlockAlchemyCrafter.ITEM_BLOCK,
                BlockAlchemySeparator.ITEM_BLOCK,
                BlockManaCatalyst.Item.INSTANCE,
                BlockPlainDimCrystal.ITEM_BLOCK,
                BlockLithiumQuartzOre.ITEM_BLOCK,
                BlockLithiumQuartzPowder.ITEM_BLOCK,
                BlockRedstoneAmalgam.ITEM_BLOCK
        );
        BlockOreDimCrystal.BLOCKS.forEach(block -> ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(HerodotusUtils.rl("dimcrystal"), "normal");
            }
        }));
        BlockGolemCore.BLOCKS.forEach(block -> ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(HerodotusUtils.rl("golem_core"), "normal");
            }
        }));
        BlockGolemCore.ITEM_BLOCKS.forEach(item -> {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(HerodotusUtils.rl("golem_core_item"), "inventory"));
            item.setTileEntityItemStackRenderer(new TileGolemCoreItemRender(item));
        });
        for (int i = 0; i < 4; i++) {
            ModelLoader.setCustomModelResourceLocation(BlockAlchemySeparatorTank.ITEM_BLOCK, i, new ModelResourceLocation(Objects.requireNonNull(BlockAlchemySeparatorTank.ITEM_BLOCK.getRegistryName()), "inventory"));
        }
        ModelLoader.setCustomModelResourceLocation(StarlightStorageTiny.INSTANCE, 1,
                new ModelResourceLocation(StarlightStorageTiny.INSTANCE.getRegistryName() + "_full", "inventory"));
        BlockTransporter.getItemBlockMap().values().forEach(ModelRegistry::registerItemModel);
        RenderingRegistry.registerEntityRenderingHandler(EntityRedSlime.class, RenderRedSlime::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileAlchemyRoundRobinTunnel.class, new TileRoundRobinTunnelRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAlchemyLazyTunnel.class, new TileLazyTunnelRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePrimordialCharger.class, new TilePrimordialChargerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGolemCore.class, new TileGolemCoreRender());
        RenderingRegistry.registerEntityRenderingHandler(EntityExtraIronGolem.class, RenderExtraIronGolem::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtraSnowman.class, RenderExtraSnowman::new);
    }

    @SubscribeEvent
    public static void itemColor(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        Stream.concat(ForgeRegistries.ITEMS.getValuesCollection().stream(), ForgeRegistries.BLOCKS.getValuesCollection().stream())
                .filter(IItemHasColor.class::isInstance)
                .filter(entry -> entry.getRegistryName().getNamespace().equals(HerodotusUtils.MOD_ID))
                .forEach(entry -> {
                    if (entry instanceof Item) {
                        itemColors.registerItemColorHandler(((IItemHasColor) entry)::getColorFromItemStack, ((Item) entry));
                    } else if (entry instanceof Block) {
                        itemColors.registerItemColorHandler(((IItemHasColor) entry)::getColorFromItemStack, ((Block) entry));
                    }
                });
    }

    @SubscribeEvent
    public static void blockColor(ColorHandlerEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            if (entry.getKey().getNamespace().equals(HerodotusUtils.MOD_ID)) {
                Block block = entry.getValue();
                if (block instanceof IBlockHasColor) {
                    blockColors.registerBlockColorHandler(((IBlockHasColor) block)::getColorMultiplier, block);
                }
            }
        }
    }

    private static void registerItemModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    private static void registerMultipleItemsModel(Item... items) {
        for (Item item : items) {
            registerItemModel(item);
        }
    }
}
