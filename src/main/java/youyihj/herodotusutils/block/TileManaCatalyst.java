package youyihj.herodotusutils.block;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import vazkii.botania.common.entity.EntityManaBurst;

/**
 * @author youyihj
 */
public class TileManaCatalyst extends TileEntity {

    public void summonBurst() {
        EnumFacing[] values = EnumFacing.values();
        world.spawnEntity(getBurst(values[world.rand.nextInt(values.length)]));
    }

    public EntityManaBurst getBurst(EnumFacing direction) {
        Vec3i directionVec = direction.getDirectionVec();
        EntityManaBurst burst = new EntityManaBurst(world);
        burst.setColor(0x4d3bb3);
        burst.setMana(1);
        burst.setStartingMana(1);
        burst.setMinManaLoss(120);
        burst.setManaLossPerTick(1F);
        burst.setGravity(0F);
        burst.setPosition(pos.getX() + 0.5 + directionVec.getX() * 0.51, pos.getY() + 0.5 + directionVec.getY() * 0.51, pos.getZ() + directionVec.getZ() * 0.51);
        burst.setMotion(directionVec.getX() * 0.5, directionVec.getY() * 0.5, directionVec.getZ() * 0.5);
        burst.setSourceLens(new ItemStack(BlockManaCatalyst.Item.INSTANCE));
        return burst;
    }


}
