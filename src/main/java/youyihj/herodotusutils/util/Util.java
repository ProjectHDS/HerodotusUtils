package youyihj.herodotusutils.util;

import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.IData;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import youyihj.herodotusutils.HerodotusUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public static <T> Optional<T> getTileEntity(World world, BlockPos pos, Class<T> tileEntityClass) {
        return Util.getTileEntity(world, pos)
                .filter(tileEntityClass::isInstance)
                .map(tileEntityClass::cast);
    }

    public static Optional<TileEntity> getTileEntity(World world, BlockPos pos) {
        return Optional.ofNullable(world.getTileEntity(pos));
    }

    public static FluidStack getDefaultFluidStack(Fluid fluid) {
        return new FluidStack(fluid, 1000);
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

    @SideOnly(Side.CLIENT)
    public static void renderItem(Minecraft minecraft, int x, int y, boolean isInput) {
        bindJeiTexture(minecraft);
        if (isInput)
            Gui.drawModalRectWithCustomSizedTexture(x - 1, y - 1, 0, 0, 18, 18, 256, 256);
        else
            Gui.drawModalRectWithCustomSizedTexture(x - 5, y - 5, 80, 0, 27, 27, 256, 256);
    }

    @SideOnly(Side.CLIENT)
    public static void renderArrow(Minecraft minecraft, int x, int y, int direction) {
        bindJeiTexture(minecraft);
        switch (direction) {
            case 0:
                Gui.drawModalRectWithCustomSizedTexture(x, y, 36, 0, 22, 15, 256, 256);// right
                break;
            case 1:
                Gui.drawModalRectWithCustomSizedTexture(x, y, 36, 16, 22, 15, 256, 256);// left
                break;
            case 2:
                Gui.drawModalRectWithCustomSizedTexture(x, y, 64, 0, 15, 22, 256, 256);// up
                break;
            case 3:
                Gui.drawModalRectWithCustomSizedTexture(x, y, 64, 32, 15, 22, 256, 256);// down
                break;
            default:
                HerodotusUtils.logger.error("Direction is not supported and you shouldn't goto in here");
        }
    }

    @SideOnly(Side.CLIENT)
    private static void bindJeiTexture(Minecraft minecraft) {
        GlStateManager.enableAlpha();
        minecraft.getTextureManager().bindTexture(HerodotusUtils.rl("textures/gui/jei/jei_default.png"));
    }
}
