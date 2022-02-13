package youyihj.herodotusutils.modsupport.bloodmagic;

import WayofTime.bloodmagic.altar.AltarTier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import youyihj.herodotusutils.HerodotusUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author youyihj
 */
public class BloodAltarStructures {
    public static final Map<AltarTier, BlockArray> STRUCTURES = new EnumMap<>(AltarTier.class);

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
    }
}
