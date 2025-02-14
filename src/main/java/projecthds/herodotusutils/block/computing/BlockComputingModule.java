package projecthds.herodotusutils.block.computing;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import projecthds.herodotusutils.util.Util;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class BlockComputingModule extends BlockCalculatorStructure {
    public BlockComputingModule() {
        super("computing_module");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileComputingModule();
    }

    public static final BlockComputingModule INSTANCE = new BlockComputingModule();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("computing_module");

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;
        ItemStack heldItem = playerIn.getHeldItem(hand);
        Util.getTileEntity(worldIn, pos)
                .map(tileEntity -> tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                .map(itemHandler -> {
                    if (heldItem.isEmpty()) {
                        return itemHandler.extractItem(0, 1, false);
                    } else {
                        return ItemHandlerHelper.insertItem(itemHandler, heldItem, false);
                    }
                })
                .ifPresent(item -> playerIn.setHeldItem(hand, item));
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        Util.getTileEntity(worldIn, pos)
                .map(tileEntity -> tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                .ifPresent(itemHandler -> InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY() + 0.5f, pos.getZ(), itemHandler.getStackInSlot(0).copy()));
        super.breakBlock(worldIn, pos, state);
    }
}
