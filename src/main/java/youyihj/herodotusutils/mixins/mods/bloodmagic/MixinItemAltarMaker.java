package youyihj.herodotusutils.mixins.mods.bloodmagic;

import WayofTime.bloodmagic.altar.AltarComponent;
import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.altar.IBloodAltar;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import youyihj.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;
import youyihj.herodotusutils.util.Util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
@Mixin(value = ItemAltarMaker.class, remap = false)
public class MixinItemAltarMaker {
    @Shadow
    private AltarTier tierToBuild;

    /**
     * @author youyihj
     * @reason build the new structure
     */
    @Overwrite
    public void buildAltar(World world, BlockPos pos) {
        if (world.isRemote)
            return;

        if (tierToBuild == AltarTier.ONE)
            return;

        BloodAltarStructures.STRUCTURES.get(tierToBuild).getPattern().forEach((offset, info) -> {
            BlockPos posOffset = pos.add(offset);
            world.setBlockState(posOffset, info.getSampleState());
        });

        Util.getTileEntity(world, pos, IBloodAltar.class).ifPresent(IBloodAltar::checkTier);
    }

    @Redirect(method = "destroyAltar", at = @At(value = "INVOKE", target = "LWayofTime/bloodmagic/altar/AltarTier;getAltarComponents()Ljava/util/List;"))
    public List<AltarComponent> getNewDummyComponents(AltarTier tier) {
        return BloodAltarStructures.STRUCTURES.get(tier).getPattern().keySet().stream()
                .map(pos -> new AltarComponent(pos, null))
                .collect(Collectors.toList());
    }
}
