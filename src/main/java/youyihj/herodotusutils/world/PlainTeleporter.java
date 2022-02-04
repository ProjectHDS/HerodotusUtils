package youyihj.herodotusutils.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

/**
 * @author youyihj
 */
public class PlainTeleporter implements ITeleporter {
    private final double x;
    private final double y;
    private final double z;

    public PlainTeleporter(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        entity.setLocationAndAngles(x, y, z, yaw, entity.rotationPitch);
    }
}
