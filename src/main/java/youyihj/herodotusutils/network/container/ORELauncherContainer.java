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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import youyihj.herodotusutils.block.organism.TileORELauncher;

import java.util.Objects;

/**
 * @author youyihj
 */
public class ORELauncherContainer extends Container {
    private final World world;
    private final BlockPos pos;
    private final TileORELauncher tileEntity;

    public ORELauncherContainer(EntityPlayer player, World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
        this.tileEntity = Objects.requireNonNull(((TileORELauncher) world.getTileEntity(pos)));
        InventoryPlayer inventoryPlayer = player.inventory;
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getInput(), 0, 23, 29));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getInput(),  1, 41, 29));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getOutput(), 0, 102, 21));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getOutput(), 1, 120, 21));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getOutput(), 2, 138, 21));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getOutput(), 3, 102, 39));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getOutput(), 4, 120, 39));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getOutput(), 5, 138, 39));
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
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            if (index > 7) {
                if (!mergeItemStack(stack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!mergeItemStack(stack1, 8, 8 + 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

             if (stack1.isEmpty()) {
                 slot.putStack(ItemStack.EMPTY);
             } else {
                 slot.onSlotChanged();
             }

            if(stack1.getCount() == stack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(playerIn, stack1);
        }
        return stack;
    }
}
