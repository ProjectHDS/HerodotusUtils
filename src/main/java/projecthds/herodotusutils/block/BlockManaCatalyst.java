package projecthds.herodotusutils.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.common.entity.EntityManaBurst;
import projecthds.herodotusutils.recipe.ManaCatalystTransform;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class BlockManaCatalyst extends PlainBlock {
    public static final BlockManaCatalyst INSTANCE = new BlockManaCatalyst();

    private BlockManaCatalyst() {
        super(Material.IRON, "mana_catalyst");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileManaCatalyst();
    }

    public static class Item extends ItemBlock implements ILensEffect {

        public static final Item INSTANCE = new Item();

        private Item() {
            super(BlockManaCatalyst.INSTANCE);
            this.setRegistryName("mana_catalyst");
        }

        @Override
        public void apply(ItemStack stack, BurstProperties props) {

        }

        @Override
        public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
            World world = ((EntityManaBurst) burst).world;
            if (!burst.isFake() && !isManaBlock && !world.isRemote) {
                BlockPos blockPos = pos.getBlockPos();
                IBlockState result = ManaCatalystTransform.getResult(world.getBlockState(blockPos));
                if (result != null) {
                    world.setBlockState(blockPos, result);
                }
                return true;
            }
            return dead;
        }

        @Override
        public void updateBurst(IManaBurst burst, ItemStack stack) {

        }

        @Override
        public boolean doParticles(IManaBurst burst, ItemStack stack) {
            return true;
        }
    }
}
