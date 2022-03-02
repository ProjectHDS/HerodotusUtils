package youyihj.herodotusutils.recipe;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import youyihj.herodotusutils.organism.IngredientType;

import java.util.*;

/**
 * @author youyihj
 */
public class OrganismRecipe {

    public static final Map<String, OrganismRecipe> REGISTRY = new HashMap<>();

    private final String name;
    private final int time;
    private final Multimap<IngredientType<?>, ?> inputs;
    private final Multimap<IngredientType<?>, ?> outputs;
    private final int minLevel;

    public OrganismRecipe(String name, int time, int minLevel, Multimap<IngredientType<?>, ?> inputs, Multimap<IngredientType<?>, ?> outputs) {
        this.name = name;
        this.time = time;
        this.inputs = inputs;
        this.outputs = outputs;
        this.minLevel = minLevel;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public Collection<IngredientType<?>> getInputTypes() {
        return inputs.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getInputEntries(IngredientType<T> type) {
        return ((Collection<T>) inputs.get(type));
    }

    public Collection<IngredientType<?>> getOutputTypes() {
        return outputs.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getOutputEntries(IngredientType<T> type) {
        return ((Collection<T>) outputs.get(type));
    }

    @ZenClass("mods.hdsutils.OrganismRecipeBuilder")
    public static class Builder {
        private int time;
        private String name;
        private int minLevel;
        private final Multimap<IngredientType<?>, Object> inputs = Multimaps.newMultimap(new IdentityHashMap<>(), ArrayList::new);
        private final Multimap<IngredientType<?>, Object> outputs = Multimaps.newMultimap(new IdentityHashMap<>(), ArrayList::new);

        @ZenMethod
        public Builder setTime(int time) {
            this.time = time;
            return this;
        }

        @ZenMethod
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        @ZenMethod
        public Builder setMinLevel(int minLevel) {
            this.minLevel = minLevel;
            return this;
        }

        public <T> Builder addInput(IngredientType<T> type, T ingredient) {
            inputs.put(type, ingredient);
            return this;
        }

        public <T> Builder addOutput(IngredientType<T> type, T ingredient) {
            outputs.put(type, ingredient);
            return this;
        }

        @ZenMethod
        public void buildAndRegister() {
            if (time == 0) {
                throw new IllegalStateException("Time is not set!");
            }
            if (name == null) {
                throw new IllegalStateException("Name is not set!");
            }
            if (minLevel == 0) {
                throw new IllegalStateException("MinLevel is not set!");
            }
            OrganismRecipe recipe = new OrganismRecipe(name, time, minLevel, Multimaps.unmodifiableMultimap(inputs), Multimaps.unmodifiableMultimap(outputs));
            REGISTRY.put(name, recipe);
        }

        // CraftTweaker Helper Methods

        @ZenMethod
        public static Builder create() {
            return new Builder();
        }

        @ZenMethod
        public Builder addItemInput(IItemStack stack) {
            return addInput(IngredientType.ITEM, stack);
        }

        @ZenMethod
        public Builder addItemOutput(IItemStack stack) {
            return addOutput(IngredientType.ITEM, stack);
        }

        @ZenMethod
        public Builder addFluidInput(ILiquidStack stack) {
            return addInput(IngredientType.FLUID, CraftTweakerMC.getLiquidStack(stack));
        }

        @ZenMethod
        public Builder addFluidOutput(ILiquidStack stack) {
            return addOutput(IngredientType.FLUID, CraftTweakerMC.getLiquidStack(stack));
        }

        @ZenMethod
        public Builder addEnergyInput(int energy) {
            return addInput(IngredientType.ENERGY, energy);
        }

        @ZenMethod
        public Builder addImpetusInput(int impetus) {
            return addInput(IngredientType.IMPETUS, impetus);
        }
    }
}
