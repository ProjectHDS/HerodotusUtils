package youyihj.herodotusutils.modsupport.modularmachinery.crafting.requirement;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import fr.frinn.modularmagic.common.integration.jei.component.JEIComponentAspect;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;
import net.minecraft.util.math.MathHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.component.ComponentAspectList;
import youyihj.herodotusutils.modsupport.modularmachinery.tile.MachineComponentAspectListProvider;
import youyihj.herodotusutils.modsupport.modularmachinery.tile.TileAspectListProvider;
import youyihj.herodotusutils.util.IMixinJEIComponentAspect;

import javax.annotation.Nonnull;
import java.util.List;

public class RequirementAspectList extends ComponentRequirement<AspectList, RequirementAspectList.Type> {

    public static class Type extends RequirementType<AspectList, RequirementAspectList> {
        public static final Type INSTANCE = new Type();

        private Type() {
            setRegistryName(HerodotusUtils.rl("aspect_list"));
        }

        @Override
        public ComponentRequirement<AspectList, ? extends RequirementType<AspectList, RequirementAspectList>> createRequirement(IOType type, JsonObject requirement) {
            if (requirement.has("amount") && requirement.get("amount").isJsonPrimitive() && requirement.get("amount").getAsJsonPrimitive().isNumber()) {
                if (requirement.has("aspect") && requirement.get("aspect").isJsonPrimitive() && requirement.get("aspect").getAsJsonPrimitive().isString()) {
                    int aspectRequired = requirement.getAsJsonPrimitive("amount").getAsInt();
                    if (aspectRequired < 0) {
                        throw new JsonParseException("'amount' can not be less than 0");
                    } else {
                        Aspect aspect = Aspect.getAspect(requirement.get("aspect").getAsString());
                        if (aspect != null) {
                            return new RequirementAspectList(type, aspectRequired, aspect);
                        } else {
                            throw new JsonParseException("Invalid aspect name : " + requirement.getAsJsonPrimitive("aspect").getAsString());
                        }
                    }
                } else {
                    throw new JsonParseException("The ComponentType '" + this.getRegistryName() + "' expects a 'aspect'-entry that defines the required/produced aspect");
                }
            } else {
                throw new JsonParseException("The ComponentType '" + this.getRegistryName() + "' expects a 'amount'-entry that defines the required/produced aspect amount");
            }
        }
    }

    public int amount;
    public Aspect aspect;

    public RequirementAspectList(IOType actionType, int amount, Aspect aspect) {
        super(Type.INSTANCE, actionType);
        this.amount = amount;
        this.aspect = aspect;
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType().equals(ComponentAspectList.INSTANCE) &&
                cmp instanceof MachineComponentAspectListProvider &&
                cmp.getIOType() == this.getActionType();
    }

    @Override
    public boolean startCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (!this.canStartCrafting(component, context, Lists.newArrayList()).isSuccess()) {
            return false;
        }
        TileAspectListProvider provider = (TileAspectListProvider) component.getComponent().getContainerProvider();
        return provider.takeFromContainer(this.aspect, this.amount);
    }

    @Nonnull
    @Override
    public CraftCheck finishCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (this.getActionType() == IOType.OUTPUT) {
            TileAspectListProvider provider = (TileAspectListProvider) component.getComponent().getContainerProvider();
            provider.addToContainer(this.aspect, this.amount);
        }

        return CraftCheck.success();
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, List<ComponentOutputRestrictor> restrictions) {
        TileAspectListProvider provider = (TileAspectListProvider) component.getComponent().getContainerProvider();
        if (provider.doesContainerContainAmount(this.aspect, this.amount)) {
            return CraftCheck.success();
        }
        return CraftCheck.failure("error.modularmagic.requirement.aspect.less");
    }

    @Override
    public RequirementAspectList deepCopy() {
        return new RequirementAspectList(this.getActionType(), this.amount, this.aspect);
    }

    @Override
    public RequirementAspectList deepCopyModified(List<RecipeModifier> modifiers) {
        float newAmount = RecipeModifier.applyModifiers(modifiers, this, this.amount, false);
        return new RequirementAspectList(this.getActionType(), MathHelper.ceil(newAmount), this.aspect);
    }


    public void startRequirementCheck(ResultChance contextChance, RecipeCraftingContext context) {
    }

    public void endRequirementCheck() {
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "component.missing.aspect_list";
    }

    @Override
    public JEIComponent<AspectList> provideJEIComponent() {
        IMixinJEIComponentAspect jeiComponentAspect = (IMixinJEIComponentAspect) new JEIComponentAspect(null);
        jeiComponentAspect.setRequirementAspectList(this);
        return jeiComponentAspect.getJEIComponent();
    }
}

