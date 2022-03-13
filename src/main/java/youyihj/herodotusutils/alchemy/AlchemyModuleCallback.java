package youyihj.herodotusutils.alchemy;

import youyihj.herodotusutils.block.alchemy.TileAlchemyController;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class AlchemyModuleCallback implements IAlchemyModule {
    private final IAlchemyModule parent;

    public AlchemyModuleCallback(IAlchemyModule parent) {
        this.parent = parent;
    }

    @Override
    public void work() {
        parent.callBackWork();
    }

    @Nullable
    @Override
    public TileAlchemyController getLinkedController() {
        return parent.getLinkedController();
    }

    @Override
    public void setLinkedController(TileAlchemyController tileAlchemyController) {
        parent.setLinkedController(tileAlchemyController);
    }
}
