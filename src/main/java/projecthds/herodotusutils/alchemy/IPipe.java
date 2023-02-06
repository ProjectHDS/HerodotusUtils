package projecthds.herodotusutils.alchemy;

import projecthds.herodotusutils.block.alchemy.TileAlchemyController;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public interface IPipe {
    @Nullable
    TileAlchemyController getLinkedController();

    void setLinkedController(TileAlchemyController tileAlchemyController);

    default void unlinkController() {
        setLinkedController(null);
    }

    default void afterModuleMainWork() {
    }
}
