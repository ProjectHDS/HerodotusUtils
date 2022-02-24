package youyihj.herodotusutils.network.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import youyihj.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
public class ORELauncherGui extends GuiContainer {
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(HerodotusUtils.MOD_ID, "textures/gui/organism_launcher.png");

    public ORELauncherGui(ORELauncherContainer inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 176;
        this.ySize = 176;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int left = (this.width - this.xSize) / 2;
        int top = (this.height - this.ySize) / 2;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(DEFAULT_TEXTURE);
        this.drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }
}
