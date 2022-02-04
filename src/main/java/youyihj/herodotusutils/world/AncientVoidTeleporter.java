package youyihj.herodotusutils.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import youyihj.herodotusutils.proxy.CommonProxy;

/**
 * @author youyihj
 */
public class AncientVoidTeleporter implements ITeleporter {
    private final BlockPos initialPos;

    public AncientVoidTeleporter(BlockPos initialPos) {
        this.initialPos = initialPos;
    }

    public static void teleport(Entity entity, BlockPos initialPos) {
        final String tagX = "TeleportRiftPosX";
        final String tagY = "TeleportRiftPosY";
        final String tagZ = "TeleportRiftPosZ";
        NBTTagCompound nbt = entity.getEntityData();
        if (!nbt.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            nbt.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        }
        NBTTagCompound persistedData = nbt.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (persistedData.hasKey(tagX) && persistedData.hasKey(tagY) && persistedData.hasKey(tagZ)) {
            initialPos = new BlockPos(persistedData.getInteger(tagX), persistedData.getInteger(tagY), persistedData.getInteger(tagZ));
        }
        persistedData.setInteger(tagX, initialPos.getX());
        persistedData.setInteger(tagY, initialPos.getY());
        persistedData.setInteger(tagZ, initialPos.getZ());
        entity.changeDimension(CommonProxy.ANCIENT_VOID_DIMENSION_ID, new AncientVoidTeleporter(initialPos));
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        BlockPos highestBlock = TileEntityEndGateway.findHighestBlock(world, initialPos, 64, false);
        BlockPos.MutableBlockPos topPos = new BlockPos.MutableBlockPos(highestBlock);
        topPos.setY(255);
        entity.getEntityData().setBoolean("DisableFallingDamage", true);
        entity.moveToBlockPosAndAngles(topPos, yaw, entity.rotationPitch);
    }
}
