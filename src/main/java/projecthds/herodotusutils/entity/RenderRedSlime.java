package projecthds.herodotusutils.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import projecthds.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
@SideOnly(Side.CLIENT)
public class RenderRedSlime extends RenderSlime {
    public RenderRedSlime(RenderManager p_i47193_1_) {
        super(p_i47193_1_);
    }

    private static final ResourceLocation RED_SLIME_TEXTURES = HerodotusUtils.rl("textures/entity/red_slime.png");

    @Override
    protected ResourceLocation getEntityTexture(EntitySlime entity) {
        return RED_SLIME_TEXTURES;
    }
}
