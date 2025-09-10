package net.team.helldivers.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.team.helldivers.HelldiversMod;

public class ModTags {
    public static class Entities {
        public static final TagKey<EntityType<?>> AUTOMATONS = tag("automatons");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(name));
        }

    }
}
