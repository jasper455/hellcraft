package net.team.helldivers.event;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.server.command.ConfigCommand;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.backslot.PlayerBackSlot;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.client.skybox.SkyboxRenderer;
import net.team.helldivers.command.StopUseLodestoneCommand;
import net.team.helldivers.command.UseLodestoneCommand;
import net.team.helldivers.damage.ModDamageSources;
import net.team.helldivers.damage.ModDamageTypes;
import net.team.helldivers.data.StructureGenerationData;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.entity.custom.BulletProjectileEntity;
import net.team.helldivers.entity.custom.bots.AbstractBotEntity;
import net.team.helldivers.helper.ClientBackSlotCache;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.custom.armor.IDemocracyProtects;
import net.team.helldivers.item.custom.armor.IHelldiverArmorItem;
import net.team.helldivers.network.CHitMarkPacket;
import net.team.helldivers.item.custom.backpacks.AbstractBackpackItem;
import net.team.helldivers.item.custom.backpacks.ShieldPackItem;
import net.team.helldivers.network.CSyncBackSlotPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SSetBackSlotPacket;
import net.team.helldivers.network.SSyncJammedPacket;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.sound.custom.MovingSoundInstance;
import net.team.helldivers.util.KeyBinding;
import net.team.helldivers.worldgen.dimension.ModDimensions;
import net.team.lodestone.systems.rendering.VFXBuilders;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static int superDestroyerDimTicks = 0;

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "properties"), new PlayerBackSlotProvider());
            }
        }
    }
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerBackSlot.class);
    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new StopUseLodestoneCommand(event.getDispatcher());
        new UseLodestoneCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getEntity().getPersistentData().putBoolean("helldivers.useLodestone",
                    event.getOriginal().getPersistentData().getBoolean("helldivers.useLodestone"));

            event.getOriginal().getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }

    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        boolean hasArmorEquipped = false;
        ItemStack foundItem = ItemStack.EMPTY;

        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.getItem() instanceof IDemocracyProtects) {
                hasArmorEquipped = true;
                foundItem = stack;
                break;
            }
        }

        if (hasArmorEquipped && player.isDeadOrDying() && player.level().getRandom().nextBoolean()) {
            event.setCanceled(true);
            player.setHealth(0.5f);
        }
        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ItemStackHandler handler = backSlot.getInventory();
            ItemStack backSlotItem = handler.getStackInSlot(0);
            if (!backSlotItem.isEmpty() && !backSlotItem.is(ModItems.PORTABLE_HELLBOMB.get())) {
                ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), backSlotItem.copyAndClear());
                player.level().addFreshEntity(itemEntity);
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        Entity vehicle = player.getVehicle();
        if (vehicle != null && vehicle.getType() == ModEntities.HELLPOD.get()) {
            event.setCanceled(true); // Prevent damage
        }

        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ItemStackHandler handler = backSlot.getInventory();
            ItemStack backSlotItem = handler.getStackInSlot(0);

            if (backSlotItem.getItem() instanceof ShieldPackItem) {
                if (backSlotItem.getDamageValue() <= backSlotItem.getMaxDamage() - 1) {
                    if (event.getSource().is(DamageTypeTags.IS_PROJECTILE) || event.getSource().is(ModDamageTypes.RAYCAST)) {
                        backSlotItem.hurt(((int) event.getAmount()), player.getRandom(), player);
                        event.setCanceled(true);
                    }
                }
            }
            // Sync back to client
            CompoundTag tag = new CompoundTag();
            backSlot.saveNBTData(tag);
            PacketHandler.sendToPlayer(new CSyncBackSlotPacket(tag), player);
        });
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
    DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            System.out.println(source.getEntity());//this is null when any of the mod explosions damages somthing. not null when a player ignites a tnt with flint and steel
            if(source.getEntity() instanceof Player player){
                System.out.println("boom");
                if(!player.level().isClientSide){
                    PacketHandler.sendToPlayer(new CHitMarkPacket(), ((ServerPlayer)player));
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ItemStackHandler handler = backSlot.getInventory();
            ItemStack backSlotItem = handler.getStackInSlot(0);
            if (backSlotItem.getItem() instanceof ShieldPackItem shieldPackItem) {
                if (backSlotItem.getDamageValue() == backSlotItem.getMaxDamage()) {
                    if (!player.getCooldowns().isOnCooldown(shieldPackItem)) {
                        player.getCooldowns().addCooldown(shieldPackItem, 1200);
                    } else {
                        player.sendSystemMessage(Component.literal("test"));
                        backSlotItem.setDamageValue(0);
                    }
                }
            }
        });

        if (KeyBinding.USE_BACKPACK.consumeClick()) {
            // Send request to server to toggle/swap back slot
            player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
                ItemStackHandler handler = backSlot.getInventory();
                ItemStack backSlotItem = handler.getStackInSlot(0);
                if (backSlotItem.getItem() instanceof AbstractBackpackItem backpackItem) {
                    backpackItem.onUse(player);
                }
            });
        }

        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0 &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem) {
            Level level = player.level();

            AtomicBoolean isJammed = new AtomicBoolean(false);

            // Define radius and center search position (e.g., player's position)
            BlockPos playerPos = player.blockPosition();
            int radius = 50;

            // Scan nearby blocks in a cube
            BlockPos.betweenClosedStream(playerPos.offset(-radius, -radius, -radius), playerPos.offset(radius, radius, radius))
                    .forEach(pos -> {
                        if (level.getBlockState(pos).is(ModBlocks.STRATAGEM_JAMMER.get())) {
                            isJammed.set(true);
                        }
                    });

            CompoundTag persistentData = player.getPersistentData();
            CompoundTag data = persistentData.getCompound(Player.PERSISTED_NBT_TAG); // "ForgeData"

            if (isJammed.get()) {
                if (!data.getBoolean("near_my_block")) {
                    data.putBoolean("near_my_block", true);
                    PacketHandler.sendToServer(new SSyncJammedPacket(true));
                }
            } else {
                if (data.contains("near_my_block")) {
                    data.remove("near_my_block");
                    PacketHandler.sendToServer(new SSyncJammedPacket(false));
                }
            }

            // Write back to persistent tag
            persistentData.put(Player.PERSISTED_NBT_TAG, data);
        }
        if (player.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            if (superDestroyerDimTicks % 7280 == 0 || superDestroyerDimTicks == 0) {
                Minecraft.getInstance().getSoundManager()
                        .play(new MovingSoundInstance(player, ModSounds.SUPER_DESTROYER_AMBIENT.get(), 1f, true));
            }
            superDestroyerDimTicks++;
        } else {
            superDestroyerDimTicks = 0;
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        GuiGraphics guiGraphics = event.getGuiGraphics();

        if (player == null) return;

        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ItemStackHandler handler = backSlot.getInventory();
            ItemStack stack = handler.getStackInSlot(0);

            if (!stack.isEmpty()) {
                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();

                // Base hotbar dimensions
                int hotbarWidth = 182;
                int hotbarHeight = 22;

                // Calculate the left edge of the hotbar (it's centered)
                int hotbarX = (screenWidth - hotbarWidth) / 2;
                int hotbarY = screenHeight - hotbarHeight - 1;

                // Determine position to the right of hotbar (assume main hand is RIGHT)
                int iconX;
                if (player.getMainArm() == HumanoidArm.RIGHT) {
                    // If main hand is right, offhand shows on left, so we place our icon on the right
                    iconX = hotbarX + hotbarWidth + 4; // 4 pixels of padding
                } else {
                    // If main hand is left, offhand shows on right, so we place our icon on the left
                    iconX = hotbarX - 22 - 4; // 22 = icon width, 4px padding
                }

                int iconY = hotbarY + 1; // align vertically with hotbar

                guiGraphics.blit(
                        ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/gui/backslot.png"),
                        iconX, iconY, 22, 22, 22, 22, 22, 22, 22, 22
                );
                guiGraphics.renderItem(stack, iconX + 3, iconY + 3);
                guiGraphics.renderItemDecorations(mc.font, stack, iconX + 3, iconY + 3);
//                Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(stack.getDamageValue())));
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerLevel level = player.getServer().getLevel(event.getTo());
        ResourceLocation super_destroyer_sec1 = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "super_destroyer/super_destroyer_sec1");
        StructureTemplate template1 = level.getStructureManager().get(super_destroyer_sec1).orElse(null);

        ResourceLocation super_destroyer_sec2 = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "super_destroyer/super_destroyer_sec2");
        StructureTemplate template2 = level.getStructureManager().get(super_destroyer_sec2).orElse(null);

        ResourceLocation super_destroyer_sec3 = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "super_destroyer/super_destroyer_sec3");
        StructureTemplate template3 = level.getStructureManager().get(super_destroyer_sec3).orElse(null);

        ResourceKey<Level> fromDim = event.getFrom();
        ResourceKey<Level> toDim = event.getTo();

        if (toDim.equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            StructureGenerationData data = StructureGenerationData.get(level);

            if (!data.hasGenerated()) {
                BlockPos pos1 = new BlockPos(-29, 4, -12);
                BlockPos pos2 = new BlockPos(18, 4, -12);
                BlockPos pos3 = new BlockPos(66, 4, -12);
                for (int x = 0; x < 33; x++) {
                    for (int z = 0; z < 33; z++) {
                        BlockPos targetPos = new BlockPos(x - 8, 3, z - 8);

                        level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }

                if (template1 != null) {
                    template1.placeInWorld(level,
                            pos1,
                            pos1,
                            new StructurePlaceSettings(),
                            level.getRandom(),
                            2);
                }

                if (template2 != null) {
                    template2.placeInWorld(level,
                            pos2,
                            pos2,
                            new StructurePlaceSettings(),
                            level.getRandom(),
                            2);
                }

                if (template3 != null) {
                    template3.placeInWorld(level,
                            pos3,
                            pos3,
                            new StructurePlaceSettings(),
                            level.getRandom(),
                            2);
                }
                data.markGenerated();
                data.setDirty();
            }
        }

        CompoundTag data = player.getPersistentData();
        CompoundTag persisted = data.getCompound(Player.PERSISTED_NBT_TAG);

        if (toDim.equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            if (!persisted.contains("PreviousGameMode")) {
                persisted.putInt("PreviousGameMode", player.gameMode.getGameModeForPlayer().getId());
            }

            player.setGameMode(GameType.ADVENTURE);
        }

        if (fromDim.equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            if (persisted.contains("PreviousGameMode")) {
                int modeId = persisted.getInt("PreviousGameMode");
                GameType prevMode = GameType.byId(modeId);
                player.setGameMode(prevMode);
                persisted.remove("PreviousGameMode");
            }
        }

        data.put(Player.PERSISTED_NBT_TAG, persisted);
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        List<Entity> affectedEntities = event.getAffectedEntities();
        for (Entity entity : affectedEntities) {
            if (entity instanceof AbstractBotEntity customEntity) {
                // Cancel velocity from explosion
                customEntity.setDeltaMovement(new Vec3(0, -2, 0)); // or limit Y motion
            }
        }
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;


        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        Camera camera = event.getCamera();

        ResourceLocation[] starsLayer = new ResourceLocation[] {
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/stars/pz.png"),   // North
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/stars/px.png"),   // East
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/stars/nz.png"),    // South
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/stars/nx.png"),    // West
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/stars/py.png"),     // Up
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/stars/ny.png")   // Down
        };

        ResourceLocation[] spaceLayer = new ResourceLocation[] {
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/sky1/pz.png"),   // North
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/sky1/px.png"),   // East
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/sky1/nz.png"),    // South
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/sky1/nx.png"),    // West
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/sky1/py.png"),     // Up
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/sky1/ny.png")   // Down
        };

        ResourceLocation[] fogLayer = new ResourceLocation[] {
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/cloudy_sky/pz.png"),   // North
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/cloudy_sky/px.png"),   // East
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/cloudy_sky/nz.png"),    // South
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/cloudy_sky/nx.png"),    // West
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/cloudy_sky/py.png"),     // Up
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/cloudy_sky/ny.png")   // Down
        };


        if (camera.getEntity().level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            // Lower rotation speed makes it rotate faster
            SkyboxRenderer.renderSkybox(poseStack, 100, 255, starsLayer, Axis.XN, Axis.YN, 30);
            SkyboxRenderer.renderSkybox(poseStack, 110, 125, spaceLayer, Axis.XP, Axis.YP, 20);
            SkyboxRenderer.renderSkybox(poseStack, 120, 75, fogLayer, Axis.YP, Axis.ZP, 20);
        } else if (camera.getEntity().level().dimension().equals(ModDimensions.CHOEPESSA_DIM)) {
            SkyboxRenderer.renderSkybox(poseStack, 100, 125, starsLayer, Axis.XN, Axis.YN, 30);
        }
    }
}
