package youyihj.herodotusutils.block;

import com.google.common.collect.ImmutableMap;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.api.v1.seed.AgriSeed;
import com.infinityraider.agricraft.tiles.TileEntityCrop;
import hellfirepvp.modularmachinery.common.util.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import thaumcraft.api.aspects.Aspect;
import youyihj.herodotusutils.util.Lazy;
import youyihj.herodotusutils.util.Util;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static thaumcraft.api.aspects.Aspect.*;
import static youyihj.herodotusutils.modsupport.thaumcraft.AspectHandler.*;

/**
 * @author youyihj
 */
public class BlockCatalyzedAltar extends PlainBlock {
    public static final IProperty<Type> TYPE = PropertyEnum.create("type", Type.class);
    public static final BlockCatalyzedAltar INSTANCE = new BlockCatalyzedAltar(Material.IRON, "catalyzed_altar");
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName("catalyzed_altar");
    public static final Map<Aspect, TransformRule> TRANSFORM_RULES;
    public static final Lazy<IAgriPlant, IAgriPlant> BASIC_VIS_PLANT;

    static {
        TRANSFORM_RULES = ImmutableMap.<Aspect, TransformRule>builder()
                .put(WRATH, new TransformRule(WRATH, ORDER, ENTROPY, AIR, EARTH, WATER, FIRE, FLUX, ELDRITCH))
                .put(GLUTTONY, new TransformRule(GLUTTONY, AIR, ORDER, ENTROPY, WATER, EARTH, ELDRITCH, FLUX, FIRE))
                .put(ENVY, new TransformRule(ENVY, ENTROPY, AIR, ELDRITCH, EARTH, ORDER, FIRE, WATER, FLUX))
                .put(NETHER, new TransformRule(NETHER, FIRE, FLUX, ELDRITCH, ORDER, AIR, EARTH, ENTROPY, WATER))
                .put(SLOTH, new TransformRule(SLOTH, FIRE, WATER, FLUX, AIR, EARTH, ENTROPY, ELDRITCH, ORDER))
                .put(PRIDE, new TransformRule(PRIDE, EARTH, ENTROPY, WATER, AIR, ORDER, FLUX, FIRE, ELDRITCH))
                .put(LUST, new TransformRule(LUST, WATER, FIRE, ENTROPY, ORDER, EARTH, ELDRITCH, FLUX, AIR))
                .put(INSPIRATION, new TransformRule(INSPIRATION, ENTROPY, ELDRITCH, ORDER, EARTH, WATER, AIR, FLUX, FIRE))
                .build();
        BASIC_VIS_PLANT = Lazy.createOptional(() -> AgriApi.getPlantRegistry().get("herodotus_basic_vis_plant"));
    }

    private BlockCatalyzedAltar(Material materialIn, String name) {
        super(materialIn, name);
        this.needsRandomTick = true;
    }

    public static Optional<Aspect> getAspectPlant(World world, BlockPos pos, boolean mature) {
        return Util.getTileEntity(world, pos, TileEntityCrop.class)
                .filter(mature ? TileEntityCrop::canBeHarvested : Util.not(TileEntityCrop::canBeHarvested))
                .map(TileEntityCrop::getSeed)
                .map(AgriSeed::getPlant)
                .map(IAgriPlant::getId)
                .filter(it -> it.endsWith("_vis_plant"))
                .map(it -> it.substring(0, it.indexOf('_')))
                .map(Aspect::getAspect);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        toggleTypeViaRedstone(state, worldIn, pos);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        if (worldIn.isRemote) return;
        AtomicBoolean flag = new AtomicBoolean();
        if (state.getValue(TYPE) == Type.TRANSFORM) {
            for (TransformRule transformRule : TRANSFORM_RULES.values()) {
                if (transformRule.matches(worldIn, pos)) {
                    Optional<IAgriPlant> plant = AgriApi.getPlantRegistry().get(transformRule.getResult().getTag() + "_vis_plant");
                    Optional<TileEntityCrop> crop = Util.getTileEntity(worldIn, pos.up(), TileEntityCrop.class);
                    crop.filter(te -> Optional.ofNullable(te.getSeed()).map(AgriSeed::getPlant).filter(it -> it.equals(BASIC_VIS_PLANT.get())).isPresent()).ifPresent(te ->
                            plant.map(pl -> new AgriSeed(pl, te.getSeed().getStat())).ifPresent(seed -> {
                                te.setSeed(seed);
                                flag.set(true);
                            })
                    );
                }
            }
        } else {
            getAspectPlant(worldIn, pos.up(), false)
                    .map(TRANSFORM_RULES::get)
                    .filter(rule -> rule.matches(worldIn, pos))
                    .flatMap(rule -> Util.getTileEntity(worldIn, pos.up(), TileEntityCrop.class))
                    .ifPresent(crop -> {
                        crop.applyGrowthTick();
                        flag.set(true);
                    });
        }
        if (flag.get()) {
            for (BlockPos offset : BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 0, 1))) {
                if (!offset.equals(pos)) {
                    Util.getTileEntity(worldIn, offset, TileEntityCrop.class).ifPresent(te -> te.setGrowthStage(0));
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        toggleTypeViaRedstone(state, worldIn, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, Type.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    private void toggleTypeViaRedstone(IBlockState state, World world, BlockPos pos) {
        if (world.isRemote) return;
        boolean powered = world.isBlockPowered(pos);
        IBlockState newState = powered ? state.withProperty(TYPE, Type.GROW) : state.withProperty(TYPE, Type.TRANSFORM);
        if (newState != state) {
            world.setBlockState(pos, newState);
        }
    }

    public enum Type implements IStringSerializable {
        TRANSFORM,
        GROW;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public static class TransformRule {
        private final Aspect result;
        private final List<Pair<BlockPos, Aspect>> in = new ArrayList<>();

        public TransformRule(Aspect result, Aspect... in) {
            this.result = result;
            if (in.length != 8) throw new IllegalArgumentException();
            for (int i = 0; i < in.length; i++) {
                int j = i > 3 ? i + 1 : i;
                this.in.add(Pair.of(new BlockPos(j % 3 - 1, 0, j / 3 - 1), in[i]));
            }
        }

        public Aspect getResult() {
            return result;
        }

        public List<Pair<BlockPos, Aspect>> getIn() {
            return in;
        }

        public boolean matches(World world, BlockPos pos) {
            EnumFacing facing = EnumFacing.NORTH;
            do {
                int flags = 0;
                for (int i = 0; i < 8; i++) {
                    Pair<BlockPos, Aspect> pair = in.get(i);
                    BlockPos offset = pos.add(MiscUtils.rotateYCCWNorthUntil(pair.getLeft(), facing));
                    boolean matches = getAspectPlant(world, offset, true).filter(pair.getValue()::equals).isPresent();
                    flags |= (matches ? 1 : 0) << i;
                }
                if (flags == 0xff) {
                    return true;
                }
                facing = facing.rotateYCCW();
            } while (facing != EnumFacing.NORTH);
            return false;
        }


    }
}
