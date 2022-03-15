package youyihj.herodotusutils.mixins.mods.bloodmagic;

import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.tile.TileAltar;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import youyihj.herodotusutils.mixins.interfaces.IBloodAltarPatch;
import youyihj.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;

import java.util.Optional;

/**
 * @author youyihj
 */
@Mixin(value = BloodAltar.class, remap = false)
public class MixinBloodAltar implements IBloodAltarPatch {
    @Shadow
    private int internalCounter;
    @Shadow
    private TileAltar tileAltar;
    private AltarTier buildingTier;
    private int slice;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "LWayofTime/bloodmagic/altar/BloodAltar;updateAltar()V"))
    private void buildStructure(CallbackInfo ci) {
        if (buildingTier != null && internalCounter % 80 == 0) {
            World world = tileAltar.getWorld();
            BlockArray structure = BloodAltarStructures.STRUCTURES.get(buildingTier);
            structure.getPatternSlice(slice).forEach((position, information) -> {
                BlockPos offset = position.add(tileAltar.getPos());
                IBlockState prev = world.getBlockState(offset);
                if (!prev.getBlock().isAir(prev, world, offset)) {
                    NonNullList<ItemStack> items = NonNullList.create();
                    prev.getBlock().getDrops(items, world, offset, prev, 0);
                    world.setBlockToAir(offset);
                    items.forEach(it -> Block.spawnAsEntity(world, tileAltar.getPos(), it));
                }
                world.setBlockState(offset, information.getSampleState(Optional.of(0L)));
            });
            world.playSound(null, tileAltar.getPos(), SoundEvents.BLOCK_SLIME_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            if (slice == structure.getMax().getY()) {
                slice = 0;
                buildingTier = null;
            } else {
                slice++;
            }
        }
    }

    @Override
    public void setBuildingTier(AltarTier tier) {
        buildingTier = tier;
        slice = BloodAltarStructures.STRUCTURES.get(tier).getMin().getY();
    }

    @Override
    public boolean isBuilding() {
        return buildingTier != null;
    }
}
