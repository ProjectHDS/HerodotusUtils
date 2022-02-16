package youyihj.herodotusutils.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import youyihj.herodotusutils.block.alchemy.TileAlchemyRoundRobinTunnel;

import java.awt.*;

/**
 * @author youyihj
 */
public class TileRoundRobinTunnelRender extends TileEntitySpecialRenderer<TileAlchemyRoundRobinTunnel> {
    private static final Color[] COLORS = new Color[] {
            new Color(234, 27, 57),
            new Color(139, 196, 63),
            new Color(251, 210, 8),
            new Color(81, 45, 141)
    };

    @Override
    public void render(TileAlchemyRoundRobinTunnel te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
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
        EnumFacing[] facingQuery = te.getFacingQuery();
        for (int i = 0; i < facingQuery.length; i++) {
            drawSign(facingQuery[i], i, buffer);
        }

        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.disablePolygonOffset();
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    private void drawSign(EnumFacing facing, int index, BufferBuilder buffer) {
        Color color = COLORS[index];
        double topHeight = 0.8125;
        if (facing == null) return;
        switch (facing) {
            case WEST:
                RenderUtils.drawSquareXZ(buffer, 0, 0.4375, 0.125, 0.5675, topHeight, color);
                break;
            case EAST:
                RenderUtils.drawSquareXZ(buffer, 0.875, 0.4375, 1, 0.5675, topHeight, color);
                break;
            case NORTH:
                RenderUtils.drawSquareXZ(buffer, 0.4375, 0, 0.5675, 0.125, topHeight, color);
                break;
            case SOUTH:
                RenderUtils.drawSquareXZ(buffer, 0.4375, 0.875, 0.5675, 1, topHeight, color);
        }
    }



}
