package projecthds.herodotusutils.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemHoeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import projecthds.herodotusutils.HerodotusUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class ItemFlintAIOT extends ItemHoeBase {
    public static final int MAX_DAMAGE = ModuleToolConfig.DURABILITY.get("flint_durable");
    public ItemFlintAIOT(ToolMaterial material, String toolTierName) {
        super(EnumMaterial.FLINT.getToolMaterial(), "flint_durable");
        this.setHarvestLevel("pickaxe", 3);
        this.setHarvestLevel("axe", 3);
        this.setHarvestLevel("shovel", 3);
        this.setRegistryName("flint_aiot");
        this.setTranslationKey(HerodotusUtils.MOD_ID + ".flint_aiot");
        this.setFull3D();
    }

    public static final ItemFlintAIOT INSTANCE = new ItemFlintAIOT(EnumMaterial.FLINT.getToolMaterial(), "flint_durable");

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return stack.getItemDamage() == MAX_DAMAGE ? 1.4f : (float) (toolMaterial.getEfficiency() * 1.4);
    }


    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return stack.getItemDamage() == MAX_DAMAGE ? Collections.emptySet() : super.getToolClasses(stack);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return stack.getItemDamage() == MAX_DAMAGE ? -1 : super.getHarvestLevel(stack, toolClass, player, blockState);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0f && stack.getItemDamage() < MAX_DAMAGE) {
            stack.damageItem(1, entityLiving);
        }

        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return stack.getItemDamage() < MAX_DAMAGE;
    }
}
