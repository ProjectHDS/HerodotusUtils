package youyihj.herodotusutils.block.organism;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import youyihj.herodotusutils.organism.LauncherManager;
import youyihj.herodotusutils.organism.StructureTier;

/**
 * @author youyihj
 */
public class TileORELauncher extends TileEntity implements ITickable {
    private StructureTier tier;
    private boolean structureComplete;

    public TileORELauncher(StructureTier tier) {
        this.tier = tier;
    }

    @SuppressWarnings("unused")
    public TileORELauncher() {}

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("tier", tier.ordinal());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.tier = StructureTier.values()[compound.getInteger("tier")];
        LauncherManager.putLauncher(this, world.getBlockState(pos).getValue(BlockHorizontal.FACING));
    }

    public StructureTier getTier() {
        return tier;
    }

    @Override
    public void onLoad() {
        LauncherManager.putLauncher(this, world.getBlockState(pos).getValue(BlockHorizontal.FACING));
    }

    @Override
    public void invalidate() {
        super.invalidate();
        LauncherManager.removeLauncher(this);
    }

    @Override
    public void update() {
        if (!structureComplete && world.getTotalWorldTime() % 40 == 0) {
            checkStructure();
        }
    }

    public void checkStructure() {
        AxisAlignedBB boundary = LauncherManager.getBoundary(this);
        AxisAlignedBB internal = boundary.shrink(1);
        internal = internal.grow(0.1);
        Iterable<BlockPos> box = BlockPos.getAllInBox((int) boundary.minX, (int) boundary.minY, (int) boundary.minZ, (int) boundary.maxX, (int) boundary.maxY, (int) boundary.maxZ);
        boundary = boundary.grow(0.1);
        for (BlockPos pos : box) {
            Vec3d vecPos = new Vec3d(pos);
            boolean isInternal = internal.contains(vecPos);
            boolean isBoundary = boundary.contains(vecPos);
            if (!isInternal && isBoundary) {
                Block block = world.getBlockState(pos).getBlock();
                if (!(block instanceof BlockStructure) || ((BlockStructure) block).getTier() != tier) {
                    structureComplete = false;
                    return;
                }
            }
        }
        structureComplete = true;
    }
}
