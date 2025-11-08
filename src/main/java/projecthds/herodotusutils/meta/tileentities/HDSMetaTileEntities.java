package projecthds.herodotusutils.meta.tileentities;

import net.minecraft.util.ResourceLocation;
import projecthds.herodotusutils.meta.tileentities.multi.MultiMachineCarver;

import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;

public class HDSMetaTileEntities {

    public static MultiMachineCarver CARVER;

    public static void init() {
        CARVER = registerMetaTileEntity(18900, new MultiMachineCarver(new ResourceLocation("hdsutils", "carver")));
    }

}
