package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import projecthds.herodotusutils.block.PlainBlock;
import projecthds.herodotusutils.config.HDSUConfig;
import projecthds.herodotusutils.item.ItemLithiumQuartz;
import projecthds.herodotusutils.item.ItemLithiumQuartzPowder;
import projecthds.herodotusutils.util.LogHelper;

import javax.annotation.Nullable;
import java.util.*;

public class BlockLithiumQuartzPowderBlock extends PlainBlock {
    public static final BlockLithiumQuartzPowderBlock INSTANCE = new BlockLithiumQuartzPowderBlock();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("lithium_quartz_powder_block");
    private static final Map<BlockPos, Long> lastUpdateTime = new HashMap<>();
    private static final Map<BlockPos, Integer> updateBurstCounter = new HashMap<>();
    private static final Map<BlockPos, Integer> randomTickCounter = new HashMap<>();
    private static final Map<BlockPos, Integer> surroundingUpdateCounter = new HashMap<>();
    private static final Map<BlockPos, Deque<Long>> updateRateTracker = new HashMap<>();

    private BlockLithiumQuartzPowderBlock() {
        super(Material.GLASS, "lithium_quartz_powder_block");
        setTickRandomly(true);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileLithiumQuartzPowderBlock();
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isRemote) return;

        int ticks = incrementRandomTicks(pos);
        LogHelper.debug("锂石英粉块在 {} 收到随机刻 #{}", pos, ticks);

