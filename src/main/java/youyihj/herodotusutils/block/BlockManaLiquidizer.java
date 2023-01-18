package youyihj.herodotusutils.block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class BlockManaLiquidizer extends PlainBlock {

    private BlockManaLiquidizer() {
        super(Material.ROCK, NAME);
    }

    public static final BlockManaLiquidizer INSTANCE = new BlockManaLiquidizer();
    public static final String NAME = "mana_liquidizer";
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName(NAME);

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileManaLiquidizer();
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }
}
