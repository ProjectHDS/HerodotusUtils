package youyihj.herodotusutils.entity.golem;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author youyihj
 */
public enum Color {
    UNSET,
    RED,
    YELLOW,
    BLUE;

    @SideOnly(Side.CLIENT)
    public void applyToRenderSystem() {
        switch (this) {
            case RED:
                GlStateManager.color(1.0f, 0.0f, 0.0f);
                break;
            case YELLOW:
                GlStateManager.color(1.0f, 1.0f, 0.0f);
                break;
            case BLUE:
                GlStateManager.color(0.0f, 0.0f, 1.0f);
                break;
            case UNSET:
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                break;
        }
    }
}
