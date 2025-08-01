package net.team.helldivers.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class StructureGenerationData extends SavedData {
    private static final String NAME = "structure_generated";
    private boolean generated = false;

    public static StructureGenerationData get(Level level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
            tag -> {
                StructureGenerationData data = new StructureGenerationData();
                data.generated = tag.getBoolean("Generated");
                return data;
            },
            StructureGenerationData::new,
            NAME
        );
    }

    public boolean hasGenerated() {
        return generated;
    }

    public void markGenerated() {
        this.generated = true;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Generated", generated);
        return tag;
    }
}