package projecthds.herodotusutils.mixins.mods.chisel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import team.chisel.client.handler.BlockSpeedHandler;
import team.chisel.common.config.Configurations;

@Mixin(value = BlockSpeedHandler.class, remap = false)
public class MixinChiselSpeedHandler {
    @Shadow
    private static MovementInput manualInputCheck;

    /**
     * @author Gary Bryson Luis Jr.
     * @reason Rework logic.
     * */
    @Overwrite
    public static void speedupPlayer(PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side.isClient() && event.player.onGround && event.player instanceof EntityPlayerSP) {
            if (manualInputCheck == null) {
                manualInputCheck = new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
            }
            EntityPlayerSP player = (EntityPlayerSP) event.player;
            IBlockState below = player.getEntityWorld().getBlockState(new BlockPos(player.posX, player.posY - (1 / 16D), player.posZ));

            if (below.getBlock().getRegistryName().toString().contains("chisel")) {
                manualInputCheck.updatePlayerMoveState();
                if ((manualInputCheck.moveForward != 0 || manualInputCheck.moveStrafe != 0) && !player.isInWater()) {
                    if (below.getBlock().toString().contains("concrete")) {
                        player.motionX *= Configurations.concreteVelocityMult;
                        player.motionZ *= Configurations.concreteVelocityMult;
                    } else {
                        player.motionX *= 2.0f;
                        player.motionZ *= 2.0f;
                    }
                }
            }
        }
    }
}
