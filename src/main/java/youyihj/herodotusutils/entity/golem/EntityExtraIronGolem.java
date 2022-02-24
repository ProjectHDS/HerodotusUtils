package youyihj.herodotusutils.entity.golem;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import youyihj.herodotusutils.util.DataSerializerEnum;
import youyihj.herodotusutils.util.SharedRiftAction;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * @author youyihj
 */
public class EntityExtraIronGolem extends EntityIronGolem implements IGolem {

    private static final DataParameter<Color> COLOR = EntityDataManager.createKey(EntityExtraIronGolem.class, DataSerializerEnum.of(Color.class));
    private static final DataParameter<Shape> SHAPE = EntityDataManager.createKey(EntityExtraIronGolem.class, DataSerializerEnum.of(Shape.class));
    private static final DataParameter<Integer> LEVEL = EntityDataManager.createKey(EntityExtraIronGolem.class, DataSerializers.VARINT);

    public EntityExtraIronGolem(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(COLOR, Color.UNSET);
        this.dataManager.register(SHAPE, Shape.UNSET);
        this.dataManager.register(LEVEL, 0);
    }

    @Override
    public Color getColor() {
        return dataManager.get(COLOR);
    }

    @Override
    public void setColor(Color color) {
        dataManager.set(COLOR, color);
    }

    @Override
    public Shape getShape() {
        return dataManager.get(SHAPE);
    }

    @Override
    public void setShape(Shape shape) {
        dataManager.set(SHAPE, shape);
    }

    @Override
    public int getLevel() {
        return dataManager.get(LEVEL);
    }

    @Override
    public void setLevel(int level) {
        dataManager.set(LEVEL, level);
    }

    @Override
    public EntityLivingBase getEntity() {
        return this;
    }

    @Override
    public IGolem copy() {
        EntityExtraIronGolem entity = new EntityExtraIronGolem(world);
        entity.setLevel(getLevel());
        entity.setShape(getShape());
        entity.setColor(getColor());
        entity.setPositionAndRotation(posX, posY, posZ, rotationYaw, rotationPitch);
        return entity;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(compound);
        nbtTagCompound.setString("Color", this.dataManager.get(COLOR).name());
        nbtTagCompound.setString("Shape", this.dataManager.get(SHAPE).name());
        nbtTagCompound.setInteger("Level", this.dataManager.get(LEVEL));
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.dataManager.set(COLOR, Color.valueOf(compound.getString("Color")));
        this.dataManager.set(SHAPE, Shape.valueOf(compound.getString("Shape")));
        this.dataManager.set(LEVEL, compound.getInteger("Level"));
    }

    @Override
    public ITextComponent getDisplayName() {
        TextComponentTranslation textComponent = new TextComponentTranslation("entity.hdsutils.golem.name",
                new TextComponentTranslation("color." + getColor().name().toLowerCase(Locale.ENGLISH)),
                new TextComponentTranslation("base.material." + getShape().name().toLowerCase(Locale.ENGLISH)),
                getLevel());
        textComponent.getStyle().setHoverEvent(this.getHoverEvent());
        textComponent.getStyle().setInsertion(this.getCachedUniqueIdString());
        return textComponent;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        boolean result = super.attackEntityFrom(source, amount);
        if (result && !world.isRemote) {
            AttackType attackType = null;
            if (source == SharedRiftAction.RIFT) {
                attackType = amount >= 10.0f ? AttackType.HARD_RIFT : AttackType.SOFT_RIFT;
            } else if (amount >= 2.0f) {
                attackType = AttackType.NORMAL;
            }
            if (attackType != null && getLevel() != 0) {
                ItemStack drop = GolemDrops.getDrop(getColor(), getShape(), getLevel(), world.rand, attackType);
                world.spawnEntity(new EntityItem(world, posX + 0.5f, posY + 0.5f, posZ + 0.5f, drop));
            }
        }
        return result;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return null;
    }
}
