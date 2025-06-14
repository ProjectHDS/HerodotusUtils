package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;


public class TileLithiumQuartzOre extends TileEntity{

    public long timeRecorded = -1;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.timeRecorded = tag.getLong("time");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setLong("time", this.timeRecorded);
        return super.writeToNBT(tag);
    }

}
