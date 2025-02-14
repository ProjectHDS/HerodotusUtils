package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import projecthds.herodotusutils.block.PlainBlock;

import javax.annotation.Nullable;

public class BlockLithiumQuartz extends PlainBlock {
    public static final BlockLithiumQuartz INSTANCE = new BlockLithiumQuartz();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("lithium_quartz");

    private BlockLithiumQuartz() {
        super(Material.GLASS, "lithium_quartz");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TileLithiumQuartz(); }
}
