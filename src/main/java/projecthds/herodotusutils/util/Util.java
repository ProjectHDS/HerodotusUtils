package projecthds.herodotusutils.util;

import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.IData;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.ArrayUtils;
import projecthds.herodotusutils.HerodotusUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author youyihj
 */
public final class Util {
    private Util() {
    }

    public static IData createDataMap(String key, IData value) {
        Map<String, IData> temp = new HashMap<>();
        temp.put(key, value);
        return new DataMap(temp, true);
    }

    public static int sumFastIntCollection(IntCollection intCollection) {
        IntIterator iterator = intCollection.iterator();
        int s = 0;
        while (iterator.hasNext()) {
            s += iterator.nextInt();
        }
        return s;
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    public static <T> Optional<T> getTileEntity(World world, BlockPos pos, Class<T> tileEntityClass) {
        return Util.getTileEntity(world, pos)
                .filter(tileEntityClass::isInstance)
                .map(tileEntityClass::cast);
    }

    public static Optional<TileEntity> getTileEntity(World world, BlockPos pos) {
        return Optional.ofNullable(world.getTileEntity(pos));
    }

    public static <T> T getCycledNextElement(T[] array, T thisElement) {
        int index = ArrayUtils.indexOf(array, thisElement);
        if (index == ArrayUtils.INDEX_NOT_FOUND) {
            throw new NoSuchElementException(thisElement + " is not belong to the given array");
        } else if (index == array.length - 1) {
            return array[0];
        } else {
            return array[index + 1];
        }
    }

    public static AxisAlignedBB createAABBFromModelPos(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AxisAlignedBB(x1 / 16, y1 / 16, z1 / 16, x2 / 16, y2 / 16, z2 / 16);
    }


    public static <T> Optional<T> getCapability(World world, BlockPos pos, Capability<T> capability, @Nullable EnumFacing facing) {
        return getTileEntity(world, pos).map(te -> te.getCapability(capability, facing));
    }

    public static void onBreakContainer(World worldIn, BlockPos pos) {
        Util.getCapability(worldIn, pos, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
                .ifPresent(itemHandler -> {
                    int slots = itemHandler.getSlots();
                    for (int i = 0; i < slots; i++) {
                        InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemHandler.getStackInSlot(i).copy());
                    }
                });
    }

    public static boolean extractItem(IItemHandler itemHandler, ItemStack stack, boolean simulate) {
        stack = stack.copy();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!simulate) {
                ItemStack stackInSlot = itemHandler.getStackInSlot(i);
                if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) continue;
            }
            ItemStack stack1 = itemHandler.extractItem(i, stack.getCount(), simulate);
            if (ItemHandlerHelper.canItemStacksStack(stack, stack1)) {
                stack.shrink(stack1.getCount());
            }
            if (stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean extractItems(IItemHandler itemHandler, List<ItemStack> stacks, boolean simulate) {
        for (ItemStack stack : stacks) {
            if (!extractItem(itemHandler, stack, simulate)) {
                return false;
            }
        }
        return true;
    }

    public static BlockPos rotateYCCWNorthUntil(BlockPos pos, EnumFacing facing) {
        EnumFacing currentFacing = EnumFacing.NORTH;
        while (facing != currentFacing) {
            pos = new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
            currentFacing = currentFacing.rotateYCCW();
        }
        return pos;
    }

    public static ResourceLocation hdsId(String path) {
        return new ResourceLocation(HerodotusUtils.MOD_ID, path);
    }
}
