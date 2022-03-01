package youyihj.herodotusutils.block.organism;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import youyihj.herodotusutils.organism.StructureTier;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class BlockFluidOutputInterface extends BlockStructure {
    public static final BlockFluidOutputInterface BRASS = new BlockFluidOutputInterface(StructureTier.BRASS);

    public BlockFluidOutputInterface(StructureTier tier) {
        super("fluid_output_interface", tier);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileFluidOutputInterface();
    }
}
