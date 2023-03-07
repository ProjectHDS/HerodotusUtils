package projecthds.herodotusutils.block.computing;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import projecthds.herodotusutils.block.PlainBlock;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author youyihj
 */
public class BlockTransporter extends PlainBlock {
    private BlockTransporter(int capacity) {
        super(Material.IRON, "transporter_" + capacity);
        this.capacity = capacity;
        blockMap.put(capacity, this);
        itemBlockMap.put(capacity, new ItemBlock(this).setRegistryName("transporter_" + capacity));
    }

    static final PropertyBool ACTIVATED = PropertyBool.create("activated");
    private static final Int2ObjectMap<BlockTransporter> blockMap = new Int2ObjectArrayMap<>();
    private static final Int2ObjectMap<Item> itemBlockMap = new Int2ObjectArrayMap<>();

    static {
        List<Integer> temp = Lists.newArrayList(1, 5, 10, 20, 50, 100);
        temp.forEach(BlockTransporter::new);
    }

    public static Map<Integer, BlockTransporter> getBlockMap() {
        return ImmutableMap.copyOf(blockMap);
    }

    public static Map<Integer, Item> getItemBlockMap() {
        return ImmutableMap.copyOf(itemBlockMap);
    }

    private final int capacity;

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVATED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVATED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVATED, meta == 1);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileTransporter(capacity);
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }
}
