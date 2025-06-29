package net.team.helldivers.item;

import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.ModBlocks;
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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.B01_HELMET.get()))
                    .title(Component.translatable("creativetab.helldivers"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.EFFECT_TESTER.get());
                        output.accept(ModItems.STRATAGEM_ORB.get());
                        output.accept(ModItems.B01_HELMET.get());
                        output.accept(ModItems.B01_CHESTPLATE.get());
                        output.accept(ModItems.B01_LEGGINGS.get());
                        output.accept(ModItems.B01_BOOTS.get());
                        output.accept(ModBlocks.HELLBOMB.get());
                        output.accept(ModItems.STRATAGEM_PICKER.get());
                        output.accept(ModItems.AR23.get());
                    }).build());
    public static final RegistryObject<CreativeModeTab> STRATAGEMS_TAB = CREATIVE_MODE_TABS.register("stratagems_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.PRECISION_STRIKE.get()))
                    .title(Component.translatable("creativetab.stratagems"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.HELLBOMB_ITEM.get());
                        output.accept(ModItems.SMALL_BARRAGE.get());
                        output.accept(ModItems.BIG_BARRAGE.get());
                        output.accept(ModItems.PRECISION_STRIKE.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
