package youyihj.herodotusutils.mixins.mods.bloodmagic;

import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.altar.IBloodAltar;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.util.Utils;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import youyihj.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;
import youyihj.herodotusutils.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author youyihj
 */
@Mixin(BlockAltar.class)
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

    private Pair<BlockPos, IBlockState> getFirstMissingComponent(World world, BlockPos pos) {
        for (BlockArray blockArray : BloodAltarStructures.STRUCTURES.values()) {
            for (Map.Entry<BlockPos, BlockArray.BlockInformation> entry : blockArray.getPattern().entrySet()) {
                BlockPos offset = pos.add(entry.getKey());
                if (!entry.getValue().matches(world, offset, true)) {
                    return Pair.of(offset, entry.getValue().getSampleState());
                }
            }
        }
        return null;
    }
}
