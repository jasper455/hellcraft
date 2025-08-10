package net.team.helldivers.client.model.block;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.block.entity.custom.GalacticTerminalBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class GalacticTerminalBlockModel extends DefaultedBlockGeoModel<GalacticTerminalBlockEntity> {
    public GalacticTerminalBlockModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "galactic_terminal"));
    }
}
