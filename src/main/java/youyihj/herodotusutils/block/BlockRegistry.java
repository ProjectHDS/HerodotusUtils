package youyihj.herodotusutils.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.block.alchemy.*;
import youyihj.herodotusutils.block.computing.*;
import youyihj.herodotusutils.fluid.FluidMana;
import youyihj.herodotusutils.util.ItemDropSupplier;

import java.util.List;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS;

/**
 * @author youyihj
 */
@Mod.EventBusSubscriber
public class BlockRegistry {

    public static final BlockOreBase RED_ORE = new BlockOreBase("red", 0xfc0d20);
    public static final BlockOreBase YELLOW_ORE = new BlockOreBase("yellow", 0xffd701);
    public static final BlockOreBase BLUE_ORE = new BlockOreBase("blue", 0x00a2dd);
    public static final BlockOreBase RHOMBUS_ORE = new BlockOreBase("rhombus", 0xffffff)
        .setDropItemSupplier(
            ItemDropSupplier.of(() -> new ItemStack(ITEMS.getValue(new ResourceLocation("contenttweaker", "rhombus"))))
        );
    public static final BlockOreBase SPHERICAL_ORE = new BlockOreBase("spherical", 0xffffff)
        .setDropItemSupplier(
            ItemDropSupplier.of(() -> new ItemStack(ITEMS.getValue(new ResourceLocation("contenttweaker", "spherical"))))
        );
    public static final BlockOreBase SQUARE_ORE = new BlockOreBase("square", 0xffffff)
        .setDropItemSupplier(
            ItemDropSupplier.of(() -> new ItemStack(ITEMS.getValue(new ResourceLocation("contenttweaker", "square"))))
        );
    public static final List<BlockOreBase> ORES = Lists.newArrayList(RED_ORE, YELLOW_ORE, BLUE_ORE, RHOMBUS_ORE, SPHERICAL_ORE, SQUARE_ORE);
    private static final Block FLUID_MANA_BLOCK = new BlockFluidClassic(FluidMana.INSTANCE, Material.WATER).setRegistryName("fluid_mana");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.registerAll(
                FLUID_MANA_BLOCK,
                BlockMercury.INSTANCE,
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
                BlockPrimordialCharger.INSTANCE
        );
        BlockTransporter.getBlockMap().values().forEach(registry::register);
        BlockGolemCore.BLOCKS.forEach(registry::register);
        ORES.forEach(registry::register);
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
    }
}
