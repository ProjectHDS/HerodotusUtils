package projecthds.herodotusutils.entity.golem;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;
import projecthds.herodotusutils.util.ItemDropSupplier;
import thaumcraft.api.items.ItemsTC;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import static projecthds.herodotusutils.entity.golem.Color.*;
import static projecthds.herodotusutils.entity.golem.Shape.*;

/**
 * @author youyihj
 */
public class GolemDrops {
    private static final Map<Pair<Color, Shape>, DropFunction[]> drops = new HashMap<>();
    private static final Map<Pair<Color, Shape>, String> metalMap = new HashMap<>();

    static {
        DropFunction coal = new DropFunction("Coal", "nugget", ItemDropSupplier.ofItem(Items.COAL));
        DropFunction redStone = new DropFunction("Redstone", "tinyDust", ItemDropSupplier.ofItem(Items.REDSTONE));
        DropFunction quartz = new DropFunction("Quartz", "tinyDust", ItemDropSupplier.ofItem(Items.QUARTZ));
        DropFunction iron = new DropFunction("Iron");
        DropFunction copper = new DropFunction("Copper");
        DropFunction lead = new DropFunction("Lead");
        DropFunction silver = new DropFunction("Silver");
        DropFunction tin = new DropFunction("Tin");
        DropFunction nickel = new DropFunction("Nickel");
        DropFunction mercury = new DropFunction("Mercury", ItemDropSupplier.ofOreDict("nuggetQuicksilver"), ItemDropSupplier.ofItem(ItemsTC.quicksilver));
        DropFunction lithium = new DropFunction("Lithium", "nugget", "crystal");
        DropFunction cinnabar = new DropFunction("Cinnabar", "nugget", "crystal");
        DropFunction quartzite = new DropFunction("Quartzite", "cluster", "rock");
        DropFunction omniEssential = new DropFunction("OmniEssential", ItemDropSupplier.ofItem("contenttweaker:onmi_essential_tiny_dust"), ItemDropSupplier.ofItem("contenttweaker:onmi_essential"));
        DropFunction coloredGem = new DropFunction("ColoredGem", ItemDropSupplier.ofItem("contenttweaker:colored_gem_tiny"), ItemDropSupplier.ofItem("contenttweaker:colored_gem"));
        DropFunction ferrousMetal = new DropFunction("FerrousMetal");
        DropFunction preciousMetal = new DropFunction("PreciousMetal");
        DropFunction radioactiveResidue = new DropFunction("RadioactiveResidue");
        DropFunction nonFerrousMetal = new DropFunction("NonFerrousMetal");
        DropFunction fractalMetal = new DropFunction("FractalMetal");

        addDrop(RED, RHOMBUS, 1, redStone);
        addDrop(RED, RHOMBUS, 2, redStone.withModifier(3.0f));
        addDrop(RED, RHOMBUS, 3, cinnabar);
        addDrop(YELLOW, RHOMBUS, 1, coal);
        addDrop(YELLOW, RHOMBUS, 2, coal.withModifier(3.0f));
        addDrop(YELLOW, RHOMBUS, 3, coloredGem);
        addDrop(BLUE, RHOMBUS, 1, quartz);
        addDrop(BLUE, RHOMBUS, 2, quartz.withModifier(3.0f));
        addDrop(BLUE, RHOMBUS, 3, omniEssential);
        addDrop(RED, SQUARE, 1, iron.withModifier(2.5f));
        addDrop(RED, SQUARE, 2, nickel);
        addDrop(RED, SQUARE, 3, ferrousMetal);
        addDrop(YELLOW, SQUARE, 1, copper);
        addDrop(YELLOW, SQUARE, 2, copper.withModifier(4.0f));
        addDrop(YELLOW, SQUARE, 3, preciousMetal);
        addDrop(BLUE, SQUARE, 1, lead);
        addDrop(BLUE, SQUARE, 2, lead.withModifier(4.0f));
        addDrop(BLUE, SQUARE, 3, radioactiveResidue);
        addDrop(RED, SPHERICAL, 1, lithium);
        addDrop(RED, SPHERICAL, 2, lithium.withModifier(3.0f));
        addDrop(RED, SPHERICAL, 3, quartzite);
        addDrop(YELLOW, SPHERICAL, 1, mercury);
        addDrop(YELLOW, SPHERICAL, 2, mercury.withModifier(3.0f));
        addDrop(YELLOW, SPHERICAL, 3, nonFerrousMetal);
        addDrop(BLUE, SPHERICAL, 1, tin.withModifier(2.5f));
        addDrop(BLUE, SPHERICAL, 2, silver);
        addDrop(BLUE, SPHERICAL, 3, fractalMetal);
    }

    public static void addDrop(Color color, Shape shape, int level, DropFunction dropFunction) {
        Pair<Color, Shape> pair = Pair.of(color, shape);
        if (level == 2) {
            metalMap.put(pair, dropFunction.name);
        }
        drops.putIfAbsent(pair, new DropFunction[3]);
        drops.get(pair)[--level] = dropFunction;
    }

    public static ItemStack getDrop(Color color, Shape shape, int level, Random random, AttackType attackType) {
        return drops.get(Pair.of(color, shape))[--level].get(random, attackType);
    }

    public static String getColoredShape(Color color, Shape shape) {
        return metalMap.get(Pair.of(color, shape));
    }

    public static class DropFunction {
        private final String name;
        private final Map<AttackType, Supplier<ItemStack>> stacks;
        private final float modifier;

        private DropFunction(String name, Map<AttackType, Supplier<ItemStack>> stacks, float modifier) {
            this.name = name;
            this.stacks = stacks;
            this.modifier = modifier;
        }

        public DropFunction(String name, Supplier<ItemStack> normalStack, Supplier<ItemStack> riftStack) {
            this.name = name;
            this.stacks = new EnumMap<>(AttackType.class);
            stacks.put(AttackType.NORMAL, normalStack);
            stacks.put(AttackType.SOFT_RIFT, riftStack);
            stacks.put(AttackType.HARD_RIFT, ItemDropSupplier.ofOreDict("bioactive" + name));
            this.modifier = 1.0f;
        }

        public DropFunction(String name, String normalDropPrefix, Supplier<ItemStack> riftStack) {
            this(name, ItemDropSupplier.ofOreDict(normalDropPrefix + name), riftStack);
        }

        public DropFunction(String name, String normalDropPrefix, String riftDropPrefix) {
            this(name, ItemDropSupplier.ofOreDict(normalDropPrefix + name), ItemDropSupplier.ofOreDict(riftDropPrefix + name));
        }

        public DropFunction(String name) {
            this(name, "nugget", "ingot");
        }


        public ItemStack get(Random random, AttackType attackType) {
            int i = MathHelper.floor(modifier);
            ItemStack itemStack = stacks.get(attackType).get();
            itemStack.setCount(i);
            if (random.nextDouble() < modifier % 1.0f) {
                itemStack.grow(1);
            }
            return itemStack;
        }

        public DropFunction withModifier(float newModifier) {
            return new DropFunction(this.name, this.stacks, newModifier);
        }
    }
}
