package youyihj.herodotusutils.mixins.mods.bloodmagic;

import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.altar.IBloodAltar;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.util.Utils;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import youyihj.herodotusutils.mixins.interfaces.IBloodAltarPatch;
import youyihj.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;
import youyihj.herodotusutils.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author youyihj
 */
@Mixin(value = BlockAltar.class, remap = false)
public class MixinBlockAltar {
    /**
     * @author youyihj
     * @reason to edit the enum stuff
     */
    @Overwrite
    public List<ITextComponent> getDocumentation(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
        List<ITextComponent> docs = new ArrayList<>();
        Util.getTileEntity(world, pos, IBloodAltar.class).ifPresent(altar -> {
            if (altar.getTier().toInt() >= AltarTier.MAXTIERS) return;
            Pair<BlockPos, IBlockState> missingComponent = getFirstMissingComponent(world, pos);
            if (missingComponent == null) return;
            BlockPos missingPos = missingComponent.getLeft();
            IBlockState blockState = missingComponent.getRight();
            docs.add(new TextComponentTranslation("chat.bloodmagic.altar.nextTier", blockState.getBlock().getLocalizedName(), Utils.prettifyBlockPosString(missingPos)));
        });
        return docs;
    }

    @Inject(method = "onBlockActivated", at = @At("HEAD"))
    private void setBuildingAltar(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        AltarTier buildingAltar = null;
        Optional<IBloodAltarPatch> altar = Util.getCapability(world, pos, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null) // gets the internal blood altar
                .filter(IBloodAltarPatch.class::isInstance)
                .map(IBloodAltarPatch.class::cast);
        if (!altar.isPresent()) return;
        IBloodAltarPatch altarPatch = altar.get();
        if (!world.isRemote && player.isSneaking() && !altarPatch.isBuilding()) {
            Optional<IItemHandler> inventory = Util.getCapability(world, pos.up(), CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
            if (inventory.isPresent()) {
                IItemHandler itemHandler = inventory.get();
                AltarTier[] tiers = AltarTier.values();
                ArrayUtils.reverse(tiers);
                for (AltarTier tier : tiers) {
                    if (tier == AltarTier.ONE) return;
                    List<ItemStack> items = BloodAltarStructures.STRUCTURE_ITEMS.get(tier);
                    if (items == null) continue;
                    if (Util.extractItems(itemHandler, items, true)) {
                        buildingAltar = tier;
                        break;
                    }
                }
                if (buildingAltar != null) {
                    List<ItemStack> items = BloodAltarStructures.STRUCTURE_ITEMS.get(buildingAltar);
                    Util.extractItems(itemHandler, items, false);
                    altarPatch.setBuildingTier(buildingAltar);
                }
            }
        }
    }

    private Pair<BlockPos, IBlockState> getFirstMissingComponent(World world, BlockPos pos) {
        for (BlockArray blockArray : BloodAltarStructures.STRUCTURES.values()) {
            for (Map.Entry<BlockPos, BlockArray.BlockInformation> entry : blockArray.getPattern().entrySet()) {
                BlockPos offset = pos.add(entry.getKey());
                if (!entry.getValue().matches(world, offset, true)) {
                    return Pair.of(offset, entry.getValue().getSampleState(Optional.of(0L)));
                }
            }
        }
        return null;
    }
}
