package youyihj.herodotusutils.block.organism;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.block.PlainBlock;
import youyihj.herodotusutils.organism.LauncherManager;
import youyihj.herodotusutils.organism.StructureTier;

/**
 * @author youyihj
 */
public class BlockStructure extends PlainBlock {
    private final StructureTier tier;
    private final Item item;

    public BlockStructure(String name, StructureTier tier) {
        super(Material.IRON, name = tier.getName2() + "_" + name);
        this.tier = tier;
        this.item = new ItemBlock(this);
        this.item.setRegistryName(name);
    }

    public Item getItem() {
        return item;
    }

    public StructureTier getTier() {
        return tier;
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
}
