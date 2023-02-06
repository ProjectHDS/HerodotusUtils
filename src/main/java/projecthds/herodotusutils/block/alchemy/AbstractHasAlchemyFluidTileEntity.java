package projecthds.herodotusutils.block.alchemy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import projecthds.herodotusutils.alchemy.*;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public abstract class AbstractHasAlchemyFluidTileEntity extends AbstractPipeTileEntity implements IHasAlchemyFluid {
    protected AlchemyFluid content;
    private AlchemyFluid cachedContent;
    private AlchemyModuleCallback emptyCallback;

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
    public InputResult handleInput(AlchemyFluid input, EnumFacing inputSide) {
        if (inputSide != inputSide()) {
            return InputResult.WRONG_SIDE;
        } else if (content != null) {
            return InputResult.BLOCK;
        } else {
            cachedContent = input;
            return InputResult.SUCCESS;
        }
    }

    @Override
    public void afterModuleMainWork() {
        if (cachedContent != null && content == null) {
            content = cachedContent;
            this.markDirty();
            cachedContent = null;
        }
        emptyCallback = null;
    }

    @Override
    public void emptyFluid() {
        content = null;
        if (emptyCallback != null) {
            emptyCallback.work();
        }
        this.markDirty();
    }

    @Override
    public void setEmptyCallback(AlchemyModuleCallback emptyCallback) {
        this.emptyCallback = emptyCallback;
    }
}
