package projecthds.herodotusutils.mixins.mods.bloodmagic;

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
import projecthds.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;
import projecthds.herodotusutils.util.Util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
@Mixin(ItemAltarMaker.class)
public class MixinItemAltarMaker {
    @Shadow(remap = false)
    private AltarTier tierToBuild;

    /**
     * @author youyihj
     * @reason build the new structure
     */
    @Overwrite(remap = false)
    public void buildAltar(World world, BlockPos pos) {
        if (world.isRemote)
            return;

        if (tierToBuild == AltarTier.ONE)
            return;

        BloodAltarStructures.STRUCTURES.get(tierToBuild).getElements().forEach((offset, info) -> {
            BlockPos posOffset = pos.add(offset);
            world.setBlockState(posOffset, info.getSampleBlock());
        });

        Util.getTileEntity(world, pos, IBloodAltar.class).ifPresent(IBloodAltar::checkTier);
    }

    @Redirect(method = "destroyAltar", at = @At(value = "INVOKE", target = "LWayofTime/bloodmagic/altar/AltarTier;getAltarComponents()Ljava/util/List;", remap = false), remap = false)
    public List<AltarComponent> getNewDummyComponents(AltarTier tier) {
        return BloodAltarStructures.STRUCTURES.get(tier).getElements().keySet().stream()
                .map(vec -> new AltarComponent(new BlockPos(vec), null))
                .collect(Collectors.toList());
    }
}
