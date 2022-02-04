package youyihj.herodotusutils.client;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler {
    public static long ticks = 0;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.side != Side.CLIENT) {
            return;
        }
        ticks++;
    }

}
