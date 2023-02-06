package projecthds.herodotusutils.computing.event;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import projecthds.herodotusutils.event.BaseEvent;
import projecthds.herodotusutils.computing.IComputingUnit;

/**
 * @author youyihj
 */
public class ComputingUnitChangeEvent extends BaseEvent {
    private final IComputingUnit computingUnit;
    private final Chunk chunk;

    public ComputingUnitChangeEvent(IComputingUnit computingUnit, Chunk chunk) {
        this.computingUnit = computingUnit;
        this.chunk = chunk;
    }

    public IComputingUnit getComputingUnit() {
        return computingUnit;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public World getWorld() {
        return getChunk().getWorld();
    }
}
