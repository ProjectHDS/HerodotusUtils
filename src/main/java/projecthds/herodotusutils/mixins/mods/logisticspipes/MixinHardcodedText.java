package projecthds.herodotusutils.mixins.mods.logisticspipes;

import logisticspipes.items.ItemUpgrade;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import network.rs485.logisticspipes.util.TextUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(ItemUpgrade.class)
public class MixinHardcodedText {
    /**
     * @author Gary Bryson Luis Jr.
     * @reason Provide translation key for some connective phrases.
     * */
    @SideOnly(Side.CLIENT)
    @Overwrite(remap = false)
    private String join(List<String> join) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < join.size() - 2; i++) {
            builder.append(TextUtil.translate(ItemUpgrade.SHIFT_INFO_PREFIX + join.get(i)));
            builder.append(TextUtil.translate(ItemUpgrade.SHIFT_INFO_PREFIX + "connect_symbol"));
        }
        if (join.size() > 1) {
            builder.append(TextUtil.translate(ItemUpgrade.SHIFT_INFO_PREFIX + join.get(join.size() - 2)));
            builder.append(TextUtil.translate(ItemUpgrade.SHIFT_INFO_PREFIX + "and"));
        }
        builder.append(TextUtil.translate(ItemUpgrade.SHIFT_INFO_PREFIX + join.get(join.size() - 1)));
        return builder.toString();
    }
}
