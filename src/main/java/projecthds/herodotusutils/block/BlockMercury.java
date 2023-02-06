package projecthds.herodotusutils.block;

import crafttweaker.api.data.DataInt;
import crafttweaker.api.data.IData;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import projecthds.herodotusutils.fluid.FluidMercury;
import projecthds.herodotusutils.util.Util;
import youyihj.zenutils.api.world.ZenUtilsWorld;

import java.util.Random;

/**
 * @author youyihj
 */
public class BlockMercury extends BlockFluidClassic {
    private BlockMercury() {
        super(FluidMercury.INSTANCE, Material.WATER);
        this.setTickRandomly(true);
        this.setRegistryName(FluidMercury.INSTANCE.getName());
    }

    public static final String TAG_POLLUTION = "mercury_polluted";
    public static final BlockMercury INSTANCE = new BlockMercury();

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if (!worldIn.isRemote) {
            worldIn.setBlockToAir(pos);
            IData toUpdateData = Util.createDataMap(TAG_POLLUTION, new DataInt(1));
            ZenUtilsWorld.updateCustomChunkData(CraftTweakerMC.getIWorld(worldIn), toUpdateData, CraftTweakerMC.getIBlockPos(pos));
        }
    }
}
