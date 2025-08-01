package net.team.helldivers.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.team.helldivers.HelldiversMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HelldiversMod.MOD_ID);

    public static final RegistryObject<SoundEvent> STRATAGEM_INPUT = registerSoundEvent("stratagem_input");

    public static final RegistryObject<SoundEvent> FALLING_SHELL = registerSoundEvent("falling_shell");

    public static final RegistryObject<SoundEvent> EXPLOSION = registerSoundEvent("explosion");

    public static final RegistryObject<SoundEvent> STRATAGEM_ACTIVATE = registerSoundEvent("stratagem_activate");

    public static final RegistryObject<SoundEvent> FIRE_ORBITAL_STRIKE = registerSoundEvent("firing_orbital_strike");

    public static final RegistryObject<SoundEvent> HELLBOMB_ARMED = registerSoundEvent("hellbomb_armed");

    public static final RegistryObject<SoundEvent> HELLBOMB_EXPLOSION = registerSoundEvent("hellbomb_explosion");

    public static final RegistryObject<SoundEvent> CLEAR_THE_AREA = registerSoundEvent("clear_the_area");

    public static final RegistryObject<SoundEvent> STRATAGEM_FAIL_INPUT = registerSoundEvent("stratagem_fail_input");

    public static final RegistryObject<SoundEvent> STRATAGEM_MENU_OPEN = registerSoundEvent("stratagem_menu_open");

    public static final RegistryObject<SoundEvent> STRATAGEM_MENU_CLOSE = registerSoundEvent("stratagem_menu_close");

    public static final RegistryObject<SoundEvent> STRATAGEM_ORB_LAND = registerSoundEvent("stratagem_orb_land");

    public static final RegistryObject<SoundEvent> GUN_EMPTY = registerSoundEvent("gun_empty");

    public static final RegistryObject<SoundEvent> AR_23_RELOAD = registerSoundEvent("ar23_reload");

    public static final RegistryObject<SoundEvent> AR_23_SHOOT = registerSoundEvent("ar23_shoot");

    public static final RegistryObject<SoundEvent> EAT_FIRE = registerSoundEvent("eat_fire");

    public static final RegistryObject<SoundEvent> P2_SHOOT = registerSoundEvent("p2_shoot");

    public static final RegistryObject<SoundEvent> PLAS1_SHOOT = registerSoundEvent("plas1_shoot");

    public static final RegistryObject<SoundEvent> PLAS1_RELOAD = registerSoundEvent("plas1_reload");

    public static final RegistryObject<SoundEvent> ORBITAL_LASER_IDLE = registerSoundEvent("orbital_laser_idle");

    public static final RegistryObject<SoundEvent> EASTER_EGG = registerSoundEvent("easter_egg");

    public static final RegistryObject<SoundEvent> EAGLE_FLYBY = registerSoundEvent("eagle_flyby");

    public static final RegistryObject<SoundEvent> ORBITAL_STRIKE_INCOMING = registerSoundEvent("orbital_strike_incoming");

    public static final RegistryObject<SoundEvent> ORBITAL_BARRAGE_CLEAR_THE_AREA = registerSoundEvent("orbital_barrage_clear_the_area");

    public static final RegistryObject<SoundEvent> ORBITAL_BARRAGE_STAND_CLEAR = registerSoundEvent("orbital_barrage_stand_clear");

    public static final RegistryObject<SoundEvent> PLANET_SELECT = registerSoundEvent("planet_select");

    public static final RegistryObject<SoundEvent> SG225_SHOOT = registerSoundEvent("sg225_shoot");

    public static final RegistryObject<SoundEvent> STALWART_SHOOT = registerSoundEvent("stalwart_shoot");

    public static final RegistryObject<SoundEvent> AMR_SHOOT = registerSoundEvent("amr_shoot");

    public static final RegistryObject<SoundEvent> AMR_RELOAD = registerSoundEvent("amr_reload");

    public static final RegistryObject<SoundEvent> TRAP_ROYALTY = registerSoundEvent("trap_royalty");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }


    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
