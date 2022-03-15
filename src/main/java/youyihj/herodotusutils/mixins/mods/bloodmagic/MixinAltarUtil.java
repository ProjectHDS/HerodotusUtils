package youyihj.herodotusutils.mixins.mods.bloodmagic;

import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.altar.AltarUpgrade;
import WayofTime.bloodmagic.altar.AltarUtil;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import com.google.common.collect.Maps;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import youyihj.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;


/**
 * @author youyihj
 */
@Mixin(value = AltarUtil.class, remap = false)
public class MixinAltarUtil {

    /**
     * @author youyihj
     * @reason edit the structure of altar
     */
    @Nonnull
    @Overwrite
    public static AltarTier getTier(World world, BlockPos pos) {
        AltarTier checkTier = AltarTier.ONE;
        for (AltarTier altarTier : AltarTier.values()) {
            if (altarTier == AltarTier.ONE) continue;
            BlockArray structure = BloodAltarStructures.STRUCTURES.get(altarTier);
            if (structure == null) continue;
            if (structure.matches(world, pos, false, null)) {
                checkTier = altarTier;
            } else break;
        }
        return checkTier;
    }

    /**
     * @author youyihj
     * @reason edit the structure of altar
     */
    @Nonnull
    @Overwrite
    public static AltarUpgrade getUpgrades(World world, BlockPos pos, AltarTier currentTier) {
        if (currentTier == AltarTier.ONE) {
            return new AltarUpgrade();
        }
        Collection<BlockPos> runePoses = Maps.filterValues(BloodAltarStructures.STRUCTURES.get(currentTier).getPattern(), blockArray -> blockArray.getSampleState(Optional.of(0L)).getBlock() == RegistrarBloodMagicBlocks.BLOOD_RUNE).keySet();
        AltarUpgrade upgrade = new AltarUpgrade();
        for (BlockPos runePos : runePoses) {
            BlockPos componentPos = pos.add(runePos);
            IBlockState state = world.getBlockState(componentPos);
            if (state.getBlock() instanceof BlockBloodRune)
                upgrade.upgrade(((BlockBloodRune) state.getBlock()).getBloodRune(world, componentPos, state));
        }
        return upgrade;
    }
}
