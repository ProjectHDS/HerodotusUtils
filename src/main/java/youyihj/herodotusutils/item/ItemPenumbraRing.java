package youyihj.herodotusutils.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import youyihj.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
public class ItemPenumbraRing extends Item implements IBauble {

    public static final String TAG_ALLOWED_FLYING_BY_PENUMBRA = "allowedFlyingByPenumbra";
    public static final ItemPenumbraRing INSTANCE = new ItemPenumbraRing();

    private ItemPenumbraRing() {
        this.setRegistryName("penumbra_ring");
        this.setUnlocalizedName(HerodotusUtils.MOD_ID + ".penumbra_ring");
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        player.noClip = false;
        if (player instanceof EntityPlayer) {
            if (isAllowedFlyingByPenumbra(player)) {
                player.getEntityData().setInteger(TAG_ALLOWED_FLYING_BY_PENUMBRA, 0);
                ((EntityPlayer) player).capabilities.allowFlying = false;
            }
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    public boolean isAllowedFlyingByPenumbra(Entity entity) {
        return entity.getEntityData().getBoolean(TAG_ALLOWED_FLYING_BY_PENUMBRA);
    }

    public void setAllowedFlyingByPenumbra(Entity entity, boolean value) {
        entity.getEntityData().setBoolean(TAG_ALLOWED_FLYING_BY_PENUMBRA, value);
    }

    public void handlePenumbraTick(EntityPlayer player, boolean isClient) {
        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        boolean flag = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (stack.getItem() == StarlightStorageTiny.INSTANCE) {
                if (stack.getMetadata() == 1) {
                    if (!isClient) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setInteger(StarlightStorageTiny.TAG_STARLIGHT, StarlightStorageTiny.CAPACITY - 1);
                        ItemStack newItem = new ItemStack(StarlightStorageTiny.INSTANCE);
                        newItem.setTagCompound(nbt);
                        inventory.set(i, newItem);
                    }
                    flag = true;
                } else {
                    NBTTagCompound nbt = stack.getTagCompound();
                    if (nbt != null) {
                        int starLight = nbt.getInteger(StarlightStorageTiny.TAG_STARLIGHT);
                        if (starLight > 0) {
                            if (!isClient) {
                                nbt.setInteger(StarlightStorageTiny.TAG_STARLIGHT, starLight - 1);
                            }
                            flag = true;
                        }
                    }
                }
                break;
            }
        }
        if (flag) {
            if (!isAllowedFlyingByPenumbra(player) && player.capabilities.allowFlying) {
                setAllowedFlyingByPenumbra(player, false);
            } else {
                setAllowedFlyingByPenumbra(player, true);
                player.capabilities.allowFlying = true;
            }
            player.noClip = player.capabilities.isFlying;
        } else {
            player.noClip = false;
            if (isAllowedFlyingByPenumbra(player)) {
                setAllowedFlyingByPenumbra(player, false);
                player.capabilities.allowFlying = false;
                player.capabilities.isFlying = false;
            }
        }
    }
}
