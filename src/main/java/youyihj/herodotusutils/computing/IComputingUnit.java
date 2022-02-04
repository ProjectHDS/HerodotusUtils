package youyihj.herodotusutils.computing;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;
import youyihj.herodotusutils.computing.event.ComputingUnitChangeEvent;
import youyihj.herodotusutils.util.Util;

/**
 * @author youyihj
 */
public interface IComputingUnit {
    int totalGeneratePower();

    int totalConsumePower();

    void generatePower(int value, BlockPos pos, Chunk chunk);

    void consumePower(int value, BlockPos pos, Chunk chunk);

    boolean canWork();

    void removeInvalidEntry(IBlockAccess world);

    class Impl implements IComputingUnit {
        private final Object2IntArrayMap<BlockPos> generateDevices = new Object2IntArrayMap<>();
        private final Object2IntArrayMap<BlockPos> consumeDevices = new Object2IntArrayMap<>();

        @Override
        public int totalGeneratePower() {
            return Util.sumFastIntCollection(generateDevices.values());
        }

        @Override
        public int totalConsumePower() {
            return Util.sumFastIntCollection(consumeDevices.values());
        }

        @Override
        public void generatePower(int value, BlockPos pos, Chunk chunk) {
            generateDevices.put(pos, value);
            new ComputingUnitChangeEvent(this, chunk).post();
        }

        @Override
        public void consumePower(int value, BlockPos pos, Chunk chunk) {
            consumeDevices.put(pos, value);
            new ComputingUnitChangeEvent(this, chunk).post();
        }

        @Override
        public boolean canWork() {
            return this.totalGeneratePower() >= this.totalConsumePower();
        }

        @Override
        public void removeInvalidEntry(IBlockAccess world) {
            generateDevices.object2IntEntrySet().removeIf(entry -> !(world.getTileEntity(entry.getKey()) instanceof IComputingUnitGenerator));
            consumeDevices.object2IntEntrySet().removeIf(entry -> !(world.getTileEntity(entry.getKey()) instanceof IComputingUnitConsumer));
        }
    }
}
