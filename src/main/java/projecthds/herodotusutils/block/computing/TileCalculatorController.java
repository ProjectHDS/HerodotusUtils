package projecthds.herodotusutils.block.computing;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import projecthds.herodotusutils.computing.IComputingUnitGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youyihj
 */
public class TileCalculatorController extends TileEntity implements ITickable, IComputingUnitGenerator {
    // TODO: replace to multiblocked way
//    private static final BlockArray MULTIBLOCK;
    private static final List<BlockPos> MODULE_POSES = new ArrayList<>();

//    static {
//        try {
//            Gson gson = new GsonBuilder().registerTypeAdapter(DynamicMachine.class, new DynamicMachine.MachineDeserializer()).create();
//            Reader reader = new InputStreamReader(new FileInputStream("config/hdsutils/calculator_controller.json"));
//            MULTIBLOCK = gson.fromJson(reader, DynamicMachine.class).getPattern();
//            MULTIBLOCK.getPattern().forEach((pos, block) -> {
//                if (block.matchesState(BlockComputingModule.INSTANCE.getDefaultState())) {
//                    MODULE_POSES.add(pos);
//                }
//            });
//        } catch (IOException e) {
//            throw new RuntimeException("cannot read controller parts", e);
//        }
//    }

    private int capacity;
    private boolean complete;

    @SuppressWarnings("unused")
    public TileCalculatorController() {
    }

    public TileCalculatorController(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.capacity = compound.getInteger("capacity");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("capacity", this.capacity);
        return super.writeToNBT(compound);
    }

    @Override
    public void update() {
        if (world.getTotalWorldTime() % 20 == 0) {
//            complete = MULTIBLOCK.matches(world, pos, false, null);
            generateToChunk(world, pos);
            updateStructure();
        }
    }

    @Override
    public int generateAmount() {
        if (!complete) {
            return 0;
        }
        return Math.min(capacity, MODULE_POSES.stream()
                .map(vec -> pos.add(vec))
                .map(posOffset -> world.getTileEntity(posOffset))
                .filter(TileComputingModule.class::isInstance)
                .map(TileComputingModule.class::cast)
                .mapToInt(TileComputingModule::getGeneratePower)
                .sum()
        );
    }

    public void setStructureInactivated() {
        complete = false;
        updateStructure();
    }

    private void updateStructure() {
//        Stream.concat(MULTIBLOCK.getPattern().keySet().stream(), Stream.of(BlockPos.ORIGIN)).forEach(pos -> {
//            BlockPos posOffset = this.pos.add(pos);
//            IBlockState state = world.getBlockState(posOffset);
//            Block block = state.getBlock();
//            if (block instanceof BlockCalculatorStructure) {
//                if (complete != state.getValue(BlockCalculatorStructure.ACTIVATED)) {
//                    world.setBlockState(posOffset, state.withProperty(BlockCalculatorStructure.ACTIVATED, complete));
//                }
//            }
//        });
    }
}
