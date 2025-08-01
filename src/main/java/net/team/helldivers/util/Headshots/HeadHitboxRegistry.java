package net.team.helldivers.util.Headshots;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HeadHitboxRegistry {
    
    private static Map<String, HeadHitbox> hitboxes = new HashMap<>();

    public static HeadHitbox get(String id) {
        return hitboxes.get(id);
    }

    public static Map<String, HeadHitbox> getAll() {
        return hitboxes;
    }

    public static void Register() {
        InputStream stream = HeadHitboxRegistry.class.getResourceAsStream("/data/helldivers/HeadLocations.json");
        if (stream == null) {
            System.err.println("HeadLocations.json not found.");
            return;
        }

        HeadHitboxLoader loader = new HeadHitboxLoader();
        Map<String, HeadHitboxRaw> rawMap = loader.loadFromJson(stream);
        if (rawMap == null) return;

        Map<String, HeadHitbox> converted = new HashMap<>();
        for (Map.Entry<String, HeadHitboxRaw> entry : rawMap.entrySet()) {
            converted.put(entry.getKey(), entry.getValue().toHeadHitbox());
        }
        
        hitboxes = converted;
        System.out.println("Head hitboxes loaded: " + hitboxes.keySet());
    }
}

