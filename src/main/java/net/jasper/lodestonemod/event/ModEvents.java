package net.jasper.lodestonemod.event;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onChatEvent(ClientChatReceivedEvent event) {

    }

//    @SubscribeEvent
//    public static void setCanFly(PlayerEvent event) {
//        if (event.getEntity().isCreative()){
//            event.getEntity().getAbilities().mayfly = true;
//            event.getEntity().getAbilities().setFlyingSpeed(0.2f);
//            event.getEntity().onUpdateAbilities();
//        } else {
//            event.getEntity().getAbilities().mayfly = false;
//            event.getEntity().onUpdateAbilities();
//        }
//    }


}
