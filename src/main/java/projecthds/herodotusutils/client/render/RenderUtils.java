package projecthds.herodotusutils.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import projecthds.herodotusutils.HerodotusUtils;

import java.awt.*;

/**
 * @author youyihj
 */
public class RenderUtils {
    public static final ResourceLocation BLANK_TEXTURE = HerodotusUtils.rl("textures/blocks/blank.png");

    public static void drawSquareXZ(BufferBuilder buffer, double minX, double minZ, double maxX, double maxZ, double y, Color color) {
        setColor(buffer.pos(minX, y, minZ).tex(0, 0), color).endVertex();
        setColor(buffer.pos(minX, y, maxZ).tex(0, 1), color).endVertex();
        setColor(buffer.pos(maxX, y, maxZ).tex(1, 1), color).endVertex();
        setColor(buffer.pos(maxX, y, minZ).tex(1, 0), color).endVertex();
    }

    public static BufferBuilder setColor(BufferBuilder bufferBuilder, Color color) {
        return bufferBuilder.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}
