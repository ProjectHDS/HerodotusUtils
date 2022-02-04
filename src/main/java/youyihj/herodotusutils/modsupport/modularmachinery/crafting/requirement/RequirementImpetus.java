package youyihj.herodotusutils.modsupport.modularmachinery.crafting.requirement;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;
import net.minecraft.util.math.MathHelper;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.component.ComponentImpetus;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.ingredient.Impetus;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.jei.ImpetusJEIComponent;
import youyihj.herodotusutils.modsupport.modularmachinery.tile.MachineComponentImpetus;
import youyihj.herodotusutils.modsupport.modularmachinery.tile.TileImpetusComponent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

/**
 * @author youyihj
 */
public class RequirementImpetus extends ComponentRequirement<Impetus, RequirementImpetus.Type> {
    private final Impetus impetus;

    public static class Type extends RequirementType<Impetus, RequirementImpetus> {

        public static final Type INSTANCE = new Type();

        private Type() {
            setRegistryName(HerodotusUtils.rl("impetus"));
        }

        @Override
        public ComponentRequirement<Impetus, ? extends RequirementType<Impetus, RequirementImpetus>> createRequirement(IOType type, JsonObject jsonObject) {
            return new RequirementImpetus(type, new Impetus(jsonObject.get("amount").getAsInt()));
        }
    }

    public RequirementImpetus(IOType actionType, Impetus impetus) {
        super(Type.INSTANCE, actionType);
        this.impetus = impetus;
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType().equals(ComponentImpetus.INSTANCE) &&
                cmp instanceof MachineComponentImpetus &&
                cmp.getIOType() == this.getActionType();
    }

    @Override
    public boolean startCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (!this.canStartCrafting(component, context, Lists.newArrayList()).isSuccess()) {
            return false;
        } else if (this.getActionType() == IOType.INPUT) {
            TileImpetusComponent.Input tileComponent = (TileImpetusComponent.Input) component.getComponent().getContainerProvider();
            tileComponent.consumeImpetus(this.impetus.getAmount());
        }
        return true;
    }

    @Nonnull
    @Override
    public CraftCheck finishCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (this.getActionType() == IOType.OUTPUT) {
            TileImpetusComponent.Output tileComponent = (TileImpetusComponent.Output) component.getComponent().getContainerProvider();
            tileComponent.supplyImpetus(this.impetus.getAmount());
        }
        return CraftCheck.success();
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, List<ComponentOutputRestrictor> restrictions) {
        switch (this.getActionType()) {
            case INPUT:
                TileImpetusComponent.Input tileComponent = (TileImpetusComponent.Input) component.getComponent().getContainerProvider();
                if (tileComponent.hasEnoughImpetus(this.impetus.getAmount())) {
                    return CraftCheck.success();
                } else {
                    return CraftCheck.failure("hdsutils.error.impetus.less");
                }
            case OUTPUT:
                TileImpetusComponent.Output tileComponent1 = (TileImpetusComponent.Output) component.getComponent().getContainerProvider();
                if (tileComponent1.hasEnoughCapacity(this.impetus.getAmount())) {
                    return CraftCheck.success();
                } else {
                    return CraftCheck.failure("hdsutils.error.impetus.space");
                }
            default:
                return CraftCheck.failure("?");
        }
    }

    @Override
    public RequirementImpetus deepCopy() {
        return new RequirementImpetus(getActionType(), new Impetus(this.impetus.getAmount()));
    }

    @Override
    public RequirementImpetus deepCopyModified(List<RecipeModifier> modifiers) {
        float newAmount = RecipeModifier.applyModifiers(modifiers, this, this.impetus.getAmount(), false);
        return new RequirementImpetus(getActionType(), new Impetus(MathHelper.ceil(newAmount)));
    }

    @Override
    public void startRequirementCheck(ResultChance contextChance, RecipeCraftingContext context) {

    }

    @Override
    public void endRequirementCheck() {

    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "component.missing.impetus." + ioType.name().toLowerCase(Locale.ENGLISH);
    }

    public Impetus getImpetus() {
        return impetus;
    }

    @Override
    public JEIComponent<Impetus> provideJEIComponent() {
        return new ImpetusJEIComponent(this);
    }
}
