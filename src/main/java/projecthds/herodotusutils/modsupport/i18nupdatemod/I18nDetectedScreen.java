package projecthds.herodotusutils.modsupport.i18nupdatemod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiErrorBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class I18nDetectedScreen extends GuiErrorBase {

    public final List<String> messages = new ArrayList<String>() {
        {
            add(I18n.format("hdsutils.tips.i18ndetected.0"));
            add(I18n.format("hdsutils.tips.i18ndetected.1"));
            add(I18n.format("hdsutils.tips.i18ndetected.2"));
        }
    };
    public int textHeight;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int i = this.height / 2 - this.textHeight / 2;
        for (String s : this.messages) {
            this.drawCenteredString(this.fontRenderer, s, this.width / 2, i, 16777215);
            i += this.fontRenderer.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
