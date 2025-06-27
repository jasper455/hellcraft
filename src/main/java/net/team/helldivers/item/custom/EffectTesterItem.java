package net.team.helldivers.item.custom;


import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.shader.post.tint.TintPostProcessor;
import net.team.helldivers.entity.custom.MissileProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.awt.*;

@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EffectTesterItem extends Item {
    private static float flashTime = 0;
    private static boolean isFlashed = false;

    public EffectTesterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Vec3 pos = pContext.getClickLocation();

            ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(20)
                    .setEasing(Easing.SINE_IN_OUT)
                    .setIntensity(5f));
            triggerExplosionEffect(pContext.getLevel(), pContext.getClickLocation());
//        TintPostProcessor.INSTANCE.setActive(true);
        isFlashed = true;
//        PacketHandler.sendToServer(new SSphereExplosionPacket(pContext.getClickedPos(), 90));
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        for (int i = 0; i < 5; i++) {
            float randomPosX = (Mth.randomBetween(pLevel.getRandom(), 85.0f, 95.0f));
            float randomPosY = (Mth.randomBetween(pLevel.getRandom(), -5.0f, 5.0f));
            float randomPosZ = Mth.randomBetween(pLevel.getRandom(), -5.0f, 5.0f);

            float randomMoveX = Mth.randomBetween(pLevel.getRandom(), -1.8f, -1.5f);
            float randomMoveZ = Mth.randomBetween(pLevel.getRandom(), -0.125f, 0.125f);

            MissileProjectileEntity explosive = new MissileProjectileEntity(pPlayer, pLevel);
            explosive.setPos(pPlayer.getX() + randomPosX, 200 + randomPosY, pPlayer.getZ() + randomPosZ);
            explosive.setDeltaMovement(randomMoveX, 0f, randomMoveZ);
            pLevel.addFreshEntity(explosive);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static void triggerExplosionEffect(Level level, Vec3 pos) {
        RandomSource random = level.getRandom();

        // Smoke
        for (int i = 0; i < 30; i++) {
            float angle = random.nextFloat() * ((float) Math.PI * 2);
            float speed = 0.1f + random.nextFloat() * 0.05f;
            float dx = Mth.cos(angle) * speed;
            float dz = Mth.sin(angle) * speed;
            float dy = 0.05f + random.nextFloat() * 0.05f;

            // Blast particle outer
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(25f, 50f).build())
                    .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39, 200)).build())
                    .addMotion(dx / 1.5, dy, dz / 1.5)
                    .setLifetime(300)
                    .spawn(level, pos.x, pos.y, pos.z);
            // Blast particle inner
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(12.5f, 25f).build())
                    .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                    .setLifetime(300)
                    .addMotion(dx / 2, dy, dz / 2)
                    .spawn(level, pos.x, pos.y, pos.z);

            // Flash Particle
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(15f, 87.5f).build())
                    .setTransparencyData(GenericParticleData.create(0.05f, 0.02f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39)).build())
                    .setLifetime(300)
                    .addMotion(dx * 4, dy, dz * 4)
                    .spawn(level, pos.x, pos.y, pos.z);

        }
        // Light Flash Particle (yk what I mean)
        WorldParticleBuilder.create(LodestoneParticleRegistry.SPARKLE_PARTICLE)
                .setScaleData(GenericParticleData.create(250f, 2.5f).build())
                .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                .setLifetime(20)
                .addMotion(0, 0, 0)
                .spawn(level, pos.x, pos.y, pos.z);

//        ModClientEvents.triggerFlashEffect(0.00005f);
        TintPostProcessor.INSTANCE.setActive(false);
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(level.isClientSide())));
    }

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        if (isFlashed) {
            flashTime++;
        }

        if (flashTime >= 500) {
            isFlashed = false;
            flashTime = 0;
            TintPostProcessor.INSTANCE.setActive(false);
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        for (int i = 0; i < 30; i++) {
            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                    .setScaleData(GenericParticleData.create(5f, 0f).build())
                    .setTransparencyData(GenericParticleData.create(0.35f, 0f).build())
                    .setColorData(ColorParticleData.create(
                            new Color(0x202020),
                            new Color(0x101010)
                    ).setCoefficient(0.5f).build())
                    .enableNoClip()
                    .setForceSpawn(true)
                    .setLifetime(40)
                    .addMotion(0, 0.02f, 0);


        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
