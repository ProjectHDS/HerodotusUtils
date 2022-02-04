package youyihj.herodotusutils.computing;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import youyihj.herodotusutils.util.Capabilities;

/**
 * @author youyihj
 */
public interface IComputingUnitConsumer extends IComputingUnitInteract {
    int consumeAmount();

    default boolean consumeToChunk(World world, BlockPos pos) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        IComputingUnit computingUnit = chunk.getCapability(Capabilities.COMPUTING_UNIT_CAPABILITY, null);
        computingUnit.consumePower(consumeAmount(), pos, chunk);
        return computingUnit.canWork();
    }
}
