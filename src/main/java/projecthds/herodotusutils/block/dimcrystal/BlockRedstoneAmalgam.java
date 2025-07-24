package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import projecthds.herodotusutils.block.PlainBlock;
import projecthds.herodotusutils.network.NetworkHandler;
import projecthds.herodotusutils.network.ParticleEffectPacket;
import projecthds.herodotusutils.util.LogHelper;

import javax.annotation.Nullable;

public class BlockRedstoneAmalgam extends PlainBlock {
    public static final BlockRedstoneAmalgam INSTANCE = new BlockRedstoneAmalgam();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("redstone_amalgam");

    private BlockRedstoneAmalgam() {
        super(Material.GLASS, "redstone_amalgam");
        this.setResistance(3600000.0F);
        this.setHardness(-1);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
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
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();
        return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileRedstoneAmalgam();
    }

    @Mod.EventBusSubscriber
    public class Logic {
        @SubscribeEvent
        public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();

            EnumFacing[] sides = {EnumFacing.NORTH, EnumFacing.SOUTH,
                    EnumFacing.EAST, EnumFacing.WEST};

            for (EnumFacing facing : sides) {
                BlockPos neighborPos = pos.offset(facing);
                IBlockState neighborState = world.getBlockState(neighborPos);
                if (neighborState.getBlock() instanceof BlockRedstoneAmalgam) {
                    if (world.getBlockState(pos).getBlock() == Blocks.FIRE) {
                        if (world.rand.nextFloat() < 0.2f) {
                            if (!world.isRemote) {
                                NetworkHandler.INSTANCE.sendToAllAround(
                                        new ParticleEffectPacket(pos.getX(), pos.getY(), pos.getZ()),
                                        world.provider.getDimension(), pos, 32
                                );
                            }

                            TileEntity te = world.getTileEntity(neighborPos);
                            if (te instanceof TileRedstoneAmalgam) {
                                ((TileRedstoneAmalgam) te).startFluidGeneration(pos);
                            }
                        }

                        if (world.rand.nextFloat() < 0.8f) {
                            int amount = 3 + world.rand.nextInt(4);
                            for (int i = 0; i < amount; i++) {
                                EntityItem entityItem = new EntityItem(world,
                                        neighborPos.getX() + 0.5, neighborPos.getY() + 0.5, neighborPos.getZ() + 0.5,
                                        new ItemStack(Items.REDSTONE));
                                entityItem.setDefaultPickupDelay();
                                entityItem.isImmuneToFire = true;
                                world.spawnEntity(entityItem);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
}
