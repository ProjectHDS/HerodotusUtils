package youyihj.herodotusutils.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import stanhebben.zenscript.util.StringUtil;
import youyihj.herodotusutils.block.TileGolemCore;
import youyihj.herodotusutils.client.ClientEventHandler;
import youyihj.herodotusutils.entity.golem.Color;
import youyihj.herodotusutils.entity.golem.Shape;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youyihj
 */
public class TileGolemCoreRender extends TileEntitySpecialRenderer<TileGolemCore> {

    private final Map<Pair<Color, Shape>, ItemStack> cacheCores = new HashMap<>();

    @Override
    public void render(TileGolemCore te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        Pair<Color, Shape> pair = Pair.of(te.getColor(), te.getShape());
        ItemStack item = cacheCores.get(pair);
        if (item == null) {
            NonNullList<ItemStack> ores = OreDictionary.getOres(te.getShape().name().toLowerCase() + "TierThree" + StringUtil.capitalize(te.getColor().name().toLowerCase()));
            item = ores.isEmpty() ? ItemStack.EMPTY : ores.get(0);
            cacheCores.put(pair, item);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        GlStateManager.rotate((ClientEventHandler.ticks * 2.5f) % 360, 0, 1, 0);
        Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.scale(0.9, 0.9, 0.9);
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }
}
