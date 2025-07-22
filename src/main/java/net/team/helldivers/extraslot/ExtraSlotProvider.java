package net.team.helldivers.extraslot;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExtraSlotProvider implements ICapabilityProvider {
    private final ExtraSlotHandler handler = new ExtraSlotHandler();
    private final LazyOptional<ExtraSlotHandler> optional = LazyOptional.of(() -> handler);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }
}