package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileLithiumQuartzBlock extends TileEntity {

    public short updateCount = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setShort("count",updateCount);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.updateCount = compound.getShort("count");
    }
}
