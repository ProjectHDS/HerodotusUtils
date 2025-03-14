package projecthds.herodotusutils.mixins.mods.bloodmagic;

import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.client.render.block.RenderAltar;
import WayofTime.bloodmagic.tile.TileAltar;
// import hellfirepvp.astralsorcery.client.util.Blending;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import projecthds.herodotusutils.modsupport.bloodmagic.BloodAltarStructures;

/**
 * @author youyihj
 */
@Mixin(RenderAltar.class)
public class MixinRenderAltar extends TileEntitySpecialRenderer<TileAltar> {
    @Inject(method = "render(LWayofTime/bloodmagic/tile/TileAltar;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", shift = At.Shift.AFTER),cancellable = true)
    private void renderMissingBlocks(TileAltar tileAltar, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {
        if (tileAltar.getCurrentTierDisplayed() == AltarTier.ONE) return;
        World world = tileAltar.getWorld();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        // Blending.CONSTANT_ALPHA.applyStateManager();
        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.translate(x, y, z);
        GlStateManager.color(1F, 1F, 1F, 1f);
        BloodAltarStructures.STRUCTURES.get(tileAltar.getCurrentTierDisplayed()).getElements().forEach((vec, info) -> {
            BlockPos absolutePos = tileAltar.getPos().add(vec);
            IBlockState state = world.getBlockState(absolutePos);
            state = state.getActualState(world, absolutePos);
            if (state.getBlock().isAir(state, world, absolutePos)) {
                GlStateManager.pushMatrix();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                blockRendererDispatcher.renderBlock(info.getSampleBlock(), new BlockPos(vec), world, bufferBuilder);
                tessellator.draw();
                GlStateManager.popMatrix();
            }
        });
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        ci.cancel();
    }
}
