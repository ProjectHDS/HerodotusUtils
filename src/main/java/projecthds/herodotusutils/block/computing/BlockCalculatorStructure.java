package projecthds.herodotusutils.block.computing;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import projecthds.herodotusutils.block.PlainBlock;

public class BlockCalculatorStructure extends PlainBlock {

    public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

    public BlockCalculatorStructure(String name) {
        super(Material.IRON, name);
        this.setHarvestLevel("pickaxe", 2);
    }

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
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(ACTIVATED) ? 14 : 0;
    }

    public static final BlockCalculatorStructure STRUCTURE_BLOCK_1 = new BlockCalculatorStructure("structure_block_1");
    public static final BlockCalculatorStructure STRUCTURE_BLOCK_2 = new BlockCalculatorStructure("structure_block_2");
    public static final BlockCalculatorStructure STRUCTURE_BLOCK_3 = new BlockCalculatorStructure("structure_block_3");
    public static final Item STRUCTURE_BLOCK_1_ITEM = new ItemBlock(STRUCTURE_BLOCK_1).setRegistryName("structure_block_1");
    public static final Item STRUCTURE_BLOCK_2_ITEM = new ItemBlock(STRUCTURE_BLOCK_2).setRegistryName("structure_block_2");
    public static final Item STRUCTURE_BLOCK_3_ITEM = new ItemBlock(STRUCTURE_BLOCK_3).setRegistryName("structure_block_3");
}
