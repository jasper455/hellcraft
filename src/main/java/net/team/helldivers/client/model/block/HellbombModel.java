package net.team.helldivers.client.model.block;

import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.entity.custom.HellbombBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class HellbombModel extends DefaultedBlockGeoModel<HellbombBlockEntity> {
    public HellbombModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "hellbomb"));
    }
}
