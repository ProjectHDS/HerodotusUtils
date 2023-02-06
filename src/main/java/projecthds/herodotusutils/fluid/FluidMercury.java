package projecthds.herodotusutils.fluid;

import net.minecraftforge.fluids.Fluid;
import projecthds.herodotusutils.HerodotusUtils;

import java.awt.*;

/**
 * @author youyihj
 */
public class FluidMercury extends Fluid {
    private FluidMercury() {
        super("mercury", HerodotusUtils.rl("fluids/liquid"), HerodotusUtils.rl("fluids/liquid_flow"), new Color(0xc8c8c8));
    }

    public static final FluidMercury INSTANCE = new FluidMercury();
}
