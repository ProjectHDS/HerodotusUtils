package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;


public class TileLithiumQuartzPowderBlock extends TileEntity{
    public short updatedTimes = 0;
    public short randomTickedCount = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("updatedTimes", updatedTimes);
        compound.setShort("tickedTimes", randomTickedCount);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        updatedTimes = compound.getShort("updatedTimes");
        randomTickedCount = compound.getShort("tickedTimes");
    }

}
