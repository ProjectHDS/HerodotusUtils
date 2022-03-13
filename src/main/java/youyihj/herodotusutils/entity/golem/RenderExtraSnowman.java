package youyihj.herodotusutils.entity.golem;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class RenderExtraSnowman extends RenderLiving<EntityExtraSnowman> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/snowman.png");

    public RenderExtraSnowman(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelSnowMan(), 0.5f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityExtraSnowman entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(EntityExtraSnowman entity, double x, double y, double z, float entityYaw, float partialTicks) {
        entity.getColor().applyToRenderSystem();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        Color.UNSET.applyToRenderSystem();
    }
}
