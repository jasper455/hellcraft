package net.team.helldivers.event;
import java.util.concurrent.atomic.AtomicBoolean;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.client.skybox.SkyboxRenderer;
import net.team.helldivers.command.StopUseLodestoneCommand;
import net.team.helldivers.command.UseLodestoneCommand;
import net.team.helldivers.data.StructureGenerationData;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.entity.custom.BulletProjectileEntity;
import net.team.helldivers.item.custom.armor.IDemocracyProtects;
import net.team.helldivers.item.custom.armor.IHelldiverArmorItem;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SSyncJammedPacket;
import net.team.helldivers.util.KeyBinding;
import net.team.helldivers.worldgen.dimension.ModDimensions;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    private static final SkyboxRenderer SKYBOX_RENDERER = new SkyboxRenderer();
    @SubscribeEvent
    public static void levelRenderEvent(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        Level level = minecraft.level;

//        SKYBOX_RENDERER.render(event.getPoseStack());

    }



    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new StopUseLodestoneCommand(event.getDispatcher());
        new UseLodestoneCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getEntity().getPersistentData().putBoolean("helldivers.useLodestone",
                event.getOriginal().getPersistentData().getBoolean("helldivers.useLodestone"));

        event.getOriginal().getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(oldCap -> {
            event.getEntity().getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(newCap -> {
                for (int i = 0; i < oldCap.getSlots(); i++) {
                    newCap.insertItem(i, oldCap.getStackInSlot(i), false);
                }
            });
        });
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
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        Entity vehicle = player.getVehicle();
        if (vehicle != null && vehicle.getType() == ModEntities.HELLPOD.get()) {
            event.setCanceled(true); // Prevent damage
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

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
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerLevel level = player.getServer().getLevel(event.getTo());
        ResourceLocation iglooTop = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "fossils/fossil1");
        StructureTemplate template = level.getStructureManager().get(iglooTop).orElse(null);

        ResourceKey<Level> fromDim = event.getFrom();
        ResourceKey<Level> toDim = event.getTo();

        if (toDim.equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            StructureGenerationData data = StructureGenerationData.get(level);

            if (!data.hasGenerated()) {
                BlockPos pos = new BlockPos(0, 64, 0);

                if (template != null) {
                    template.placeInWorld(level,
                            pos,
                            pos,
                            new StructurePlaceSettings(),
                            level.getRandom(),
                            2);
                }
                BulletProjectileEntity bullet = new BulletProjectileEntity(player, level, false, false);
                bullet.setPos(2, 8, 2);
                bullet.setDeltaMovement(0, 0, 0);
                bullet.setNoGravity(true);
                level.addFreshEntity(bullet);

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
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;


        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        Camera camera = event.getCamera();

        if (camera.getEntity().level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            SkyboxRenderer.renderSkybox(poseStack);
        }
    }
}
