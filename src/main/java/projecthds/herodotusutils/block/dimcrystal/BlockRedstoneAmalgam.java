package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import projecthds.herodotusutils.block.PlainBlock;

import javax.annotation.Nullable;

public class BlockRedstoneAmalgam extends PlainBlock {
    public static final BlockRedstoneAmalgam INSTANCE = new BlockRedstoneAmalgam();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("redstone_amalgam");

    private BlockRedstoneAmalgam() {
        super(Material.GLASS, "redstone_amalgam");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TileRedstoneAmalgam(); }

    @Mod.EventBusSubscriber
    public class Logic{
        @SubscribeEvent
        public static void onFirePlaced(BlockEvent.Eve)
    }

}
