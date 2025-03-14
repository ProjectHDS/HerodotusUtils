package projecthds.herodotusutils.mixins.mods.bloodmagic;

import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.altar.AltarUpgrade;
import WayofTime.bloodmagic.altar.AltarUtil;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import projecthds.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;
import projecthds.herodotusutils.util.Multiblock;

import javax.annotation.Nonnull;
import java.util.Collection;


/**
 * @author youyihj
 */
@Mixin(AltarUtil.class)
public class MixinAltarUtil {

    /**
     * @author youyihj
     * @reason edit the structure of altar
     */
    @Nonnull
    @Overwrite(remap = false)
    public static AltarTier getTier(World world, BlockPos pos) {
        AltarTier checkTier = AltarTier.ONE;
        for (AltarTier altarTier : AltarTier.values()) {
            if (altarTier == AltarTier.ONE) continue;
            Multiblock multiblock = BloodAltarStructures.STRUCTURES.get(altarTier);
            if (multiblock == null) continue;
            if (multiblock.matches(world, pos, EnumFacing.NORTH)) {
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
    @Overwrite(remap = false)
    public static AltarUpgrade getUpgrades(World world, BlockPos pos, AltarTier currentTier) {
        if (currentTier == AltarTier.ONE) {
            return new AltarUpgrade();
        }
        Collection<Vec3i> runePoses = Maps.filterValues(BloodAltarStructures.STRUCTURES.get(currentTier).getElements(), blockArray -> blockArray.getSampleBlock().getBlock() == RegistrarBloodMagicBlocks.BLOOD_RUNE).keySet();
        AltarUpgrade upgrade = new AltarUpgrade();
        for (Vec3i runePos : runePoses) {
            BlockPos componentPos = pos.add(runePos);
            IBlockState state = world.getBlockState(componentPos);
            if (state.getBlock() instanceof BlockBloodRune)
                upgrade.upgrade(((BlockBloodRune) state.getBlock()).getBloodRune(world, componentPos, state));
        }
        return upgrade;
    }
}
