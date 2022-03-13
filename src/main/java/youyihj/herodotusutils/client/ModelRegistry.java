package youyihj.herodotusutils.client;

import hellfirepvp.modularmachinery.common.block.BlockDynamicColor;
import hellfirepvp.modularmachinery.common.item.ItemDynamicColor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
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
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.block.*;
import youyihj.herodotusutils.block.BlockGolemCore;
import youyihj.herodotusutils.block.BlockManaLiquidizer;
import youyihj.herodotusutils.block.BlockOreBase;
import youyihj.herodotusutils.block.BlockRegistry;
import youyihj.herodotusutils.block.alchemy.*;
import youyihj.herodotusutils.block.computing.BlockCalculatorController;
import youyihj.herodotusutils.block.computing.BlockCalculatorStructure;
import youyihj.herodotusutils.block.computing.BlockComputingModule;
import youyihj.herodotusutils.block.computing.BlockTransporter;
import youyihj.herodotusutils.block.organism.plugin.BlockConditionPlugin;
import youyihj.herodotusutils.client.render.TileLazyTunnelRender;
import youyihj.herodotusutils.client.render.TilePrimordialChargerRender;
import youyihj.herodotusutils.client.render.TileRoundRobinTunnelRender;
import youyihj.herodotusutils.client.render.*;
import youyihj.herodotusutils.entity.EntityRedSlime;
import youyihj.herodotusutils.entity.RenderRedSlime;
import youyihj.herodotusutils.entity.golem.*;
import youyihj.herodotusutils.fluid.FluidMana;
import youyihj.herodotusutils.fluid.FluidMercury;
import youyihj.herodotusutils.item.*;
import youyihj.herodotusutils.modsupport.modularmachinery.block.BlockAspectListProviderInput;
import youyihj.herodotusutils.modsupport.modularmachinery.block.BlockImpetusHatch;
import youyihj.herodotusutils.organism.StructureTier;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * @author youyihj
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ModelRegistry {
    private static final IStateMapper ORE_STATE_MAPPER = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            switch (state.getValue(BlockOreBase.PROPERTY_TYPE)) {
                case POOR:
                    return new ModelResourceLocation(HerodotusUtils.rl("poor_ore"), "normal");
                case NORMAL:
                    return new ModelResourceLocation(HerodotusUtils.rl("normal_ore"), "normal");
                case DENSE:
                    return new ModelResourceLocation(HerodotusUtils.rl("dense_ore"), "normal");
                default:
                    return null;
            }
        }
    };

    private static final IntFunction<ModelResourceLocation> META_ORE_STATE_MAPPER = meta -> {
        switch (meta) {
            case 1:
                return new ModelResourceLocation(HerodotusUtils.rl("poor_ore"), "inventory");
            case 2:
                return new ModelResourceLocation(HerodotusUtils.rl("dense_ore"), "inventory");
            default:
                return new ModelResourceLocation(HerodotusUtils.rl("normal_ore"), "inventory");
        }
    };

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
                BlockAspectListProviderInput.ITEM_BLOCK,
                BlockImpetusHatch.Input.ITEM_BLOCK,
                BlockImpetusHatch.Output.ITEM_BLOCK,
                BlockCatalyzedAltar.ITEM_BLOCK,
                BlockPrimordialCharger.ITEM_BLOCK,
                BlockImpetusHatch.Output.ITEM_BLOCK,
                BlockAlchemyCrafter.ITEM_BLOCK,
                BlockAlchemySeparator.ITEM_BLOCK,
                BlockConditionPlugin.WATER.getItemBlock(),
                BlockConditionPlugin.LIGHT.getItemBlock(),
                BlockConditionPlugin.OXYGEN.getItemBlock(),
                BlockConditionPlugin.PRESSURE.getItemBlock(),
                BlockConditionPlugin.TEMPERATURE.getItemBlock(),
                BlockConditionPlugin.HUMIDITY.getItemBlock()
        );
        for (BlockOreBase ore : BlockRegistry.ORES) {
            ModelLoader.setCustomStateMapper(ore, ORE_STATE_MAPPER);
            for (int i = 0; i < BlockOreBase.Type.values().length; i++) {
                ModelLoader.setCustomModelResourceLocation(ore.getItem(), i, META_ORE_STATE_MAPPER.apply(i));
            }
        }
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
        StructureTier.BRASS.register(block -> registerItemModel(block.getItem()));
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
                .filter(ItemDynamicColor.class::isInstance)
                .filter(entry -> entry.getRegistryName().getResourceDomain().equals(HerodotusUtils.MOD_ID))
                .forEach(entry -> {
                    if (entry instanceof Item) {
                        itemColors.registerItemColorHandler(((ItemDynamicColor) entry)::getColorFromItemstack, ((Item) entry));
                    } else if (entry instanceof Block) {
                        itemColors.registerItemColorHandler(((ItemDynamicColor) entry)::getColorFromItemstack, ((Block) entry));
                    }
                });
    }

    @SubscribeEvent
    public static void blockColor(ColorHandlerEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            if (entry.getKey().getResourceDomain().equals(HerodotusUtils.MOD_ID)) {
                Block block = entry.getValue();
                if (block instanceof BlockDynamicColor) {
                    blockColors.registerBlockColorHandler(((BlockDynamicColor) block)::getColorMultiplier, block);
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
