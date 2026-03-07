package codes.viremox.salute.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public final class TextureCodec {

    private TextureCodec() {}

    public static String compact(String base64Value) {
        return extractSkinUrl(base64Value)
                .map(TextureCodec::encodeMinimal)
                .orElse(base64Value);
    }

    private static Optional<String> extractSkinUrl(String base64Value) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64Value);
            JsonObject root = JsonParser.parseString(new String(decoded, StandardCharsets.UTF_8)).getAsJsonObject();
            String url = root.getAsJsonObject("textures")
                    .getAsJsonObject("SKIN")
                    .get("url")
                    .getAsString();
            return Optional.of(url);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static String encodeMinimal(String skinUrl) {
        JsonObject skin = new JsonObject();
        skin.addProperty("url", skinUrl);
        JsonObject textures = new JsonObject();
        textures.add("SKIN", skin);
        JsonObject root = new JsonObject();
        root.add("textures", textures);
        return Base64.getEncoder().encodeToString(root.toString().getBytes(StandardCharsets.UTF_8));
    }
}
