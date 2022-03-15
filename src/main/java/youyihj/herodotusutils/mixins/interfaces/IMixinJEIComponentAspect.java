package youyihj.herodotusutils.mixins.interfaces;

import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement.JEIComponent;
import thaumcraft.api.aspects.AspectList;
import youyihj.herodotusutils.modsupport.modularmachinery.crafting.requirement.RequirementAspectList;

public interface IMixinJEIComponentAspect {
    void setRequirementAspectList(RequirementAspectList requirementAspectList);

    JEIComponent<AspectList> getJEIComponent();
}
