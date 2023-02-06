package projecthds.herodotusutils.block.alchemy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import projecthds.herodotusutils.alchemy.IAlchemyModule;
import projecthds.herodotusutils.alchemy.IPipe;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youyihj
 */
public class TileAlchemyController extends AbstractPipeTileEntity implements ITickable {
    /* package-private */ boolean lastRedstoneSignal = false;
    private boolean isFirstScan = true;
    private final Map<BlockPos, IPipe> pipes = new HashMap<>();

    @Override
    public void update() {
        if (world.isRemote)
            return;
        if (isFirstScan) {
            startScanPipes();
            isFirstScan = false;
        }
        work();
    }

    public void startScanPipes() {
        pipes.values().forEach(IPipe::unlinkController);
        pipes.clear();
        scanPipes(pos);
    }

    private void scanPipes(BlockPos pos) {
        boolean success = addPipe(pos);
        if (!success)
            return;
        for (EnumFacing enumFacing : EnumFacing.values()) {
            BlockPos offset = pos.offset(enumFacing);
            scanPipes(offset);
        }
    }

    private boolean addPipe(BlockPos pipePos) {
        TileEntity tileEntity = world.getTileEntity(pipePos);
        if (tileEntity instanceof IPipe) {
            IPipe pipe = (IPipe) tileEntity;
            if (pipe.getLinkedController() != this) {
                pipes.put(pipePos, pipe);
                pipe.setLinkedController(this);
                return true;
            }
        }
        return false;
    }

    private void work() {
        if (world.getBlockState(pos).getValue(BlockAlchemyController.WORK_TYPE_PROPERTY).shouldWork(world, pos, this)) {
            pipes.forEach((pos, pipe) -> {
                if (pipe instanceof IAlchemyModule) {
                    ((IAlchemyModule) pipe).work();
                }
            });
            pipes.values().forEach(IPipe::afterModuleMainWork);
        }
    }
}
