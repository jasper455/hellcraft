package net.team.helldivers.screen;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import net.team.helldivers.screen.custom.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, HelldiversMod.MOD_ID);

    public static final RegistryObject<MenuType<StratagemSelectMenu>> EXTRACTION_TERMINAL =
            registerMenuType("extraction_terminal", StratagemSelectMenu::new);

    public static final RegistryObject<MenuType<HellbombInputMenu>> HELLBOMB_INPUT_MENU =
            registerMenuType("hellbomb", HellbombInputMenu::new);

    public static final RegistryObject<MenuType<SupportHellpodMenu>> SUPPORT_HELLPOD_MENU =
            MENUS.register("support_hellpod_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        // Read the entity ID from the packet
                        int entityId = data.readInt();
                        Level level = inv.player.level();
                        Entity entity = level.getEntity(entityId);

                        if (entity instanceof SupportHellpodEntity hellpodEntity) {
                            return new SupportHellpodMenu(windowId, inv, hellpodEntity);
                        }

                        throw new IllegalStateException("Invalid entity type for menu!");
                    }));

    public static final RegistryObject<MenuType<HellbombEntityInputMenu>> HELLBOMB_ENTITY_INPUT_MENU =
            MENUS.register("hellbomb_input_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        Level level = inv.player.level();
                        Entity entity = level.getEntity(entityId);

                        if (entity instanceof HellbombHellpodEntity hellbombEntity) {
                            return new HellbombEntityInputMenu(windowId, inv, hellbombEntity);
                        }

                        throw new IllegalStateException("Invalid entity type for menu!");
                    }));

    public static final RegistryObject<MenuType<GalaxyMapMenu>> GALAXY_MAP_MENU =
            registerMenuType("galaxy_map", GalaxyMapMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
