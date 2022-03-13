package youyihj.herodotusutils.recipe;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youyihj
 */
public class ClipManager {
    public static final List<ClipInfo> clipInfos = new ArrayList<>();

    @ZenClass("mods.hdsutils.Clip")
    @ZenRegister
    public static class ClipInfoWriter {
        @ZenMethod
        public static void addClip(IIngredient ingredient, int duration, int power, @Optional IItemStack breakingResult) {
            ingredient.getItems().forEach(stack -> clipInfos.add(new ClipInfo(CraftTweakerMC.getItemStack(stack), duration, power, CraftTweakerMC.getItemStack(breakingResult))));
        }
    }

    public static class ClipInfo {
        private final ItemStack clip;
        private final int duration;
        private final int power;
        private final ItemStack breakingResult;

        public ClipInfo(ItemStack clip, int duration, int power, ItemStack breakingResult) {
            this.clip = clip;
            this.duration = duration;
            this.power = power;
            this.breakingResult = breakingResult;
        }

        public int getDuration() {
            return duration;
        }

        public int getPower() {
            return power;
        }

        public ItemStack getBreakingResult() {
            return breakingResult;
        }

        public ItemStack getClip() {
            return clip;
        }
    }
}
