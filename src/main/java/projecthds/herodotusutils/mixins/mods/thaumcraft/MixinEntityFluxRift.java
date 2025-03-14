package projecthds.herodotusutils.mixins.mods.thaumcraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import projecthds.herodotusutils.item.ItemRiftFeed;
import projecthds.herodotusutils.util.ItemDropSupplier;
import projecthds.herodotusutils.util.SharedRiftAction;
import thaumcraft.common.entities.EntityFluxRift;

/**
 * @author youyihj
 */
@Pseudo
@Mixin(EntityFluxRift.class)
public abstract class MixinEntityFluxRift extends Entity {

    private static final ItemDropSupplier PRIMORDIAL_GRAIN = ItemDropSupplier.of(() -> new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumicwonders:primordial_grain"))));

    @Shadow(remap = false)
    int maxSize;

    public MixinEntityFluxRift(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false)
    public abstract int getRiftSize();

    @Shadow(remap = false)
    public abstract void setRiftSize(int s);

    @Shadow(remap = false)
    public abstract void setCollapse(boolean b);

    @Shadow(remap = false)
    public abstract float getRiftStability();

    @Shadow(remap = false)
    public abstract void setRiftStability(float s);

    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"))
    public boolean injectOnUpdate(Entity entity, DamageSource source, float amount) {
        return SharedRiftAction.attackEntity(entity, amount);
    }

    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setDead()V"))
    public void injectOnUpdate(Entity entity) {
        EntityItem entityItem = (EntityItem) entity;
        ItemStack item = entityItem.getItem();
        if (item.getItem() == ItemRiftFeed.INSTANCE) {
            int s = 0;
            for (int i = 0; i < item.getCount(); i++) {
                s += MathHelper.getInt(world.rand, 3, 8);
            }
            int newSize = Math.min(100, getRiftSize() + item.getCount() + s);
            maxSize = newSize;
            setRiftStability(Math.min(100.0f, getRiftStability() + 10.0f * item.getCount()));
            setRiftSize(newSize);
            if (getRiftSize() >= 100)
                setCollapse(true);
        }
        entity.setDead();
    }

    @Redirect(method = "completeCollapse", at = @At(value = "INVOKE", target = "Lthaumcraft/common/entities/EntityFluxRift;entityDropItem(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/item/EntityItem;", ordinal = 0))
    public EntityItem removePrimordialPearl(EntityFluxRift instance, ItemStack itemStack, float v) {
        // NO-OP
        return null;
    }

    @Inject(method = "completeCollapse", at = @At(value = "INVOKE", target = "Lthaumcraft/common/entities/EntityFluxRift;setDead()V"))
    public void addPrimordialGrain(CallbackInfo ci) {
        this.entityDropItem(PRIMORDIAL_GRAIN.get(), 0.0f);
    }
}
