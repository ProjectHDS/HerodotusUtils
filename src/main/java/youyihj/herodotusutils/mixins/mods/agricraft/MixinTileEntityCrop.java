package youyihj.herodotusutils.mixins.mods.agricraft;

import com.infinityraider.agricraft.tiles.TileEntityCrop;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author youyihj
 */
@Mixin(value = TileEntityCrop.class, remap = false)
public abstract class MixinTileEntityCrop extends TileEntityBase {

    @Inject(
            method = "setGrowthStage",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/infinityraider/agricraft/tiles/TileEntityCrop;growthStage:I",
                    opcode = Opcodes.PUTFIELD
            ))
    private void sendBlockUpdate(int stage, CallbackInfoReturnable<Boolean> cir) {
        IBlockState blockState = getState();
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        world.markAndNotifyBlock(pos, chunk, blockState, blockState, Constants.BlockFlags.DEFAULT);
    }
}
