package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;


public class TileLithiumQuartz extends TileEntity{

    public long timeRecorded = -1;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.timeRecorded = tag.getLong("Time");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setLong("Time", this.timeRecorded);
        return super.writeToNBT(tag);
    }

}
