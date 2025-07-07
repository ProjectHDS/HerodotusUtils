package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import projecthds.herodotusutils.block.PlainBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BlockOreDimCrystal extends PlainBlock {
    public static final String NAME = "dimcrystal";
    public static final List<BlockOreDimCrystal> BLOCKS = new ArrayList<>(3);
    public static final List<BlockOreDimCrystal.Item> ITEM_BLOCKS = new ArrayList<>(3);
    public final String content;

    static {
        for (String type: new String[]{"Copper", "Iron", "Tin", "Lead"}) {
            BlockOreDimCrystal block = new BlockOreDimCrystal(type);
            BLOCKS.add(block);
            BlockOreDimCrystal.Item itemBlock = new BlockOreDimCrystal.Item(block);
            itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
            ITEM_BLOCKS.add(itemBlock);
        }
    }

    BlockOreDimCrystal(String ore) {
        super(Material.GLASS, NAME + "_" + ore.toLowerCase(Locale.ENGLISH));
        content = ore.toLowerCase(Locale.ENGLISH);
        this.setResistance(3600000.0F);
        this.blockHardness = -1;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();
        return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }

    public static class Item extends ItemBlock {
        public Item(Block block) {
            super(block);
        }
    }

}
