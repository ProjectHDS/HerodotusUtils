package youyihj.herodotusutils.modsupport.bloodmagic;

import WayofTime.bloodmagic.altar.AltarTier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import youyihj.herodotusutils.HerodotusUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
public class BloodAltarStructures {
    public static final Map<AltarTier, BlockArray> STRUCTURES = new EnumMap<>(AltarTier.class);
    public static final Map<AltarTier, List<ItemStack>> STRUCTURE_ITEMS = new EnumMap<>(AltarTier.class);

    public static void loadStructures() {
        Gson gson = new GsonBuilder().registerTypeAdapter(DynamicMachine.class, new DynamicMachine.MachineDeserializer()).create();
        for (AltarTier tier : AltarTier.values()) {
            if (tier == AltarTier.ONE) continue;
            try {
                Reader reader = new InputStreamReader(new FileInputStream("config/hdsutils/blood_altar_" + tier.toInt() +".json"), StandardCharsets.UTF_8);
                STRUCTURES.put(tier, gson.fromJson(reader, DynamicMachine.class).getPattern());
            } catch (IOException e) {
                HerodotusUtils.logger.error("cannot read blood altar structures", e);
            }
        }
        STRUCTURES.forEach(((tier, blockArray) -> {
            List<ItemStack> items = blockArray.getPattern().values().stream()
                    .map(it -> it.getSampleState(Optional.of(0L)))
                    .map(BloodAltarStructures::getItemFromBlockState)
                    .collect(Collectors.groupingBy(ItemMeta::new, Collectors.summingInt(ItemStack::getCount)))
                    .entrySet()
                    .stream()
                    .map(entry -> new ItemStack(entry.getKey().getItem(), entry.getValue(), entry.getKey().getMeta()))
                    .collect(Collectors.toList());
            STRUCTURE_ITEMS.put(tier, items);
            HerodotusUtils.logger.info(tier + ": " + items.stream().map(ItemStack::toString).collect(Collectors.joining(", ")));
        }));
    }

    private static ItemStack getItemFromBlockState(IBlockState state) {
        Block block = state.getBlock();
        return new ItemStack(Item.getItemFromBlock(block), 1, block.damageDropped(state));
    }

    private static class ItemMeta {
        private final Item item;
        private final int meta;

        public ItemMeta(Item item, int meta) {
            this.item = item;
            this.meta = meta;
        }

        public ItemMeta(ItemStack stack) {
            this(stack.getItem(), stack.getMetadata());
        }

        public Item getItem() {
            return item;
        }

        public int getMeta() {
            return meta;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemMeta itemMeta = (ItemMeta) o;
            return meta == itemMeta.meta && Objects.equals(item, itemMeta.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, meta);
        }
    }
}
