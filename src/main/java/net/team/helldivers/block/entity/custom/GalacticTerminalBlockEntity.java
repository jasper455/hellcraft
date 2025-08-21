package net.team.helldivers.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.team.helldivers.screen.custom.GalaxyMapMenu;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GalacticTerminalBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider, Container {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final String EXTRACTION_INVENTORY_KEY = "ExtractionInventory";
    private static final int INVENTORY_SIZE = 4;

    public GalacticTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GALACTIC_TERMINAL.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return null;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {}

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {}

    @Override
    public Component getDisplayName() {
        return Component.literal("Galaxy Map");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GalaxyMapMenu(pContainerId, pPlayerInventory, this);
    }

    public Container getPlayerInventory(Player player) {
        CompoundTag persistentData = getPersistentData(player);
        SimpleContainer container = new SimpleContainer(INVENTORY_SIZE);

        if (persistentData.contains("Items")) {
            ListTag items = persistentData.getList("Items", 10);
            for (int i = 0; i < items.size(); i++) {
                CompoundTag itemTag = items.getCompound(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < container.getContainerSize()) {
                    container.setItem(slot, ItemStack.of(itemTag));
                }
            }
        }

        return container;
    }

    public void savePlayerInventory(Player player, Container container) {
        CompoundTag persistentData = getPersistentData(player);
        ListTag items = new ListTag();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(itemTag);
                items.add(itemTag);
            }
        }

        persistentData.put("Items", items);
        setPersistentData(player, persistentData);
    }

    private CompoundTag getPersistentData(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag forgeData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);

        if (!forgeData.contains(EXTRACTION_INVENTORY_KEY)) {
            forgeData.put(EXTRACTION_INVENTORY_KEY, new CompoundTag());
        }

        return forgeData.getCompound(EXTRACTION_INVENTORY_KEY);
    }

    private void setPersistentData(Player player, CompoundTag data) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag forgeData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);

        forgeData.put(EXTRACTION_INVENTORY_KEY, data);
        persistentData.put(Player.PERSISTED_NBT_TAG, forgeData);
    }
}

