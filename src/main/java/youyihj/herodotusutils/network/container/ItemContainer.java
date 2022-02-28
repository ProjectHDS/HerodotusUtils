package youyihj.herodotusutils.network.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.awt.*;
import java.util.Objects;

/**
 * @author youyihj
 */
public abstract class ItemContainer extends Container {
    private final World world;
    private final BlockPos pos;
    private final TileEntity tileEntity;
    private final int inventorySize;

    public ItemContainer(EntityPlayer player, World world, BlockPos pos, int inventorySize) {
        this.world = world;
        this.pos = pos;
        this.inventorySize = inventorySize;
        this.tileEntity = Objects.requireNonNull(world.getTileEntity(pos));
        InventoryPlayer inventoryPlayer = player.inventory;
        IItemHandler inventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        for (int i = 0; i < inventorySize; i++) {
            Point point = getSlotPosition(i);
            this.addSlotToContainer(new SlotItemHandler(inventory, i, point.x, point.y));
        }
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + 18 * i, 152));
            this.addSlotToContainer(new Slot(inventoryPlayer, i + 9, 8 + 18 * i, 94));
            this.addSlotToContainer(new Slot(inventoryPlayer, i + 18, 8 + 18 * i, 112));
            this.addSlotToContainer(new Slot(inventoryPlayer, i + 27, 8 + 18 * i, 130));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.world.equals(this.world) && playerIn.getDistanceSq(this.pos) < 64.0d;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < inventorySize) {
                if (!mergeItemStack(itemstack1, inventorySize, inventorySize + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!mergeItemStack(itemstack1, 0, inventorySize, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if(itemstack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if(itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    protected abstract Point getSlotPosition(int index);
}
