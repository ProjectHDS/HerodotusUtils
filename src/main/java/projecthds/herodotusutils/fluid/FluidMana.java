package projecthds.herodotusutils.fluid;

import net.minecraftforge.fluids.Fluid;
import vazkii.botania.common.block.tile.mana.TilePool;
import projecthds.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
public class FluidMana extends Fluid {
    private FluidMana() {
        super("fluid_mana", HerodotusUtils.rl("fluids/liquid"), HerodotusUtils.rl("fluids/liquid_flow"), TilePool.PARTICLE_COLOR);
    }

    public static final FluidMana INSTANCE = new FluidMana();
}
