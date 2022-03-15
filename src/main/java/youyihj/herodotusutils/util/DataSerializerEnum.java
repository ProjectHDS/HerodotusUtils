package youyihj.herodotusutils.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youyihj
 */
public class DataSerializerEnum<T extends Enum<T>> implements DataSerializer<T> {
    private final Class<T> clazz;
    private static final Map<Class<?>, DataSerializerEnum<?>> createdSerializers = new HashMap<>();

    private DataSerializerEnum(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public static <T extends Enum<T>> DataSerializerEnum<T> of(Class<T> clazz) {
        DataSerializerEnum<?> serializer = createdSerializers.get(clazz);
        if (serializer != null) {
            return ((DataSerializerEnum<T>) serializer);
        } else {
            DataSerializerEnum<T> newSerializer = new DataSerializerEnum<>(clazz);
            createdSerializers.put(clazz, newSerializer);
            DataSerializers.registerSerializer(newSerializer);
            return newSerializer;
        }
    }

    @Override
    public void write(PacketBuffer buf, T value) {
        buf.writeVarInt(value.ordinal());
    }

    @Override
    public T read(PacketBuffer buf) {
        return clazz.getEnumConstants()[buf.readVarInt()];
    }

    @Override
    public DataParameter<T> createKey(int id) {
        return new DataParameter<>(id, this);
    }

    @Override
    public T copyValue(T value) {
        return value;
    }
}
