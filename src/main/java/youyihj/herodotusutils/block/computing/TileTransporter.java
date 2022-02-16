package youyihj.herodotusutils.block.computing;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.computing.IComputingUnitConsumer;

/**
 * @author youyihj
 */
public class TileTransporter extends TileEntity implements ITickable, IComputingUnitConsumer {
    public TileTransporter(int capacity) {
        this.capacity = capacity;
    }

    public TileTransporter() {
    }

    private int capacity;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.capacity = compound.getInteger("capacity");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("capacity", this.capacity);
        return super.writeToNBT(compound);
    }

    private void update0() {
        boolean working = this.consumeToChunk(world, pos);
        if (working != world.getBlockState(pos).getValue(BlockTransporter.ACTIVATED)) {
            world.setBlockState(pos, BlockTransporter.getBlockMap().get(capacity).getDefaultState().withProperty(BlockTransporter.ACTIVATED, working));
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public int consumeAmount() {
        return capacity;
    }

    @Override
    public void update() {
        if (world.isRemote || world.getTotalWorldTime() % 80 != 14)
            return;
        update0();
    }
}
