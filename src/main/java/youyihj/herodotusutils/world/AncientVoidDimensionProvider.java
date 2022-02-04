package youyihj.herodotusutils.world;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.world.chunkgenerator.ChunkGeneratorVoid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import youyihj.herodotusutils.client.render.RiftSkyRenderer;
import youyihj.herodotusutils.proxy.CommonProxy;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author youyihj
 */
public class AncientVoidDimensionProvider extends WorldProvider {
    private IRenderHandler skyRenderer;

    @Override
    protected void init() {
        this.hasSkyLight = false;
        this.biomeProvider = new BiomeProviderSingle(Objects.requireNonNull(ForgeRegistries.BIOMES.getValue(new ResourceLocation("biometweaker", "ancient_void"))));
    }

    @Override
    public DimensionType getDimensionType() {
        return CommonProxy.ANCIENT_VOID_DIMENSION;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer() {
        if (skyRenderer == null) {
            skyRenderer = new RiftSkyRenderer();
        }
        return skyRenderer;
    }

    @Override
    public Vec3d getFogColor(float x, float z) {
        float f = MathHelper.cos(x * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float red = 0.05859375f;
        float green = 0.0f;
        float blue = 0.1015625f;
        red = red * (f * 0.94F + 0.06F);
        green = green * (f * 0.94F + 0.06F);
        blue = blue * (f * 0.91F + 0.09F);
        return new Vec3d(red, green, blue);
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorVoid(world, 8151753964130558908L, ConfigurationManager.getInstance().getPresets().get("ancient bricks islands").getDefinition(9));
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public int getMoonPhase(long worldTime) {
        return 0;
    }

    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.0F;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return null;
    }

    public boolean canRespawnHere() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 8.0F;
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }
}
