package net.infinite1274.helldivers.entity.custom;

import net.infinite1274.helldivers.entity.ModEntities;
import net.infinite1274.helldivers.item.ModItems;
import net.infinite1274.helldivers.network.PacketHandler;
import net.infinite1274.helldivers.network.SSphereExplosionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class StratagemOrbEntity extends AbstractArrow {
    private float rotation;
    public Vec3 groundedOffset;
    private int groundedTicks = 0;
    public String stratagemType = "";


    public StratagemOrbEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StratagemOrbEntity(LivingEntity shooter, Level level) {
        super(ModEntities.STRATAGEM_ORB.get(), shooter, level);
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 4);

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if(pResult.getDirection() == Direction.SOUTH) {
            groundedOffset = new Vec3(180f, 180f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.NORTH) {
            groundedOffset = new Vec3(180f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.EAST) {
            groundedOffset = new Vec3(180f, -90f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.WEST) {
            groundedOffset = new Vec3(180f, 90f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }

        if(pResult.getDirection() == Direction.DOWN) {
            groundedOffset = new Vec3(0f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.UP) {
            groundedOffset = new Vec3(270f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }

        if (stratagemType == null) this.discard();

//        if ("Hellbomb".equals(stratagemType)) {
//            BlockPos pos = new BlockPos(this.getOnPos().getX(), this.getOnPos().getY() + 300, this.getOnPos().getZ());
//            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level(), pos, ModBlocks.HELLBOMB.get().defaultBlockState());
//            level().addFreshEntity(fallingBlockEntity);
//        }

        if ("Orbital Precision Strike".equals(stratagemType)) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Sending in an Eagle!"));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isGrounded()) {
            groundedTicks++;
        }

        if ("Orbital Precision Strike".equals(stratagemType) && groundedTicks == 147) {
            BlockPos pos = new BlockPos(this.getOnPos().getX(), this.getOnPos().getY() + 300, this.getOnPos().getZ());
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level(), pos, Blocks.BLACK_CONCRETE.defaultBlockState());
            fallingBlockEntity.setDeltaMovement(0, -8, 0);
            level().addFreshEntity(fallingBlockEntity);
        }

        if ("Orbital Precision Strike".equals(stratagemType) && groundedTicks >= 200) {
            PacketHandler.sendToServer(new SSphereExplosionPacket(this.getOnPos(), 10));
            this.discard();
        }

    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.STRATAGEM_ORB.get(), 1);
    }

}
