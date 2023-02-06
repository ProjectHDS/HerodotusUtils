package projecthds.herodotusutils.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityRedSlime extends EntitySlime {

    public EntityRedSlime(World world) {
        super(world);
    }

    public double baseMaxHealth = 20 * 40;
    public double baseAttackStrength = 4 * 4;

    @Override
    protected EntityRedSlime createInstance() {
        return new EntityRedSlime(world);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.baseMaxHealth = compound.getDouble("baseMaxHealth");
        this.baseAttackStrength = compound.getDouble("baseAttackStrength");
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setDouble("baseMaxHealth", baseMaxHealth);
        compound.setDouble("baseAttackStrength", baseAttackStrength);
        super.writeEntityToNBT(compound);
    }

    @Override
    protected int getAttackStrength() {
        return ((int) (baseAttackStrength));
    }

    @Override
    public void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(baseMaxHealth);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }

    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.setSlimeSize(4, true);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        this.setLeftHanded(false);
        return livingdata;
    }

    @Override
    public void setDead() {

        // Copied from subclass
        int i = this.getSlimeSize();

        if (!this.world.isRemote && i > 1 && this.getHealth() <= 0.0F) {
            int j = 2 + this.rand.nextInt(3);

            for (int k = 0; k < j; ++k) {
                float f = ((float) (k % 2) - 0.5F) * (float) i / 4.0F;
                float f1 = ((float) (k / 2) - 0.5F) * (float) i / 4.0F;
                EntityRedSlime entityRedSlime = this.createInstance(); // edited
                entityRedSlime.syncBaseValue(this, j); // edited

                if (this.hasCustomName()) {
                    entityRedSlime.setCustomNameTag(this.getCustomNameTag());
                }

                if (this.isNoDespawnRequired()) {
                    entityRedSlime.enablePersistence();
                }

                entityRedSlime.setSlimeSize(i / 2, true);
                entityRedSlime.setLocationAndAngles(this.posX + (double) f, this.posY + 0.5D, this.posZ + (double) f1, this.rand.nextFloat() * 360.0F, 0.0F);
                this.world.spawnEntity(entityRedSlime);
            }
        }
        this.isDead = true;
    }

    private void syncBaseValue(EntityRedSlime other, double spawnAmount) {
        this.baseAttackStrength = Math.ceil(other.baseAttackStrength / spawnAmount);
        this.baseMaxHealth = Math.ceil(other.baseMaxHealth /spawnAmount);
    }
}
