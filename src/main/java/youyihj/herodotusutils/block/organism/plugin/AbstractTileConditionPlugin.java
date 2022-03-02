package youyihj.herodotusutils.block.organism.plugin;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import youyihj.herodotusutils.organism.IConditionPlugin;
import youyihj.herodotusutils.util.Capabilities;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public abstract class AbstractTileConditionPlugin extends TileEntity {
    private final IConditionPlugin conditionPlugin;

    public AbstractTileConditionPlugin() {
        this.conditionPlugin = createConditionPlugin();
    }

    protected abstract IConditionPlugin createConditionPlugin();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == Capabilities.CONDITION_PLUGIN_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == Capabilities.CONDITION_PLUGIN_CAPABILITY) {
            return Capabilities.CONDITION_PLUGIN_CAPABILITY.cast(conditionPlugin);
        }
        return super.getCapability(capability, facing);
    }
}
