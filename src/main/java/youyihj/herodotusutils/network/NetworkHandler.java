package youyihj.herodotusutils.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import youyihj.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
public enum NetworkHandler {
    INSTANCE;

    private final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(HerodotusUtils.MOD_ID);

    {
        channel.registerMessage(TaintSyncMessage.Handler.class, TaintSyncMessage.class, 0, Side.CLIENT);
    }

    public void sendMessageToPlayer(IMessage msg, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            channel.sendTo(msg, ((EntityPlayerMP) player));
        }
    }
}
