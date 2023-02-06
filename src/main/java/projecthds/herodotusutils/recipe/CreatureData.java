package projecthds.herodotusutils.recipe;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.item.MCItemStack;
import crafttweaker.mc1120.item.MCMutableItemStack;
import net.minecraft.item.ItemStack;
import projecthds.herodotusutils.util.IntPair;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

/**
 * @author youyihj
 */
@ZenRegister
@ZenClass("mods.hdsutils.CreatureData")
public class CreatureData {
    private static final IItemStack[] items = new IItemStack[16];
    private static final int[] durations = new int[16];

    static {
        Arrays.fill(items, MCItemStack.EMPTY);
    }

    @ZenMethod
    public static void add(int type, IItemStack stack, int duration) {
        items[type] = stack;
        durations[type] = duration;
    }

    public static IntPair get(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        IItemStack toMatch = new MCMutableItemStack(stack);
        for (int i = 0; i < items.length; i++) {
            if (items[i].matches(toMatch)) {
                return new IntPair(i, durations[i]);
            }
        }
        return null;
    }
}
