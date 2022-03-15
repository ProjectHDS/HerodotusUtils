package youyihj.herodotusutils.mixins.mods.modularmagic;

import com.google.common.collect.Lists;
import fr.frinn.modularmagic.common.integration.jei.component.JEIComponentAspect;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement.JEIComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.aspects.AspectList;
import youyihj.herodotusutils.mixins.interfaces.IMixinJEIComponentAspect;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.requirement.RequirementAspectList;

import java.util.List;

@Mixin(value = JEIComponentAspect.class, remap = false)
public abstract class MixinJEIComponentAspect extends JEIComponent<AspectList> implements IMixinJEIComponentAspect {

    private RequirementAspectList requirementAspectList = null;

    @Inject(method = "getJEIIORequirements", at = @At("HEAD"), cancellable = true)
    public void injectGetJEIIORequirements(CallbackInfoReturnable<List<AspectList>> cir) {
        if (requirementAspectList != null) {
            AspectList list = new AspectList();
            list.add(this.requirementAspectList.aspect, this.requirementAspectList.amount);
            cir.setReturnValue(Lists.newArrayList(list));
        }
    }

    @Override
    public void setRequirementAspectList(RequirementAspectList requirementAspectList) {
        this.requirementAspectList = requirementAspectList;
    }

    @Override
    public JEIComponent<AspectList> getJEIComponent() {
        return this;
    }

}
