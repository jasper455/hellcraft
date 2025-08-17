package net.team.helldivers.backslot;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerBackSlotProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerBackSlot> PLAYER_BACK_SLOT = CapabilityManager.get(new CapabilityToken<PlayerBackSlot>() {});

    private PlayerBackSlot backSlot = null;
    private final LazyOptional<PlayerBackSlot> lazyOptional = LazyOptional.of(this::createPlayerBackSlot);

    private PlayerBackSlot createPlayerBackSlot() {
        if (this.backSlot == null) {
            this.backSlot = new PlayerBackSlot();
        }
        return this.backSlot;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_BACK_SLOT) {
            return lazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerBackSlot().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerBackSlot().loadNBTData(nbt);
    }
}
