package projecthds.herodotusutils.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import projecthds.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
public enum NetworkHandler {
    INSTANCE;

    private final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(HerodotusUtils.MOD_ID);
    private int packetId = 0;

    {
        channel.registerMessage(TaintSyncMessage.Handler.class, TaintSyncMessage.class, nextID(), Side.CLIENT);
        channel.registerMessage(ParticleEffectPacket.Handler.class, ParticleEffectPacket.class, nextID(), Side.CLIENT);
    }

    private int nextID() {
        return packetId++;
    }

    public void sendMessageToPlayer(IMessage msg, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            channel.sendTo(msg, ((EntityPlayerMP) player));
        }
    }
    public void sendToAllAround(IMessage message, int dimension, BlockPos pos, double range) {
        NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(
                dimension, pos.getX(), pos.getY(), pos.getZ(), range);
        channel.sendToAllAround(message, targetPoint);
    }
}
