package net.team.helldivers.client.model.block;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class ExtractionTerminalBlockModel extends DefaultedBlockGeoModel<ExtractionTerminalBlockEntity> {
    public ExtractionTerminalBlockModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "extraction_terminal"));
    }
}
