package youyihj.herodotusutils.mixins.mods.modularmachinery;

import hellfirepvp.modularmachinery.common.machine.MachineLoader;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import youyihj.herodotusutils.modsupport.modularmachinery.multiblock.AlchemicalPipeBlockInformation;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author youyihj
 */
@Mixin(value = MachineLoader.class, remap = false)
public abstract class MixinMachineLoader {
    @Shadow
    public static Map<String, BlockArray.BlockInformation> variableContext;

    @Inject(method = "prepareContext", at = @At("RETURN"))
    private static void putPipeInformation(List<File> key, CallbackInfo ci) {
        variableContext.put("pipes_all", new AlchemicalPipeBlockInformation());
    }
}
