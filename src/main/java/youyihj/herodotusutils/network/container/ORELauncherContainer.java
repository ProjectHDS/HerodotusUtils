package youyihj.herodotusutils.network.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.herodotusutils.block.organism.TileORELauncher;
import youyihj.herodotusutils.organism.RecipeContext;
import youyihj.herodotusutils.recipe.OrganismRecipe;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author youyihj
 */
public class ORELauncherContainer extends Container {
    private final World world;
    private final BlockPos pos;
    private final TileORELauncher tileEntity;
    private int[] ints = new int[4];

    public ORELauncherContainer(EntityPlayer player, World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
        this.tileEntity = Objects.requireNonNull(((TileORELauncher) world.getTileEntity(pos)));
        InventoryPlayer inventoryPlayer = player.inventory;
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
        int complete = tileEntity.isStructureComplete() ? 1 : 0;
        RecipeContext context = tileEntity.getRecipeContext();
        int[] newInts = new int[] {complete, context.getTicks(), context.getStatus().ordinal(), Optional.ofNullable(context.getCurrentRecipe()).map(OrganismRecipe::getTime).orElse(0)};
        if (!Arrays.equals(newInts, ints)) {
            ints = newInts;
            for (int i = 0; i < newInts.length; i++) {
                int finalI = i;
                this.listeners.forEach(it -> it.sendWindowProperty(this, finalI, newInts[finalI]));
            }
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id < ints.length)
            ints[id] = data;
    }

    public boolean isComplete() {
        return ints[0] == 1;
    }

    public int getTimer() {
        return ints[1];
    }

    public RecipeContext.Status getStatus() {
        return RecipeContext.Status.values()[ints[2]];
    }

    public int getRequiredTime() {
        return ints[3];
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.world.equals(this.world) && playerIn.getDistanceSq(this.pos) < 64.0d;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return ItemStack.EMPTY;
    }
}
