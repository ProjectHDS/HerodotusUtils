package youyihj.herodotusutils.item;

import WayofTime.bloodmagic.util.helper.NetworkHelper;
import com.teamacronymcoders.contenttweaker.ContentTweaker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.ItemHandlerHelper;
import youyihj.herodotusutils.fluid.FluidMercury;
import youyihj.herodotusutils.util.Capabilities;

import java.util.Optional;

/**
 * @author youyihj
 */
public class RefinedBottle extends ItemFluidContainer {

    public static final String NAME = "refined_bottle";
    public static final RefinedBottle INSTANCE = new RefinedBottle();

    private RefinedBottle() {
        super(NAME);
        this.setMaxStackSize(16);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack item = player.getHeldItem(hand);
            Optional.ofNullable(worldIn.getTileEntity(pos))
                    .map(tileEntity -> tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing))
                    .ifPresent(fluidHandler -> {
                        FluidActionResult fluidActionResult = FluidUtil.tryFillContainer(item, fluidHandler, 1000, player, true);
                        if (item.getCount() == 1) {
                            player.setHeldItem(hand, fluidActionResult.result);
                        } else {
                            item.shrink(1);
                            ItemHandlerHelper.giveItemToPlayer(player, fluidActionResult.result);
                        }
                    });
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        World world = playerIn.getEntityWorld();
        ItemStack stackCopy = ItemHandlerHelper.copyStackWithSize(stack, 1);
        FluidStack fluidContained = FluidUtil.getFluidContained(stackCopy);
        if (fluidContained == null)
            return false;
        if (fluidContained.getFluid().getName().equals("mercury") && fluidContained.amount >= 500) {
            if (!world.isRemote) {
                FluidUtil.getFluidHandler(stackCopy).drain(500, true);
                target.addPotionEffect(new PotionEffect(MobEffects.POISON, 400, 1));
                if (stack.getCount() == 1) {
                    playerIn.setHeldItem(hand, stackCopy);
                } else {
                    stack.shrink(1);
                    ItemHandlerHelper.giveItemToPlayer(playerIn, stackCopy);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (containsMercury(itemStack)) {
            playerIn.setActiveHand(handIn);
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
        } else {
            return ActionResult.newResult(EnumActionResult.PASS, itemStack);
        }
    }

    @Override
    public String getFluidName(FluidStack fluidStack) {
        return fluidStack.getLocalizedName() + " * " + fluidStack.amount + " mB";
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return containsMercury(stack) ? 32 : 0;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote && containsMercury(stack)) {
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.POISON, 400, 0));
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 800, 1));
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 800, 0));
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                player.getCapability(Capabilities.TAINT_CAPABILITY, null).addPermanentTaint(3);
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ContentTweaker.MOD_ID, "flesh_bolus"));
                if (item != null && NetworkHelper.getSoulNetwork(player).getCurrentEssence() >= 1000) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(item));
                    player.sendStatusMessage(new TextComponentTranslation("hdsutils.drink_mercury"), true);
                }
            }
        }
        return new ItemStack(this, 1, 0);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return containsMercury(stack) ? EnumAction.DRINK : EnumAction.NONE;
    }

    private boolean containsMercury(ItemStack stack) {
        return new FluidStack(FluidMercury.INSTANCE, Fluid.BUCKET_VOLUME).isFluidEqual(stack);
    }
}
