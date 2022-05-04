package youyihj.herodotusutils.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.util.StringUtil;
import youyihj.herodotusutils.block.BlockGolemCore;
import youyihj.herodotusutils.client.ClientEventHandler;
import youyihj.herodotusutils.entity.golem.Color;
import youyihj.herodotusutils.entity.golem.Shape;

/**
 * @author youyihj
 */
public class TileGolemCoreItemRender extends TileEntityItemStackRenderer {
    private final BlockGolemCore.Item item;
    private ItemStack cacheCore;
    private IBakedModel model;
    public TileGolemCoreItemRender(BlockGolemCore.Item item) {
        this.item = item;
    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        Block block = item.getBlock();
        if (model == null) {
            model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(block.getDefaultState());
        }
        if (cacheCore == null) {
            BlockGolemCore block1 = (BlockGolemCore) block;
            Color color = block1.getColor();
            Shape shape = block1.getShape();
            NonNullList<ItemStack> ores = OreDictionary.getOres(shape.name().toLowerCase() + "Primordial" + StringUtil.capitalize(color.name().toLowerCase()));
            cacheCore = ores.isEmpty() ? ItemStack.EMPTY : ores.get(0);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0.5, 0.5);
        Minecraft.getMinecraft().getRenderItem().renderItem(itemStackIn, model);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.rotate((ClientEventHandler.ticks * 2.5f) % 360, 0, 1, 0);
        Minecraft.getMinecraft().getRenderItem().renderItem(cacheCore, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.scale(0.9, 0.9, 0.9);
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }
}
