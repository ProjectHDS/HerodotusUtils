package youyihj.herodotusutils.modsupport.modularmachinery.tile;

import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.tiles.TileThaumcraft;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author ikexing
 */
public class TileAspectListProvider extends TileThaumcraft implements MachineComponentTile, IAspectSource, IEssentiaTransport, ITickable {

    public static final int MAX_ASPECT = 6;
    public static final int MAX_AMOUNT = 250;
    public static final int SUCTION_AMOUNT = 32; // what fuck this ???

    private AspectList aspects = new AspectList();

    private Aspect aspectCurrent = null;

    @Override
    public void update() {
        if (!getWorld().isRemote && this.aspects.getAspects().length > 0) {
            if (this.aspectCurrent == null) {
                int randInt = 0;
                Aspect[] aspects = this.aspects.getAspects();
                if (aspects.length > 1)
                    randInt = getWorld().rand.nextInt(aspects.length - 1);
                aspectCurrent = this.aspects.getAspects()[randInt];
            }
            if (this.aspectCurrent != null && this.aspects.getAmount(aspectCurrent) > MAX_AMOUNT) {
                aspectCurrent = null;
            }
            for (EnumFacing value : EnumFacing.VALUES) {
                fill(value);
            }
        }
    }

    private void fill(EnumFacing face) {
        TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.world, this.pos, face);
        if (te == null) return;
        IEssentiaTransport ic = (IEssentiaTransport) te;
        if (!ic.canOutputTo(face.getOpposite())) return;
        Aspect ta = null;
        if (this.aspectCurrent != null) {
            ta = this.aspectCurrent;
        } else if (ic.getEssentiaAmount(face.getOpposite()) > 0 && ic.getSuctionAmount(face.getOpposite()) < this.getSuctionAmount(face) && this.getSuctionAmount(face) >= ic.getMinimumSuction()) {
            ta = ic.getEssentiaType(face.getOpposite());
        }

        if (ta != null && ic.getSuctionAmount(face.getOpposite()) < this.getSuctionAmount(face)) {
            this.addToContainer(ta, ic.takeEssentia(ta, 1, face.getOpposite()));
        }
    }

    @Override
    public void readSyncNBT(NBTTagCompound nbt) {
        this.aspects.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        this.aspects.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public AspectList getAspects() {
        return this.aspects;
    }

    @Override
    public void setAspects(AspectList aspects) {
        this.aspects = aspects;
    }

    @Override
    public boolean doesContainerAccept(Aspect tag) {
        if (this.aspects.size() > MAX_ASPECT) {
            return this.aspects.getAmount(tag) > MAX_AMOUNT;
        }
        return true;
    }

    @Override
    public int addToContainer(Aspect tag, int amount) {
        if (amount != 0 && this.aspects.size() < MAX_ASPECT) {
            int currentAmount = this.aspects.getAmount(tag);
            if (currentAmount < MAX_AMOUNT) {
                int added = Math.min(amount, MAX_AMOUNT - currentAmount);
                this.aspects.add(tag, amount);
                amount -= added;
            }
            this.syncTile(false);
            this.markDirty();
        }
        return amount;
    }

    @Override
    public boolean takeFromContainer(Aspect tag, int amount) {
        if (this.aspects.getAmount(tag) >= amount) {
            this.aspects.remove(tag, amount);
            this.syncTile(false);
            this.markDirty();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean takeFromContainer(AspectList ot) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect tag, int amount) {
        return this.aspects.getAmount(tag) >= amount;
    }

    @Override
    public boolean doesContainerContain(AspectList ot) {
        return false;
    }

    @Override
    public int containerContains(Aspect tag) {
        return this.aspects.getAmount(tag);
    }

    @Override
    public boolean isConnectable(EnumFacing face) {
        return true;
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {
        this.aspectCurrent = aspect;
    }

    @Override
    public Aspect getSuctionType(EnumFacing face) {
        return this.aspectCurrent;
    }

    @Override
    public int getSuctionAmount(EnumFacing face) {
        return this.aspectCurrent != null ? SUCTION_AMOUNT : 0;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
        return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
        return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing face) {
        return this.aspectCurrent;
    }

    @Override
    public int getEssentiaAmount(EnumFacing face) {
        return Objects.nonNull(this.aspectCurrent) ? this.aspects.getAmount(this.aspectCurrent) : 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Nullable
    public MachineComponent<TileAspectListProvider> provideComponent() {
        return new MachineComponentAspectListProvider(this, IOType.INPUT);
    }

    @Override
    public boolean canInputFrom(EnumFacing face) {
        return true;
    }

    @Override
    public boolean canOutputTo(EnumFacing face) {
        return false;
    }

    @Override
    public boolean isBlocked() {
        return false;
    }
}