        if (ticks >= HDSUConfig.RequiredRandomTicksForQuartz) {
            transformBlock(worldIn, pos);
        }
    }

    private int incrementRandomTicks(BlockPos pos) {
        int ticks = getCounterValue(randomTickCounter, pos, 0) + 1;
        putValue(randomTickCounter, pos, ticks);
        return ticks;
    }

    private void transformBlock(World world, BlockPos pos) {
        int updates = getCounterValue(surroundingUpdateCounter, pos, 0);
        LogHelper.debug("锂石英粉块在 {} 销毁前记录到 {} 次周围更新", pos, updates);

        world.setBlockToAir(pos);

        for (int i = 0; i < HDSUConfig.PowderDropCountForQuartz; i++) {
            dropItem(world, pos, ItemLithiumQuartzPowder.INSTANCE);
        }

        if (isUpdateCountInRange(updates)) {
            LogHelper.debug("锂石英粉块在 {} 满足条件(更新次数={})，额外掉落锂石英", pos, updates);
            dropItem(world, pos, ItemLithiumQuartz.INSTANCE);
        } else {
            LogHelper.debug("锂石英粉块在 {} 不满足条件(更新次数={})，不额外掉落", pos, updates);
        }

        cleanupCounters(pos);
    }

    private boolean isUpdateCountInRange(int updates) {
        return updates >= HDSUConfig.MinUpdatesForQuartz && updates <= HDSUConfig.MaxUpdatesForQuartz;
    }

    private static void cleanupCounters(BlockPos pos) {
        removeValue(randomTickCounter, pos);
        removeValue(surroundingUpdateCounter, pos);
        updateRateTracker.remove(pos);
        lastUpdateTime.remove(pos);
        updateBurstCounter.remove(pos);
    }

    private static boolean isLithiumQuartzPowderBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == INSTANCE;
    }

    private static BlockPos findExistingPos(Map<BlockPos, ?> map, BlockPos pos) {
        for (BlockPos existingPos : map.keySet()) {
            if (existingPos.getX() == pos.getX() &&
                    existingPos.getY() == pos.getY() &&
                    existingPos.getZ() == pos.getZ()) {
                return existingPos;
            }
        }
        return null;
    }

    private static int getCounterValue(Map<BlockPos, Integer> map, BlockPos pos, int defaultValue) {
        BlockPos existingPos = findExistingPos(map, pos);
        return existingPos != null ? map.get(existingPos) : defaultValue;
    }

    private static void putValue(Map<BlockPos, Integer> map, BlockPos pos, int value) {
        BlockPos newPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        BlockPos existingPos = findExistingPos(map, pos);

        if (existingPos != null) {
            map.remove(existingPos);
        }

        map.put(newPos, value);
    }

    private static void removeValue(Map<BlockPos, ?> map, BlockPos pos) {
        BlockPos existingPos = findExistingPos(map, pos);
        if (existingPos != null) {
            map.remove(existingPos);
        }
    }

    private void dropItem(World world, BlockPos pos, Item item) {
        Block.spawnAsEntity(world, pos, new ItemStack(item));
    }

    public static void recordSurroundingUpdate(BlockPos updatedPos, World world) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos powderPos = updatedPos.offset(facing);

            if (isLithiumQuartzPowderBlock(world, powderPos)) {
                long currentTick = world.getTotalWorldTime();

                Long lastUpdate = lastUpdateTime.getOrDefault(powderPos, 0L);
                int burstCount = updateBurstCounter.getOrDefault(powderPos, 0);

                boolean withinDebounceInterval = (currentTick - lastUpdate) <= HDSUConfig.DebounceIntervalForQuartz;
                LogHelper.debug("在消抖期内，间隔为 {}", currentTick - lastUpdate);

                if (withinDebounceInterval) {
                    burstCount++;
                } else {
                    burstCount = 1;
                }

                lastUpdateTime.put(powderPos, currentTick);
                updateBurstCounter.put(powderPos, burstCount);

                if (withinDebounceInterval && burstCount > HDSUConfig.MaxBurstUpdatesForQuartz) {
                    LogHelper.debug("锂石英粉块在 {} 消抖生效，忽略连续更新 #{}", powderPos, burstCount);
                    continue;
                }

                LogHelper.debug("锂石英粉块在 {} 接受更新 (突发序列 #{}, 间隔: {})",
                        powderPos, burstCount, currentTick - lastUpdate);

                int updates = getCounterValue(surroundingUpdateCounter, powderPos, 0);
                updates++;
                putValue(surroundingUpdateCounter, powderPos, updates);

                Deque<Long> timestamps = updateRateTracker.computeIfAbsent(powderPos, k -> new ArrayDeque<>());
                timestamps.addLast(currentTick);

                while (!timestamps.isEmpty() &&
                        (currentTick - timestamps.peekFirst()) > HDSUConfig.RateCheckIntervalForQuartz) {
                    timestamps.removeFirst();
                }

                if (timestamps.size() > HDSUConfig.UpdateRateThresholdForQuartz) {
                    LogHelper.debug("锂石英粉块在 {} 因高频更新（{}次/秒）发生爆炸", powderPos, timestamps.size());
                    handleOverloadExplosion(world, powderPos);
                    continue;
                }

                if (findExistingPos(randomTickCounter, powderPos) == null) {
                    putValue(randomTickCounter, powderPos, 0);
                    LogHelper.debug("发现未追踪的锂石英粉块在 {}，已添加到跟踪列表", powderPos);
                }

                LogHelper.debug("锂石英粉块在 {} 周围更新次数增加到: {}", powderPos, updates);
            }
        }
    }

    private static void handleOverloadExplosion(World world, BlockPos pos) {
        world.createExplosion(null,
                pos.getX() + 2,
                pos.getY() + 2,
                pos.getZ() + 2,
                4.0F,
                HDSUConfig.OverloadExplosionBreakBlock);

        world.setBlockToAir(pos);

        cleanupCounters(pos);
        updateRateTracker.remove(pos);
    }


    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            super.onBlockAdded(worldIn, pos, state);
            putValue(randomTickCounter, pos, 0);
            putValue(surroundingUpdateCounter, pos, 0);
            lastUpdateTime.put(pos, worldIn.getTotalWorldTime());
            updateBurstCounter.put(pos, 0);
            LogHelper.debug("锂石英粉块在 {} 被放置，初始化计数器", pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            LogHelper.debug("锂石英粉块在 {} 被破坏，清理计数器", pos);
            cleanupCounters(pos);
        }
        super.breakBlock(worldIn, pos, state);
    }
}