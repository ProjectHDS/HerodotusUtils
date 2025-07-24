package projecthds.herodotusutils.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import projecthds.herodotusutils.HerodotusUtils;

import java.util.Random;
public class ItemFireTorch extends Item {

    public static final ItemFireTorch INSTANCE = new ItemFireTorch();

    private ItemFireTorch() {
        this.setRegistryName("fire_torch");
        this.setTranslationKey(HerodotusUtils.MOD_ID + ".fire_torch");
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;

            Random rand = worldIn.rand;
            if (rand.nextFloat() < 0.8f) {
                attemptIgnition(worldIn, player);

                worldIn.playSound(null, player.getPosition(), SoundEvents.ITEM_FIRECHARGE_USE,
                        SoundCategory.PLAYERS, 1.0F, 1.0F);
            } else {
                worldIn.playSound(null, player.getPosition(), SoundEvents.BLOCK_WOOD_BREAK,
                        SoundCategory.PLAYERS, 0.5F, 0.8F);
            }
            stack.shrink(1);
        }

        return stack;
    }

    private void attemptIgnition(World world, EntityPlayer player) {
        BlockPos targetPos = getTargetBlockPos(player);

        if (targetPos != null && !world.isRemote) {
            BlockPos firePos = targetPos.up();

            if (world.isAirBlock(firePos) && Blocks.FIRE.canPlaceBlockAt(world, firePos)) {
                world.setBlockState(firePos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    private BlockPos getTargetBlockPos(EntityPlayer player) {
        Vec3d start = player.getPositionEyes(1.0F);
        Vec3d look = player.getLookVec();
        Vec3d end = start.add(look.x * 3, look.y * 3, look.z * 3);

        net.minecraft.util.math.RayTraceResult result = player.world.rayTraceBlocks(start, end, false);

        if (result != null && result.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
            return result.getBlockPos();
        }

        return null;
    }
}