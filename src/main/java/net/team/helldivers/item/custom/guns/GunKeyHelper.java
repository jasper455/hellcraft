package net.team.helldivers.item.custom.guns;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SShootPacket;
import net.team.helldivers.network.SStartShootPacket;
import net.team.helldivers.network.SStopShootPacket;
import net.team.helldivers.util.KeyBinding;

@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GunKeyHelper {

    private static boolean wasShooting = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!(mc.player.getMainHandItem().getItem() instanceof AbstractGunItem)) return;
        boolean shootingNow = KeyBinding.SHOOT.isDown();

        if (shootingNow && !wasShooting) {
            // Key just presseds
            PacketHandler.sendToServer(new SStartShootPacket());
            System.out.println("startshoot");
        } 
        else if (!shootingNow && wasShooting) {
            // Key just released
            PacketHandler.sendToServer(new SStopShootPacket());
            System.out.println("endshooting");
        }

        wasShooting = shootingNow;
    }
}
