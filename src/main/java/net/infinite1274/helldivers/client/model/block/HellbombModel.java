package net.infinite1274.helldivers.client.model.block;

import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.block.entity.custom.HellbombBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class HellbombModel extends DefaultedBlockGeoModel<HellbombBlockEntity> {
    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "workbench/sawmill")
     * }</pre>
     *
     */
    public HellbombModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "hellbomb"));
    }
}
