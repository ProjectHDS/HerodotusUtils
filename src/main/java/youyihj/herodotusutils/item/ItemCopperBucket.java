package youyihj.herodotusutils.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author youyihj
 */
public class ItemCopperBucket extends ItemFluidContainer {
    public static final String NAME = "copper_bucket";
    public static final ItemCopperBucket INSTANCE = new ItemCopperBucket();

    private ItemCopperBucket() {
        super(NAME);
        this.setMaxStackSize(1);
    }

    @Override
    public String getFluidName(FluidStack fluidStack) {
        return fluidStack.getLocalizedName();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new FluidBucketWrapper(stack) {
            @Nullable
            @Override
            public FluidStack getFluid() {
                return FluidStack.loadFluidStackFromNBT(container.getTagCompound());
            }

            @Override
            protected void setFluid(@Nullable FluidStack fluidStack) {
                NBTTagCompound tagCompound = container.getTagCompound();
                if (tagCompound == null) {
                    tagCompound = new NBTTagCompound();
                    container.setTagCompound(tagCompound);
                }
                if (fluidStack == null) {
                    tagCompound.removeTag("FluidName");
                    tagCompound.removeTag("Amount");
                } else {
                    tagCompound.setString("FluidName", fluidStack.getFluid().getName());
                    tagCompound.setInteger("Amount", Fluid.BUCKET_VOLUME);
                }
            }
        };
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, player, this.isEmpty(stack));
        if (rayTraceResult == null) {
            return ActionResult.newResult(EnumActionResult.FAIL, stack);
        }
        BlockPos pos = rayTraceResult.getBlockPos();
        EnumFacing facing = rayTraceResult.sideHit;
        if (pos == null || facing == null) {
            return ActionResult.newResult(EnumActionResult.FAIL, stack);
        }
        FluidActionResult fluidActionResult;
        if (this.isEmpty(stack)) {
            if (shouldBroken(FluidUtil.getFluidHandler(worldIn, pos, facing), stack, worldIn)) {
                player.renderBrokenItemStack(stack);
                stack.shrink(1);
                return ActionResult.newResult(EnumActionResult.FAIL, stack);
            }
            fluidActionResult = FluidUtil.tryPickUpFluid(stack, player, worldIn, pos, facing);
        } else {
            FluidStack fluidContained = FluidUtil.getFluidContained(stack);
            fluidActionResult = FluidUtil.tryPlaceFluid(player, worldIn, pos, stack, fluidContained);
            if (!fluidActionResult.success) {
                fluidActionResult = FluidUtil.tryPlaceFluid(player, worldIn, pos.offset(facing), stack, fluidContained);
            }
            if (shouldBroken(FluidUtil.getFluidHandler(worldIn, pos, facing), stack, worldIn)) {
                player.renderBrokenItemStack(stack);
                stack.shrink(1);
                return ActionResult.newResult(EnumActionResult.FAIL, stack);
            }
        }
        if (fluidActionResult.success) {
            player.setHeldItem(hand, fluidActionResult.result);
            return ActionResult.newResult(EnumActionResult.SUCCESS, fluidActionResult.result);
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    private boolean shouldBroken(@Nullable IFluidHandler fluidHandler, ItemStack container, World world) {
        if (world.isRemote)
            return false;
        return Stream.of(fluidHandler, FluidUtil.getFluidHandler(container))
                .filter(Objects::nonNull)
                .map(IFluidHandler::getTankProperties)
                .filter(ArrayUtils::isEmpty)
                .map(properties -> properties[0])
                .map(IFluidTankProperties::getContents)
                .filter(Objects::nonNull)
                .map(FluidStack::getFluid)
                .anyMatch(this::isHotFluid) && world.rand.nextInt(6) == 0;
    }
}
