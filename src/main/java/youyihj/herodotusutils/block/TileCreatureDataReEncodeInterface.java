package youyihj.herodotusutils.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author youyihj
 */
public class TileCreatureDataReEncodeInterface extends TileEntity {
    private int channel = TileCreatureDataAnalyzer.UNDETERMINED_CHANNEL;

    public void setChannel(int channel) {
        if (world != null && !world.isRemote) {
            updateChannel(channel);
        }
    }

    @Override
    public void onLoad() {
        if (!world.isRemote && channel != TileCreatureDataAnalyzer.UNDETERMINED_CHANNEL) {
            updateChannel(channel);
        }
    }

    public int getChannel() {
        return channel;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("channel", channel);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.channel = compound.getInteger("channel");
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        TileCreatureDataAnalyzer.INTERFACES_BY_CHANNEL.get(channel).remove(this);
    }

    public void toggleType(int type) {
        Integer currentType = world.getBlockState(pos).getValue(BlockCreatureDataReEncodeInterface.TYPE);
        if (currentType == type)
            return;
        IBlockState state = BlockCreatureDataReEncodeInterface.INSTANCE.getDefaultState().withProperty(BlockCreatureDataReEncodeInterface.TYPE, type);
        world.setBlockState(pos, state);
    }

    private void updateChannel(int channel) {
        TileCreatureDataAnalyzer.INTERFACES_BY_CHANNEL.get(channel).remove(this);
        this.channel = channel;
        TileCreatureDataAnalyzer.INTERFACES_BY_CHANNEL.get(channel).add(this);
        toggleType(TileCreatureDataAnalyzer.TYPES_BY_CHANNEL.get(channel));
    }
}
