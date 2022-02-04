package youyihj.herodotusutils.network.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import youyihj.herodotusutils.block.TileCreatureDataAnalyzer;

import java.util.Arrays;

/**
 * @author youyihj
 */
public class CreatureDataAnalyzerContainer extends Container {
    private final World world;
    private final BlockPos pos;
    private final TileEntity tileEntity;
    private int[] intList = new int[3];

    public CreatureDataAnalyzerContainer(EntityPlayer player, World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
        this.tileEntity = world.getTileEntity(pos);
        Capability<IItemHandler> itemHandlerCapability = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        IItemHandler item = tileEntity.getCapability(itemHandlerCapability, null);
        InventoryPlayer inventoryPlayer = player.inventory;
        this.addSlotToContainer(new SlotItemHandler(item, 0, 80, 32));
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + 18 * i, 152));
            this.addSlotToContainer(new Slot(inventoryPlayer, i + 9, 8 + 18 * i, 94));
            this.addSlotToContainer(new Slot(inventoryPlayer, i + 18, 8 + 18 * i, 112));
            this.addSlotToContainer(new Slot(inventoryPlayer, i + 27, 8 + 18 * i, 130));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (tileEntity instanceof TileCreatureDataAnalyzer) {
            TileCreatureDataAnalyzer te = (TileCreatureDataAnalyzer) tileEntity;
            int[] newIntList = new int[]{te.getChannel(), te.getTimer(), te.getType()};
            if (!Arrays.equals(intList, newIntList)) {
                this.intList = newIntList;
            }
            for (int i = 0; i < newIntList.length; i++) {
                int finalI = i;
                this.listeners.forEach(it -> it.sendWindowProperty(this, finalI, newIntList[finalI]));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        if (id <= intList.length)
            intList[id] = data;
    }

    public int getChannel() {
        return intList[0];
    }

    public int getTimer() {
        return intList[1];
    }

    public int getType() {
        return intList[2];
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.world.equals(this.world) && playerIn.getDistanceSq(this.pos) < 64.0d;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (index < 0) {
            return itemstack;
        }
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (this.mergeItemStack(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            } else if (index < 28) {
                if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 37) {
                if (!this.mergeItemStack(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
