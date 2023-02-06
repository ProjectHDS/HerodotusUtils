package projecthds.herodotusutils.computing;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import projecthds.herodotusutils.util.Capabilities;

/**
 * @author youyihj
 */
public interface IComputingUnitGenerator extends IComputingUnitInteract {
    int generateAmount();

    default void generateToChunk(World world, BlockPos pos) {
        Chunk chunk = world.getChunk(pos);
        chunk.getCapability(Capabilities.COMPUTING_UNIT_CAPABILITY, null).generatePower(generateAmount(), pos, chunk);
    }
}
