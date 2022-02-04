package youyihj.herodotusutils.network.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.block.BlockCreatureDataAnalyzer;

/**
 * @author youyihj
 */
public class CreatureDataAnalyzerGui extends GuiContainer {
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(HerodotusUtils.MOD_ID, "textures/gui/single_item_device.png");

    public CreatureDataAnalyzerGui(CreatureDataAnalyzerContainer inventorySlotsIn) {
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

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        CreatureDataAnalyzerContainer container = (CreatureDataAnalyzerContainer) this.inventorySlots;
        this.drawCenteredString(this.fontRenderer, BlockCreatureDataAnalyzer.INSTANCE.getLocalizedName(), xSize / 2, 6, -1);
        String timeShowString = I18n.format("hdsutils.time", container.getTimer());
        String timeShowType = I18n.format("hdsutils.type", I18n.format("hdsutils.type." + container.getType()));
        this.drawCenteredString(this.fontRenderer, timeShowString, xSize / 2, 54, -1);
        this.drawCenteredString(this.fontRenderer, timeShowType, xSize / 2, 64, -1);
    }
}
