package projecthds.herodotusutils.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 * @author youyihj
 */
public class HorizontalBlockBoundingBoxes {
    private final AxisAlignedBB base;
    private final AxisAlignedBB[] boxes = new AxisAlignedBB[4];

    public HorizontalBlockBoundingBoxes(AxisAlignedBB base) {
        this.base = base;
        for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
            boxes[horizontal.getHorizontalIndex()] = rotateBlockBoundingBox(base, horizontal);
        }
    }

    public static HorizontalBlockBoundingBoxes of(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new HorizontalBlockBoundingBoxes(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
    }

    public static HorizontalBlockBoundingBoxes ofModelPos(double x1, double y1, double z1, double x2, double y2, double z2) {
        return of(x1 / 16, y1 / 16, z1 / 16, x2 / 16, y2 / 16, z2 / 16);
    }

    public AxisAlignedBB getBase() {
        return base;
    }

    public AxisAlignedBB getBoundingBox(EnumFacing facing) {
        return boxes[facing.getHorizontalIndex()];
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IProperty<EnumFacing> property) {
        return getBoundingBox(state.getValue(property));
    }

    private static AxisAlignedBB rotateBlockBoundingBox(AxisAlignedBB aabb, EnumFacing facing) {
        Vec3d minVec = new Vec3d(aabb.minX, aabb.minY, aabb.minZ);
        Vec3d maxVec = new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ);
        final Vec3d center = new Vec3d(0.5, 0.5, 0.5);
        EnumFacing currentFacing = EnumFacing.NORTH;
        while (currentFacing != facing) {
            currentFacing = currentFacing.rotateYCCW();
            minVec = rotateYCCW(minVec, center);
            maxVec = rotateYCCW(maxVec, center);
        }
        return new AxisAlignedBB(minVec.x, minVec.y, minVec.z, maxVec.x, maxVec.y, maxVec.z);
    }

    private static Vec3d rotateYCCW(Vec3d toRotate, Vec3d center) {
        double centerX = center.x;
        double centerZ = center.z;
        double a = toRotate.x - centerX;
        double b = toRotate.z - centerZ;
        return new Vec3d(b + centerX, toRotate.y, -a + centerZ);
    }
}
