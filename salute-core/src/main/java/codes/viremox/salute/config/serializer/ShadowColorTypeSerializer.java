package codes.viremox.salute.config.serializer;

import net.kyori.adventure.text.format.ShadowColor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class ShadowColorTypeSerializer implements TypeSerializer<ShadowColor> {

    public static final ShadowColorTypeSerializer INSTANCE = new ShadowColorTypeSerializer();

    private ShadowColorTypeSerializer() {}

    @Override
    public ShadowColor deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String raw = node.getString();
        if (raw == null || raw.isBlank()) return null;

        String cleaned = raw.startsWith("#") ? raw.substring(1) : raw;

        try {
            int argb = (int) Long.parseUnsignedLong(cleaned, 16);
            return ShadowColor.shadowColor(argb);
        } catch (NumberFormatException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void serialize(Type type, @Nullable ShadowColor color, ConfigurationNode node) throws SerializationException {
        if (color == null) {
            node.raw(null);
            return;
        }
        node.set("#" + String.format("%08X", color.value()));
    }
}
