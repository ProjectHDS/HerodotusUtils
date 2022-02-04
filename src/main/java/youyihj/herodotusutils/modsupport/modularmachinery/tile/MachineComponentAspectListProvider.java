package youyihj.herodotusutils.modsupport.modularmachinery.tile;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.component.ComponentAspectList;

/**
 * @author ikexing
 */
public class MachineComponentAspectListProvider extends MachineComponent<TileAspectListProvider> {

    private final TileAspectListProvider aspectListProvider;

    public MachineComponentAspectListProvider(TileAspectListProvider aspectListProvider, IOType ioType) {
        super(ioType);
        this.aspectListProvider = aspectListProvider;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentAspectList.INSTANCE;
    }

    @Override
    public TileAspectListProvider getContainerProvider() {
        return this.aspectListProvider;
    }
}
