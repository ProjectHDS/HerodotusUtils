package projecthds.herodotusutils.block;

import com.google.common.primitives.Ints;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.*;
import org.jetbrains.annotations.NotNull;
import projecthds.herodotusutils.fluid.FluidMercurySteam;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockMercurySteam extends BlockFluidClassic {
    private BlockMercurySteam() {
        super(FluidMercurySteam.INSTANCE, Material.WATER);
        this.setRegistryName(FluidMercurySteam.INSTANCE.getName());
        this.density = 0;
        this.densityDir = 1;
        this.tickRate = 2;
    }

    public static final BlockMercurySteam INSTANCE = new BlockMercurySteam();

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollision(worldIn, pos, state, entityIn);

        if (entityIn instanceof EntityLivingBase){
            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 3));
            entityIn.attackEntityFrom(DamageSource.MAGIC,0.5F);
        }

    }
}
