package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import projecthds.herodotusutils.block.PlainBlock;


public class BlockPlainDimCrystal extends PlainBlock {
    public static final BlockPlainDimCrystal INSTANCE = new BlockPlainDimCrystal();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("dimcrystal");

    private BlockPlainDimCrystal() {
        super(Material.GLASS, "dimcrystal");
        this.blockHardness = -1f;
        this.blockResistance = -1f;
        this.fullBlock = false;
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }
}
