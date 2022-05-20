package youyihj.herodotusutils.block;

import com.infinityraider.agricraft.api.v1.seed.AgriSeed;
import com.infinityraider.agricraft.tiles.TileEntityCrop;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import thaumcraft.api.aspects.Aspect;
import youyihj.herodotusutils.util.Util;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * @author youyihj
 */
public class BlockPrimordialCharger extends PlainBlock {
    public static final BlockPrimordialCharger INSTANCE = new BlockPrimordialCharger(Material.IRON, "primordial_charger");
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("primordial_charger");
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 1.125, 0.875);

    private BlockPrimordialCharger(Material materialIn, String name) {
        super(materialIn, name);
        this.fullBlock = false;
        this.needsRandomTick = true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Optional<IItemHandler> optionalItem = Util.getCapability(worldIn, pos, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
        if (optionalItem.isPresent()) {
            IItemHandler inventory = optionalItem.get();
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if (heldItem.isEmpty()) {
                ItemStack extractItem = inventory.extractItem(0, 1, true);
                if (!extractItem.isEmpty()) {
                    if (!worldIn.isRemote) {
                        playerIn.setHeldItem(hand, inventory.extractItem(0, 1, false));
                    }
                    return true;
                }
            } else if (inventory.isItemValid(0, heldItem)) {
                ItemStack itemStack = inventory.insertItem(0, heldItem, true);
                if (itemStack.getCount() != heldItem.getCount()) {
                    if (!worldIn.isRemote) {
                        playerIn.setHeldItem(hand, inventory.insertItem(0, heldItem, false));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        Util.onBreakContainer(worldIn, pos);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        BlockPos downPos = pos.down();
        int space = 2;
        while (space < 8) {
            if (worldIn.getBlockState(downPos.north(space)).getBlock().getClass() == BlockCatalyzedAltar.class)
                break;
            space++;
        }
        int[] ints = new int[]{-space, 0, space};
        Set<Aspect> foundAspects = new HashSet<>();
        Set<Aspect> advanceAspects = BlockCatalyzedAltar.TRANSFORM_RULES.keySet();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                BlockPos offset = pos.add(ints[i], 0, ints[j]);
                BlockCatalyzedAltar.getAspectPlant(worldIn, offset, true).filter(advanceAspects::contains).ifPresent(foundAspects::add);
            }
        }
        if (foundAspects.size() == 8) {
            Util.getCapability(worldIn, pos, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
                    .ifPresent(it -> {
                        ItemStack stackInSlot = it.extractItem(0, 1, true);
                        if (!stackInSlot.isEmpty()) {
                            boolean healed = stackInSlot.attemptDamageItem(-1, random, null);
                            if (healed) {
                                it.extractItem(0, 1, false);
                                it.insertItem(0, stackInSlot, false);
                            }
                        }
                    });
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    BlockPos offset = pos.add(ints[i], 0, ints[j]);
                    Util.getTileEntity(worldIn, offset, TileEntityCrop.class)
                            .ifPresent(te -> {
                                te.setSeed(new AgriSeed(BlockCatalyzedAltar.BASIC_VIS_PLANT.get(), te.getSeed().getStat()));
                            });
                }
            }
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePrimordialCharger();
    }
}
