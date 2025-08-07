package projecthds.herodotusutils.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import projecthds.herodotusutils.fluid.FluidMercurySteam;
import projecthds.herodotusutils.item.ItemMercuryDroplet;

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

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);
        if (!world.isRemote && isSourceBlock(world, pos)) {
            if (isTouchWater(world, pos)) {
                int dropletCount = 8 + rand.nextInt(11);
                ItemStack dropletStack = new ItemStack(ItemMercuryDroplet.INSTANCE, dropletCount);
                EntityItem dropletEntity = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), dropletStack);
                world.spawnEntity(dropletEntity);

                world.playSound(null, pos, net.minecraft.init.SoundEvents.BLOCK_LAVA_EXTINGUISH,
                        net.minecraft.util.SoundCategory.BLOCKS, 0.5F, 2.0F);
                world.setBlockToAir(pos);
            }
        }
    }

    private boolean isTouchWater(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos checkPos = pos.offset(facing);
            IBlockState checkState = world.getBlockState(checkPos);

            if (checkState.getBlock() == Blocks.WATER ||
                    checkState.getBlock() == Blocks.FLOWING_WATER) {
                return true;
            }
        }
        return false;
    }
}
