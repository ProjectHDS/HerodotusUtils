package youyihj.herodotusutils.util;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author youyihj
 */
public class Multiblock {
    private final Map<Vec3i, Element> elements = new HashMap<>();
    private int maxY = 0;
    private int minY = 0;

    public static Vec3i rotate(Vec3i vec3i, EnumFacing facing) {
        EnumFacing currentFacing = EnumFacing.NORTH;
        while (currentFacing != facing) {
            currentFacing = currentFacing.rotateYCCW();
            vec3i = new Vec3i(vec3i.getZ(), vec3i.getY(), -vec3i.getX());
        }
        return vec3i;
    }

    private static ItemStack getItemFromBlockState(IBlockState state) {
        Block block = state.getBlock();
        return new ItemStack(Item.getItemFromBlock(block), 1, block.damageDropped(state));
    }

    public void addBlock(Vec3i vec3i, Element element) {
        int y = vec3i.getY();
        if (y > maxY) {
            maxY = y;
        }
        if (y < minY) {
            minY = y;
        }
        elements.put(vec3i, element);
    }

    public Map<Vec3i, Element> getElements() {
        return elements;
    }

    public Map<Vec3i, Element> getSlice(int slice) {
        return Maps.filterEntries(elements, (entry) -> entry.getKey().getY() == slice);
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMinY() {
        return minY;
    }

    public boolean matches(World world, BlockPos pos, EnumFacing facing) {
        for (Map.Entry<Vec3i, Element> entry : elements.entrySet()) {
            Vec3i offset = rotate(entry.getKey(), facing);
            BlockPos offsetPos = pos.add(offset);
            if (!world.isBlockLoaded(offsetPos)) return false;
            if (!entry.getValue().matches(world.getBlockState(pos))) return false;
        }
        return true;
    }


    public static class Element {
        private final List<IBlockState> states;

        public Element(List<IBlockState> states) {
            this.states = states;
        }

        public boolean matches(IBlockState state) {
            return states.contains(state);
        }

        public IBlockState getSampleBlock() {
            if (states.isEmpty()) return Blocks.AIR.getDefaultState();
            return states.get(0);
        }

        public ItemStack getItem(World world, BlockPos pos) {
            if (states.isEmpty()) return ItemStack.EMPTY;
            IBlockState state = states.get(0);
            return getItemFromBlockState(state);
        }
    }

    /**
     * @author youyihj
     */
    public static class Deserializer implements JsonDeserializer<Multiblock> {
        @SuppressWarnings("deprecation")
        @Override
        public Multiblock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonArray()) {
                Multiblock multiblock = new Multiblock();
                JsonArray array = json.getAsJsonArray();
                array.forEach(it -> {
                    JsonObject object = it.getAsJsonObject();
                    int x = object.get("x").getAsInt();
                    int y = object.get("y").getAsInt();
                    int z = object.get("z").getAsInt();
                    JsonArray elements1 = object.get("elements").getAsJsonArray();
                    List<IBlockState> states = new ArrayList<>();
                    elements1.forEach(it1 -> {
                        String string = it1.getAsString();
                        String[] split = string.split("@");
                        Block block = Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(split[0])));
                        if (split.length == 2) {
                            states.add(block.getStateFromMeta(Integer.parseInt(split[1])));
                        } else {
                            states.addAll(block.getBlockState().getValidStates());
                        }
                    });
                    multiblock.addBlock(new Vec3i(x, y, z), new Element(states));
                });
                return multiblock;
            } else {
                throw new JsonParseException("must be an array!");
            }
        }
    }
}
