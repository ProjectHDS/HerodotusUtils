package projecthds.herodotusutils.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.client.IBlockHasColor;
import projecthds.herodotusutils.client.IItemHasColor;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public class BlockOreBase extends PlainBlock implements IBlockHasColor, IItemHasColor {
    public static final PropertyEnum<Type> PROPERTY_TYPE = PropertyEnum.create("type", Type.class);
    private Supplier<ItemStack> dropItemSupplier;
    private final Item item;
    private final String name;
    private final int color;

    public BlockOreBase(String name, int color) {
        super(Material.ROCK, name + "_ore");
        this.setHarvestLevel("pickaxe", 0);
        this.color = color;
        this.name = name;
        this.item = new ItemBlock(this) {
            @Override
            public String getItemStackDisplayName(ItemStack stack) {
                return I18n.translateToLocalFormatted(Type.valueOf(stack.getMetadata()).unlocalizedName, I18n.translateToLocal("base.material." + name));
            }

            @Override
            public int getMetadata(int damage) {
                return damage;
            }

        }.setRegistryName(HerodotusUtils.rl(name + "_ore")).setHasSubtypes(true);
        this.setHardness(1.0f);
    }

    public BlockOreBase setDropItemSupplier(Supplier<ItemStack> dropItemSupplier) {
        this.dropItemSupplier = dropItemSupplier;
        return this;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROPERTY_TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PROPERTY_TYPE).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(PROPERTY_TYPE, Type.valueOf(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(meta);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return dropItemSupplier == null ? this.getMetaFromState(state) : 0;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : RANDOM;

        if (dropItemSupplier == null) {
            super.getDrops(drops, world, pos, state, fortune);
        } else {
            try {
                ItemStack stack = dropItemSupplier.get();
                Type type = state.getValue(PROPERTY_TYPE);
                stack.setCount(getDropAmount(rand, type.getRandomDrop(rand), fortune));
                drops.add(stack);
            } catch (Exception e) {
                HerodotusUtils.logger.error("Fail to get drop items, drop the block itself", e);
                dropItemSupplier = null;
                super.getDrops(drops, world, pos, state, fortune);
            }
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < Type.values().length; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : RANDOM;

        if (dropItemSupplier == null) {
            return 0;
        } else {
            Type type = state.getValue(PROPERTY_TYPE);
            return MathHelper.getInt(rand, type.minXpDrop + fortune * 2, type.maxDrop + fortune * 3);
        }
    }

    public Item getItem() {
        return item;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int tintIndex) {
        return tintIndex == 0 ? color : -1;
    }

    @Override
    public String getLocalizedName() {
        return new ItemStack(this, 1, 0).getDisplayName();
    }

    public void registerOreDict() {
        for (Type value : Type.values()) {
            String oreDictName = (value == Type.NORMAL ? "ore" : value.getName() + "Ore") +
                    Character.toUpperCase(name.charAt(0)) +
                    name.substring(1);
            OreDictionary.registerOre(oreDictName, new ItemStack(this, 1, value.ordinal()));
        }
    }

    private int getDropAmount(Random random, int originAmount, int fortuneLevel) {
        int i = random.nextInt(fortuneLevel + 2) - 1;
        if (i < 0) {
            i = 0;
        }
        return originAmount * (i + 1);
    }

    @Override
    public int getColorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
        return tintIndex == 0 ? color : -1;
    }

    public enum Type implements IStringSerializable {
        NORMAL(1, 3, 0, 5, "base.part.ore"),
        POOR(0, 2, 0, 2, "base.part.poor_ore"),
        DENSE(3, 5, 1, 8, "base.part.dense_ore");

        public final int minDrop;
        public final int maxDrop;
        public final int minXpDrop;
        public final int maxXpDrop;
        public final String unlocalizedName;

        Type(int minDrop, int maxDrop, int minXpDrop, int maxXpDrop, String unlocalizedName) {
            this.minDrop = minDrop;
            this.maxDrop = maxDrop;
            this.minXpDrop = minXpDrop;
            this.maxXpDrop = maxXpDrop;
            this.unlocalizedName = unlocalizedName;
        }

        public static Type valueOf(int index) {
            if (index >= values().length)
                index = 0;
            return values()[index];
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

        public int getRandomDrop(Random random) {
            return MathHelper.getInt(random, minDrop, maxDrop);
        }
    }
}
