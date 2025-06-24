package net.infinite1274.helldivers.entity.client;

import net.infinite1274.helldivers.HelldiversMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation MISSILE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "missile"), "main");

    public static final ModelLayerLocation STRATAGEM_ORB = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "stratagem_orb"), "main");

    public static final ModelLayerLocation HELLPOD = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "hellpod"), "main");
}
