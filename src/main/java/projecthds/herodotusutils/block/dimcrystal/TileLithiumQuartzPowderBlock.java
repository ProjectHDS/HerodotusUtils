package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;


public class TileLithiumQuartzPowderBlock extends TileEntity implements ITickable {
    private int updatedTimes = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("updatedTimes", updatedTimes);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        updatedTimes = compound.getInteger("updatedTimes");
    }

    public int getUpdatedTimes() {
        return updatedTimes;
    }

    @Override
    public void update() {
        int ticking = 0;
        if (world.getTotalWorldTime() % 20 == 0 && !world.isRemote) {
            if (ticking <2) {
                
            }
            ticking +=1;
        }
    }
}
