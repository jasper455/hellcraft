package net.infinite1274.helldivers.item;

import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HelldiversMod.MOD_ID);
    public static final RegistryObject<CreativeModeTab> HELLDIVERS_TAB = CREATIVE_MODE_TABS.register("helldivers_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.HELLDIVER_HELMET.get()))
                    .title(Component.translatable("creativetab.helldivers"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.EFFECT_TESTER.get());
                        output.accept(ModItems.STRATAGEM_ORB.get());
                        output.accept(ModItems.HELLDIVER_HELMET.get());
                        output.accept(ModItems.HELLDIVER_CHESTPLATE.get());
                        output.accept(ModItems.HELLDIVER_LEGGINGS.get());
                        output.accept(ModItems.HELLDIVER_BOOTS.get());
                        output.accept(ModBlocks.HELLBOMB.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
