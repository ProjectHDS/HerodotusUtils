package youyihj.herodotusutils.modsupport.modularmachinery.crafting.component;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import youyihj.herodotusutils.HerodotusUtils;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class ComponentImpetus extends ComponentType {

    public static final ComponentImpetus INSTANCE = new ComponentImpetus();

    private ComponentImpetus() {
        setRegistryName(HerodotusUtils.rl("impetus_component"));
    }

    @Nullable
    @Override
    public String requiresModid() {
        return "thaumicaugmentation";
    }
}
