package youyihj.herodotusutils.fluid;

import net.minecraftforge.fluids.Fluid;
import youyihj.herodotusutils.HerodotusUtils;

import java.awt.*;

/**
 * @author youyihj
 */
public class FluidAlchemyWaste extends Fluid {
    private FluidAlchemyWaste() {
        super("fluid_alchemy_waste", HerodotusUtils.rl("fluids/liquid"), HerodotusUtils.rl("fluids/liquid_flow"), new Color(0x444444));
    }

    public static final FluidAlchemyWaste INSTANCE = new FluidAlchemyWaste();
}
