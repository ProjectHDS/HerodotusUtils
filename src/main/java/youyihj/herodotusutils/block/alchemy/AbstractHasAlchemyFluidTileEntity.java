package youyihj.herodotusutils.block.alchemy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import youyihj.herodotusutils.alchemy.AlchemyFluid;
import youyihj.herodotusutils.alchemy.IHasAlchemyFluid;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public abstract class AbstractHasAlchemyFluidTileEntity extends AbstractPipeTileEntity implements IHasAlchemyFluid {
    protected AlchemyFluid content;
    private AlchemyFluid cachedContent;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("fluid")) {
            content = new AlchemyFluid();
            content.deserializeNBT(compound.getTagList("fluid", Constants.NBT.TAG_COMPOUND));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (content != null) {
            compound.setTag("fluid", content.serializeNBT());
        }
        return compound;
    }

    @Override
    @Nullable
    public AlchemyFluid getContainedFluid() {
        return content;
    }

    @Override
    public boolean handleInput(AlchemyFluid input, EnumFacing inputSide) {
        if (content == null && inputSide == inputSide()) {
            cachedContent = input;
            return true;
        }
        return false;
    }

    @Override
    public void afterModuleMainWork() {
        if (cachedContent != null && content == null) {
            content = cachedContent;
            this.markDirty();
            cachedContent = null;
        }
    }

    @Override
    public void emptyFluid() {
        content = null;
        this.markDirty();
    }
}
