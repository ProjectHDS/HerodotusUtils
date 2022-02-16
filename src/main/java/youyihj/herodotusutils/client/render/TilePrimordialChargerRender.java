package youyihj.herodotusutils.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.MathHelper;
import youyihj.herodotusutils.block.TilePrimordialCharger;
import youyihj.herodotusutils.client.ClientEventHandler;

/**
 * @author youyihj
 */
public class TilePrimordialChargerRender extends TileEntitySpecialRenderer<TilePrimordialCharger> {

    @Override
    public void render(TilePrimordialCharger te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 1.32 + MathHelper.sin(ClientEventHandler.ticks / 25.0f) * 0.15, z + 0.5);
        GlStateManager.rotate((ClientEventHandler.ticks * 2.5f) % 360, 0, 1, 0);
        GlStateManager.scale(0.9, 0.9, 0.9);
        Minecraft.getMinecraft().getRenderItem().renderItem(te.getContent(), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }
}
