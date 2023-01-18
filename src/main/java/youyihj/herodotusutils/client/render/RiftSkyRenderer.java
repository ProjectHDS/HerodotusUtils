package youyihj.herodotusutils.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;
import youyihj.herodotusutils.client.ClientEventHandler;

import java.util.Random;

public class RiftSkyRenderer extends IRenderHandler {

    /**
     * SETTINGS FOR RENDERER
     */
    private static final int STAR_NUMBERS = 800;
    private static final float MOVE_SPEED = 2.0F;
    private static final float STAR_SIZE = 1.0F;

    private static final float SKY_BG_RED = 0.01F;
    private static final float SKY_BG_GREEN = 0.02F;
    private static final float SKY_BG_BLUE = 0.04F;


//    private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");


    /**
     * VBO if possible
     */
    private final VertexBuffer[] skyNoiseVBOs;
    /**
     * The sky noise GL Call list
     */
    private final int[] skyNoiseGLCallLists;
    private final boolean vboEnabled;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final VertexFormat vertexBufferFormat;

    public RiftSkyRenderer() {
        //15 'Stars' Layers
        skyNoiseVBOs = new VertexBuffer[15];
        skyNoiseGLCallLists = new int[15];
        this.vboEnabled = OpenGlHelper.useVbo();
        this.vertexBufferFormat = mc.renderGlobal.vertexBufferFormat;

        for (int i = 0; i < 15; i++) {
            generateSkyNoise(i);
        }
    }


    private void generateSkyNoise(int index) {
        //Initialize
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        //Remove old renders
        if (this.skyNoiseVBOs[index] != null) {
            this.skyNoiseVBOs[index].deleteGlBuffers();
        }
        if (this.skyNoiseGLCallLists[index] >= 0) {
            GLAllocation.deleteDisplayLists(this.skyNoiseGLCallLists[index]);
            this.skyNoiseGLCallLists[index] = -1;
        }


        if (this.vboEnabled) {
            this.skyNoiseVBOs[index] = new VertexBuffer(this.vertexBufferFormat);
            this.renderSkyNoises(bufferbuilder, index);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.skyNoiseVBOs[index].bufferData(bufferbuilder.getByteBuffer());
        } else {
            this.skyNoiseGLCallLists[index] = GLAllocation.generateDisplayLists(1);
            GlStateManager.pushMatrix();
            //4864==0x1300==GL11.GL_COMPILE
//            GlStateManager.glNewList(this.starGLCallList, 4864);
            GlStateManager.glNewList(this.skyNoiseGLCallLists[index], GL11.GL_COMPILE);
            this.renderSkyNoises(bufferbuilder, index);
            tessellator.draw();
            GlStateManager.glEndList();
            GlStateManager.popMatrix();
        }
    }

    private void renderSkyNoises(BufferBuilder bufferBuilderIn, int index) {
        Random random = new Random(10842L);
        //7==GL11.GL_QUADS
//        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilderIn.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        //Scale is just the size of the star
        float scale = 4.5F - (index + 1) / 4.0F;
        //This loop generates stars in each layer, the more loop it does, the more star it shows.
        for (int i = 0; i < STAR_NUMBERS; ++i) {
            double d0 = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = (double) (0.3F + random.nextFloat() * 0.2F) * scale * STAR_SIZE;
            double ratio = random.nextFloat() * 0.25 + 0.5F;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 180.0D;
                double d6 = d1 * 180.0D;
                double d7 = d2 * 180.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j) {
                    double d17 = 0.0D;
                    double d18 = (double) ((j & 2) - 1) * d3;
                    double d19 = (double) ((j + 1 & 2) - 1) * d3 * ratio;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + d17 * d13;
                    double d24 = d20 * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    bufferBuilderIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }
    }

    public static int framebufferWidth = -1;
    public static int framebufferHeight = -1;


    private static final Random RANDOM = new Random(31100L);

