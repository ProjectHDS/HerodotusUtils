package youyihj.herodotusutils.modsupport.modularmachinery.crafting.component;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import youyihj.herodotusutils.HerodotusUtils;

import javax.annotation.Nullable;

public class ComponentAspectList extends ComponentType {

    public static final ComponentAspectList INSTANCE = new ComponentAspectList();

    private ComponentAspectList() {
        setRegistryName(HerodotusUtils.rl("aspect_list_component"));
    }

    @Nullable
    public String requiresModid() {
        return "thaumcraft";
    }
}
