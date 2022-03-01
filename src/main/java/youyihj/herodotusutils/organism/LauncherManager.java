package youyihj.herodotusutils.organism;

import hellfirepvp.modularmachinery.common.util.MiscUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import youyihj.herodotusutils.block.organism.TileORELauncher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youyihj
 */
public class LauncherManager {
    private static final Map<TileORELauncher, AxisAlignedBB> launchers = new HashMap<>();

    public static void putLauncher(TileORELauncher launcher, EnumFacing facing) {
        BlockPos pos = launcher.getPos();
        int size = launcher.getTier().getSize();
        BlockPos first = pos.add(MiscUtils.rotateYCCWNorthUntil(new BlockPos(-size / 2, -1, 0), facing));
        BlockPos second = pos.add(MiscUtils.rotateYCCWNorthUntil(new BlockPos(size / 2, size - 2, size - 1), facing));
        launchers.put(launcher, new AxisAlignedBB(first.getX(), first.getY(), first.getZ(), second.getX(), second.getY(), second.getZ()));
    }

    public static AxisAlignedBB getBoundary(TileORELauncher launcher) {
        return launchers.get(launcher);
    }

    public static void removeLauncher(TileORELauncher launcher) {
        launchers.remove(launcher);
    }

    public static void onRefreshStructure(BlockPos pos) {
        for (Map.Entry<TileORELauncher, AxisAlignedBB> entry : launchers.entrySet()) {
            TileORELauncher launcher = entry.getKey();
            AxisAlignedBB aabb = entry.getValue();
            if (aabb.grow(0.1).contains(new Vec3d(pos))) {
                launcher.checkStructure();
                return;
            }
        }
    }
}