    private float calculateAngle(float input) {
        return input % 360F;
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        renderSkyEnd();

        if (mc.profiler.profilingEnabled) {
            mc.profiler.startSection("riftSkyRender");
        }

        GlStateManager.disableLighting();
        float ticks = ClientEventHandler.ticks + partialTicks;
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        RANDOM.setSeed(31100L);
        renderStars(ticks);

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.depthMask(true);

        if (mc.profiler.profilingEnabled) {
            mc.profiler.endSection();
        }

    }

    private static final float SPEED_SQRT = MathHelper.sqrt(MOVE_SPEED);

    private void renderStars(float ticks) {
        for (int i = 0; i < 15; i++) {
            //Random color
            float f1 = 2.0F / (float) (18 - i);
            float r = (RANDOM.nextFloat() * 0.5F + 0.1F) * f1;
            float g = (RANDOM.nextFloat() * 0.5F + 0.3F) * f1;
            float b = (RANDOM.nextFloat() * 0.5F + 0.5F) * f1;
            GlStateManager.color(r, g, b, 1.0F);

            GlStateManager.pushMatrix();
            //Random movement
            float f2 = (float) (i + 1);
            float speedMultiply = 2.0F + f2 / 1.5F;
            //Change the const two line below can change the speed it moves.
            float movementFast = ticks * speedMultiply / 300F * MOVE_SPEED;
            float movementSlow = ticks * speedMultiply / 600F * MOVE_SPEED;
            float movementShake = MathHelper.sin(ticks / 60F * SPEED_SQRT) * 2.5F;

//            GlStateManager.translate(17.0F / f2, (2.0F + f2 / 1.5F) * movement, 0.0F);
//            GlStateManager.scale(scale, scale, scale);
            GlStateManager.rotate((f2 * f2 * 4321.0F - f2 * 9.0F) * 2.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((f2 * f2 * 3223.0F + f2 * 5.0F) * 2.0F, 1.0F, 0.0F, 1.0F);
            float rotateDirection = (i / 7.0F - 1.0F);
            GlStateManager.rotate(calculateAngle(movementSlow), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(calculateAngle(movementFast * rotateDirection), 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(calculateAngle(movementFast * (1 - rotateDirection)), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(movementShake, 0.0F, 1.0F, 0.0F);
            renderSkyNoisesBuf(i);
            GlStateManager.popMatrix();
        }
    }

    private void renderSkyNoisesBuf(int index) {
        if (this.vboEnabled) {
            this.skyNoiseVBOs[index].bindBuffer();
//            GlStateManager.glEnableClientState(32884);
            GlStateManager.glEnableClientState(GL11.GL_VERTEX_ARRAY);
//            GlStateManager.glVertexPointer(3, 5126, 12, 0);
            GlStateManager.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
            this.skyNoiseVBOs[index].drawArrays(7);
            this.skyNoiseVBOs[index].unbindBuffer();
//            GlStateManager.glDisableClientState(32884);
            GlStateManager.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        } else {
            GlStateManager.callList(this.skyNoiseGLCallLists[index]);
        }
    }


    private void renderSkyEnd() {
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
//        mc.renderEngine.bindTexture(END_SKY_TEXTURES);
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int k1 = 0; k1 < 6; ++k1) {
            GlStateManager.pushMatrix();

            if (k1 == 1) {
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 2) {
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 3) {
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 4) {
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if (k1 == 5) {
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(-100.0D, -100.0D, -100.0D).color(SKY_BG_RED, SKY_BG_GREEN, SKY_BG_BLUE, 1.0F).endVertex();
            bufferbuilder.pos(-100.0D, -100.0D, 100.0D).color(SKY_BG_RED, SKY_BG_GREEN, SKY_BG_BLUE, 1.0F).endVertex();
            bufferbuilder.pos(100.0D, -100.0D, 100.0D).color(SKY_BG_RED, SKY_BG_GREEN, SKY_BG_BLUE, 1.0F).endVertex();
            bufferbuilder.pos(100.0D, -100.0D, -100.0D).color(SKY_BG_RED, SKY_BG_GREEN, SKY_BG_BLUE, 1.0F).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.enableTexture2D();
    }


}
