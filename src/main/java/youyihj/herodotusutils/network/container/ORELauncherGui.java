package youyihj.herodotusutils.network.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import youyihj.herodotusutils.HerodotusUtils;

import java.util.Locale;

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

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        ORELauncherContainer container = (ORELauncherContainer) this.inventorySlots;
        String statusKey = container.isComplete() ? container.getStatus().name().toLowerCase(Locale.ENGLISH) : "structure";
        String prefix = "hdsutils.organism.status";
        this.fontRenderer.drawString(I18n.format(prefix, I18n.format(prefix + "." + statusKey)), 13, 14, -1);
        container.getClientCondition().getMap().forEach((type, value) -> {
            String message = I18n.format(type.getLocalizationKey()) + ": " + value;
            this.fontRenderer.drawString(message, 13 + type.getId() % 2 * 80, 28 + type.getId() / 2 * 14, -1);
        });
        this.mc.getTextureManager().bindTexture(DEFAULT_TEXTURE);
        double process = ((double) container.getTimer()) / ((double) container.getRequiredTime());
        int length = 160;
        int renderLength = ((int) (process * length));
        this.drawTexturedModalRect(8, 80,0, 176, renderLength, 4);
    }
}
