package projecthds.herodotusutils.fluid;

import net.minecraftforge.fluids.Fluid;
import projecthds.herodotusutils.HerodotusUtils;

import java.awt.*;

public class FluidMercurySteam extends Fluid {
    private FluidMercurySteam() {
        super("mercury_steam", HerodotusUtils.rl("fluids/liquid"), HerodotusUtils.rl("fluids/liquid_flow"), new Color(0xc8c8c8));
        setDensity(0);
    }

    public static final FluidMercurySteam INSTANCE = new FluidMercurySteam();
}
