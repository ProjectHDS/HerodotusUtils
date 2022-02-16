package youyihj.herodotusutils.modsupport.jei.render;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.alchemy.AlchemyEssenceStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author youyihj
 */
public class AlchemyEssenceRender implements IIngredientRenderer<AlchemyEssenceStack> {
    private final ResourceLocation runesTexture = HerodotusUtils.rl("textures/gui/jei/runes.png");

    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable AlchemyEssenceStack ingredient) {
        if (ingredient != null) {
            GlStateManager.enableAlpha();
            minecraft.getTextureManager().bindTexture(runesTexture);
            int index = ingredient.getEssence().getIndex();
            Gui.drawScaledCustomSizeModalRect(xPosition, yPosition, (index & 15) * 32, (index >> 4) * 32, 32, 32, 16, 16, 512, 512);
            if (ingredient.getCount() > 1) {
                getFontRenderer(minecraft, ingredient).drawString(String.valueOf(ingredient.getCount()), xPosition + 13, yPosition + 9, -1);
            }
        }
    }

    @Override
    public List<String> getTooltip(Minecraft minecraft, AlchemyEssenceStack ingredient, ITooltipFlag tooltipFlag) {
        return Collections.singletonList(ingredient.getDisplayName());
    }
}
