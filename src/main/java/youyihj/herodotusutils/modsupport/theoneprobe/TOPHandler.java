package youyihj.herodotusutils.modsupport.theoneprobe;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

/**
 * @author youyihj
 */
public class TOPHandler implements Function<ITheOneProbe, Void> {
    public static int ELEMENT_ITEM_WITH_NAME_ID = 0;

    @Override
    public Void apply(ITheOneProbe iTheOneProbe) {
        iTheOneProbe.registerProvider(TOPInfoProvider.INSTANCE);
        ELEMENT_ITEM_WITH_NAME_ID = iTheOneProbe.registerElementFactory(ElementItemWithName::new);
        return null;
    }
}
