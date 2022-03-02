package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.block.PlainBlock;
import youyihj.herodotusutils.organism.LauncherManager;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author youyihj
 */
public class BlockConditionPlugin extends PlainBlock {
    public static final BlockConditionPlugin LIGHT = new BlockConditionPlugin("light_plugin", TileLightPlugin::new);
    public static final BlockConditionPlugin HUMIDITY = new BlockConditionPlugin("humidity_plugin", TileHumidityPlugin::new);
    public static final BlockConditionPlugin PRESSURE = new BlockConditionPlugin("pressure_plugin", TilePressurePlugin::new);
    public static final BlockConditionPlugin WATER = new BlockConditionPlugin("water_plugin", TileWaterPlugin::new);
    public static final BlockConditionPlugin OXYGEN = new BlockConditionPlugin("oxygen_plugin", TileOxygenPlugin::new);
    public static final BlockConditionPlugin TEMPERATURE = new BlockConditionPlugin("temperature_plugin", TileTemperaturePlugin::new);

    private final Supplier<TileEntity> tileEntityFactory;
    private final ItemBlock itemBlock;

    public BlockConditionPlugin(String name, Supplier<TileEntity> tileEntityFactory) {
        super(Material.IRON, name);
        this.itemBlock = new ItemBlock(this);
        itemBlock.setRegistryName(Objects.requireNonNull(this.getRegistryName()));
        this.tileEntityFactory = tileEntityFactory;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return tileEntityFactory.get();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        LauncherManager.onRefreshStructure(pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        LauncherManager.onRefreshStructure(pos);
        super.breakBlock(worldIn, pos, state);
    }

    public ItemBlock getItemBlock() {
        return itemBlock;
    }
}
