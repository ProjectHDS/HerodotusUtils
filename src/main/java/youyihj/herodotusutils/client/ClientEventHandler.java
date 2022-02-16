package youyihj.herodotusutils.client;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import youyihj.herodotusutils.alchemy.AlchemyFluid;
import youyihj.herodotusutils.recipe.AlchemyRecipes;

import java.util.Optional;

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


    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Optional.of(event.getItemStack())
                .map(FluidUtil::getFluidContained)
                .map(FluidStack::getFluid)
                .map(AlchemyRecipes::normalToAlchemy)
                .map(AlchemyFluid::getDisplayName)
                .ifPresent(event.getToolTip()::add);
    }

}
