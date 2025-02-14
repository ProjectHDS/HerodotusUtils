package projecthds.herodotusutils.item;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.player.IPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import projecthds.herodotusutils.util.interfaces.ITaint;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.modsupport.crafttweaker.ExpandPlayer;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author youyihj
 */
public class ItemTaintChecker extends Item {
    private ItemTaintChecker() {
        this.setRegistryName("taint_checker");
        this.setTranslationKey(HerodotusUtils.MOD_ID + ".taint_checker");
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);
    }

    public static final ItemTaintChecker INSTANCE = new ItemTaintChecker();

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        IPlayer player = CraftTweakerAPI.client.getPlayer();
        if (player != null) {
            ITaint taint = ExpandPlayer.getTaint(player);
            tooltip.add(I18n.format("hdsutils.taint.infected", taint.getInfectedTaint()));
            tooltip.add(I18n.format("hdsutils.taint.sticky", taint.getStickyTaint()));
            tooltip.add(I18n.format("hdsutils.taint.permanent", taint.getPermanentTaint()));
            tooltip.add(I18n.format("hdsutils.taint.percentage", taint.getScale() * 100));
        }
    }
}
