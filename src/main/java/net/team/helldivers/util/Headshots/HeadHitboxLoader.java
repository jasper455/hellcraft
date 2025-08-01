package net.team.helldivers.util.Headshots;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

public class HeadHitboxLoader {
    private static final Gson GSON = new Gson();
    public Map<String, HeadHitboxRaw> loadFromJson(InputStream stream) {
        try (InputStreamReader reader = new InputStreamReader(stream)) {
            Type mapType = new TypeToken<Map<String, HeadHitboxRaw>>(){}.getType();
            Map<String, HeadHitboxRaw> map = GSON.fromJson(reader, mapType);
            return map;
        } catch (Exception e) {
            System.err.println("Failed to load head hitboxes:");
            e.printStackTrace();
            return null;
        }
    }
}
