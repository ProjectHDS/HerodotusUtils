package projecthds.herodotusutils.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import projecthds.herodotusutils.block.alchemy.TileAlchemyLazyTunnel;

import java.awt.*;

/**
 * @author youyihj
 */
public class TileLazyTunnelRender extends TileEntitySpecialRenderer<TileAlchemyLazyTunnel> {

    @Override
    public void render(TileAlchemyLazyTunnel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1, -20);

        Minecraft.getMinecraft().getTextureManager().bindTexture(RenderUtils.BLANK_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        double topHeight = 0.875;
//        switch (te.outputSide().getAxis()) {
//            case X:
//                RenderUtils.drawSquareXZ(buffer, 0.4375, 0.3125, 0.5625, 0.6875, topHeight, Color.WHITE);
//                break;
//            case Z:
//                RenderUtils.drawSquareXZ(buffer, 0.3125, 0.4375, 0.6875, 0.5625, topHeight, Color.WHITE);
//                break;
//        }
        double length = 0.375 / te.getBound() * (te.getCounter() + 1);
        switch (te.outputSide()) {
            case NORTH:
                RenderUtils.drawSquareXZ(buffer, 0.3125, 0.4375, 0.3125 + length, 0.5625, topHeight, Color.WHITE);
                break;
            case SOUTH:
                RenderUtils.drawSquareXZ(buffer, 0.6875 - length, 0.4375, 0.6875, 0.5625, topHeight, Color.WHITE);
                break;
            case EAST:
                RenderUtils.drawSquareXZ(buffer, 0.4375, 0.3125, 0.5625, 0.3125 + length, topHeight, Color.WHITE);
                break;
            case WEST:
                RenderUtils.drawSquareXZ(buffer, 0.4375, 0.6875 - length, 0.5625, 0.6875, topHeight, Color.WHITE);
                break;
        }

        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.disablePolygonOffset();
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }
}
