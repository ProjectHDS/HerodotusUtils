package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import projecthds.herodotusutils.block.BlockMercurySteam;

public class TileRedstoneAmalgam extends TileEntity implements ITickable {
    private int delayTimer = -1;
    private BlockPos fluidSpawnPos;

    public void startFluidGeneration(BlockPos pos) {
        this.delayTimer = 40;
        this.fluidSpawnPos = pos;
    }

    @Override
    public void update() {
        if (delayTimer > 0) {
            delayTimer--;
            if (delayTimer == 0) {
                if (fluidSpawnPos != null && world != null) {
                    world.setBlockState(fluidSpawnPos, BlockMercurySteam.INSTANCE.getDefaultState());
                }
                fluidSpawnPos = null;
            }
        }
    }
}