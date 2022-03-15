package youyihj.herodotusutils.modsupport.modularmachinery.multiblock;

import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import youyihj.herodotusutils.block.alchemy.AbstractPipeBlock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author youyihj
 */
public class AlchemicalPipeBlockInformation extends BlockArray.BlockInformation {
    private static final List<BlockArray.IBlockStateDescriptor> AIR_DESCRIPTOR = Collections.singletonList(new BlockArray.IBlockStateDescriptor(Blocks.AIR.getDefaultState()));

    public AlchemicalPipeBlockInformation() {
        super(AIR_DESCRIPTOR);
    }

    @Override
    public IBlockState getSampleState(Optional<Long> snapTick) {
        return Blocks.AIR.getDefaultState();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getDescriptiveStack(Optional<Long> snapTick) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean matchesState(IBlockState state) {
        Block block = state.getBlock();
        return block == Blocks.AIR || block instanceof AbstractPipeBlock;
    }

    @Override
    public boolean matches(World world, BlockPos at, boolean default_) {
        if(!world.isBlockLoaded(at)) {
            return default_;
        }
        return matchesState(world.getBlockState(at));
    }

    @Override
    public BlockArray.BlockInformation copy() {
        return new AlchemicalPipeBlockInformation();
    }

    @Override
    public BlockArray.BlockInformation copyRotateYCCW() {
        return new AlchemicalPipeBlockInformation();
    }
}
