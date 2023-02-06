package projecthds.herodotusutils.modsupport.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import dalapo.factech.auxiliary.MachineRecipes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethodStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
@ZenRegister
@ZenExpansion("mods.factorytech.Disassembler")
public class ExpandDisassembler {
    @ZenMethodStatic
    public static void add(IEntityDefinition definition, IItemStack... stacks) {
        CraftTweakerAPI.apply(new Add(definition, stacks));
    }

    @ZenMethodStatic
    public static void remove(IEntityDefinition definition, IItemStack... stacks) {
        CraftTweakerAPI.apply(new Remove(definition, stacks));
    }

    public static abstract class DisassemblerActionBase implements IAction {
        protected final Class<? extends Entity> entityClass;
        protected final IItemStack[] stacks;
        protected final String entityCommandString;
        private final String type;

        protected DisassemblerActionBase(IEntityDefinition definition, IItemStack[] stacks, String type) {
            this.entityClass = ((EntityEntry) definition.getInternal()).getEntityClass();
            this.stacks = stacks;
            this.entityCommandString = definition.toString();
            this.type = type;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void apply() {
            if (EntityLiving.class.isAssignableFrom(entityClass)) {
                if (!MachineRecipes.DISASSEMBLER.containsKey(entityClass)) {
                    MachineRecipes.DISASSEMBLER.put(((Class<? extends EntityLiving>) entityClass), new ArrayList<>());
                }
                List<ItemStack> itemStacks = MachineRecipes.DISASSEMBLER.get(entityClass);
                tweakDroppingItems(itemStacks);
            }
        }

        @Override
        public String describe() {
            return type + " Disassembler recipe for " + entityCommandString + " -> " + Arrays.stream(stacks).map(IItemStack::toCommandString).collect(Collectors.joining(", ", "[", "]"));
        }

        protected abstract void tweakDroppingItems(List<ItemStack> droppingItems);
    }

    public static class Add extends DisassemblerActionBase {

        protected Add(IEntityDefinition definition, IItemStack[] stacks) {
            super(definition, stacks, "Adding");
        }

        @Override
        protected void tweakDroppingItems(List<ItemStack> droppingItems) {
            droppingItems.addAll(Arrays.asList(CraftTweakerMC.getItemStacks(this.stacks)));
        }
    }

    public static class Remove extends DisassemblerActionBase {

        protected Remove(IEntityDefinition definition, IItemStack[] stacks) {
            super(definition, stacks, "Removing");
        }

        @Override
        protected void tweakDroppingItems(List<ItemStack> droppingItems) {
            List<ItemStack> collect = droppingItems.stream()
                    .filter(this::shouldRemoveThisItem)
                    .collect(Collectors.toList());
            droppingItems.removeAll(collect);
        }

        private boolean shouldRemoveThisItem(ItemStack stack) {
            return Arrays.stream(stacks).map(CraftTweakerMC::getItemStack).anyMatch(stack::isItemEqual);
        }
    }
}
