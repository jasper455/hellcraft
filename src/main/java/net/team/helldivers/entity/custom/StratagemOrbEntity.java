package net.team.helldivers.entity.custom;

import it.unimi.dsi.fastutil.floats.FloatLists;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.common.Mod;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.helper.OrbitalBarrage;
import net.team.helldivers.network.CSmallExplosionParticlesPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SExplosionPacket;
import net.team.helldivers.sound.ModSounds;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.team.helldivers.worldgen.dimension.ModDimensions;

import java.util.List;

public class StratagemOrbEntity extends AbstractArrow {
    private static final EntityDataAccessor<String> STRATAGEM_TYPE =
            SynchedEntityData.defineId(StratagemOrbEntity.class, EntityDataSerializers.STRING);
    private Direction ownerDirection;
    private float rotation;
    public Vec3 groundedOffset;
    private int groundedTicks = 0;
    public boolean hasBeenGrounded;
    private LivingEntity strongest = null;

    public StratagemOrbEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StratagemOrbEntity(LivingEntity shooter, Level level, String stratagemType, Direction ownerDirection) {
        super(ModEntities.STRATAGEM_ORB.get(), shooter, level);
        this.ownerDirection = ownerDirection;
        setStratagemType(stratagemType);
    }

    public float getRenderingRotation() {
            rotation += 0.5f;
            if (rotation >= 360) {
                rotation = 0;
            }
        return rotation;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide && result.getEntity() instanceof LivingEntity && !getStratagemType().equals("Orbital Railcannon Strike")) {
            this.setDeltaMovement(Vec3.ZERO);
        }
    }


    public boolean isGrounded() {
        return inGround;
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

        if (getStratagemType() == null) this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            this.discard();
        }

        if (this.isGrounded() || hasBeenGrounded) {
            hasBeenGrounded = true;
            groundedTicks++;
        }

        if (this.isGrounded() && groundedTicks == 1 && !this.level().isClientSide()) {
            this.playSound(ModSounds.STRATAGEM_ORB_LAND.get(), 3f, 1.0f);
        }

        // OTHER

        // Hellbomb Entity stuff
        if (getStratagemType().equals("Hellbomb") && groundedTicks == 120 && !this.level().isClientSide) {
            HellbombHellpodEntity hellpod = new HellbombHellpodEntity(this.level());
            hellpod.setPos(this.getX(), 200, this.getZ());

            // Get the owner (player)
            if (this.getOwner() instanceof Player player) {
                // Set the entity's rotation to face the player
                double deltaX = player.getX() - this.getBlockX();
                double deltaZ = player.getZ() - this.getBlockZ();
                float yRot = (float) (Math.atan2(deltaZ, deltaX) * (180.0D / Math.PI)) - 90.0F;
                hellpod.setYRot(yRot);
            }

            this.level().addFreshEntity(hellpod);
        }
        if (getStratagemType().equals("Hellbomb") && groundedTicks == 180) {
            this.discard();
            groundedTicks = 0;
        }

        // SUPPORT

        // Resupply Entity Stuff
        if (getStratagemType().equals("Resupply") && !this.level().isClientSide) {
            if (groundedTicks == 300) {
                SupportHellpodEntity supportHellpodEntity = new SupportHellpodEntity(this.level(), getStratagemType());
                supportHellpodEntity.setPos(this.getX(), 200, this.getZ());
                this.level().addFreshEntity(supportHellpodEntity);

                // Get the owner (player)
                if (this.getOwner() instanceof Player player) {
                    // Set the entity's rotation to face the player
                    double deltaX = player.getX() - this.getBlockX();
                    double deltaZ = player.getZ() - this.getBlockZ();
                    float yRot = (float) (Math.atan2(deltaZ, deltaX) * (180.0D / Math.PI)) - 90.0F;
                    supportHellpodEntity.setYRot(yRot);
                }
            }
        }
        if (getStratagemType().equals("Resupply") && groundedTicks > 320) {
            this.discard();
            groundedTicks = 0;
        }

        // Expendable Anti-Tank Entity Stuff
        if (getStratagemType().equals("Expendable Anti-Tank") && !this.level().isClientSide) {
            if (groundedTicks == 100) {
                SupportHellpodEntity supportHellpodEntity = new SupportHellpodEntity(this.level(), getStratagemType());
                supportHellpodEntity.setPos(this.getX(), 200, this.getZ());
                this.level().addFreshEntity(supportHellpodEntity);

                // Get the owner (player)
                if (this.getOwner() instanceof Player player) {
                    // Set the entity's rotation to face the player
                    double deltaX = player.getX() - this.getBlockX();
                    double deltaZ = player.getZ() - this.getBlockZ();
                    float yRot = (float) (Math.atan2(deltaZ, deltaX) * (180.0D / Math.PI)) - 90.0F;
                    supportHellpodEntity.setYRot(yRot);
                }
            }
        }
        if (getStratagemType().equals("Expendable Anti-Tank") && groundedTicks > 140) {
            this.discard();
            groundedTicks = 0;
        }

        // Stalwart Entity Stuff
        if (getStratagemType().equals("Stalwart") && !this.level().isClientSide) {
            if (groundedTicks == 100) {
                SupportHellpodEntity supportHellpodEntity = new SupportHellpodEntity(this.level(), getStratagemType());
                supportHellpodEntity.setPos(this.getX(), 200, this.getZ());
                this.level().addFreshEntity(supportHellpodEntity);

                // Get the owner (player)
                if (this.getOwner() instanceof Player player) {
                    // Set the entity's rotation to face the player
                    double deltaX = player.getX() - this.getBlockX();
                    double deltaZ = player.getZ() - this.getBlockZ();
                    float yRot = (float) (Math.atan2(deltaZ, deltaX) * (180.0D / Math.PI)) - 90.0F;
                    supportHellpodEntity.setYRot(yRot);
                }

            }
        }
        if (getStratagemType().equals("Stalwart") && groundedTicks > 140) {
            this.discard();
            groundedTicks = 0;
        }

        // Amr Entity Stuff
        if (getStratagemType().equals("Anti-Materiel Rifle") && !this.level().isClientSide) {
            if (groundedTicks == 100) {
                SupportHellpodEntity supportHellpodEntity = new SupportHellpodEntity(this.level(), getStratagemType());
                supportHellpodEntity.setPos(this.getX(), 200, this.getZ());
                this.level().addFreshEntity(supportHellpodEntity);

                // Get the owner (player)
                if (this.getOwner() instanceof Player player) {
                    // Set the entity's rotation to face the player
                    double deltaX = player.getX() - this.getBlockX();
                    double deltaZ = player.getZ() - this.getBlockZ();
                    float yRot = (float) (Math.atan2(deltaZ, deltaX) * (180.0D / Math.PI)) - 90.0F;
                    supportHellpodEntity.setYRot(yRot);
                }

            }
        }
        if (getStratagemType().equals("Anti-Materiel Rifle") && groundedTicks > 140) {
            this.discard();
            groundedTicks = 0;
        }

        // Portable Hellbomb Entity Stuff
        if (getStratagemType().equals("Portable Hellbomb") && !this.level().isClientSide) {
            if (groundedTicks == 100) {
                SupportHellpodEntity supportHellpodEntity = new SupportHellpodEntity(this.level(), getStratagemType());
                supportHellpodEntity.setPos(this.getX(), 200, this.getZ());
                this.level().addFreshEntity(supportHellpodEntity);

                // Get the owner (player)
                if (this.getOwner() instanceof Player player) {
                    // Set the entity's rotation to face the player
                    double deltaX = player.getX() - this.getBlockX();
                    double deltaZ = player.getZ() - this.getBlockZ();
                    float yRot = (float) (Math.atan2(deltaZ, deltaX) * (180.0D / Math.PI)) - 90.0F;
                    supportHellpodEntity.setYRot(yRot);
                }

            }
        }
        if (getStratagemType().equals("Portable Hellbomb") && groundedTicks > 180) {
            this.discard();
            groundedTicks = 0;
        }

        // Jump Pack Entity Stuff
        if (getStratagemType().equals("Jump Pack") && !this.level().isClientSide) {
            if (groundedTicks == 100) {
                SupportHellpodEntity supportHellpodEntity = new SupportHellpodEntity(this.level(), getStratagemType());
                supportHellpodEntity.setPos(this.getX(), 200, this.getZ());
                this.level().addFreshEntity(supportHellpodEntity);

                // Get the owner (player)
                if (this.getOwner() instanceof Player player) {
                    // Set the entity's rotation to face the player
                    double deltaX = player.getX() - this.getBlockX();
                    double deltaZ = player.getZ() - this.getBlockZ();
                    float yRot = (float) (Math.atan2(deltaZ, deltaX) * (180.0D / Math.PI)) - 90.0F;
                    supportHellpodEntity.setYRot(yRot);
                }

            }
        }
        if (getStratagemType().equals("Jump Pack") && groundedTicks > 180) {
            this.discard();
            groundedTicks = 0;
        }

        // ORBITAL

        // Orbital Precision Strike Entity stuff
        if (getStratagemType().equals("Orbital Precision Strike") && groundedTicks == 60 && !this.level().isClientSide) {
            if (random.nextBoolean() && random.nextBoolean()) {
                this.playSound(ModSounds.FIRE_ORBITAL_STRIKE.get(), 10000000.0f, 1.0f);
            } else if (random.nextBoolean() && !random.nextBoolean()) {
                this.playSound(ModSounds.ORBITAL_STRIKE_INCOMING.get(), 10000000.0f, 1.0f);
            }
        }
        if (getStratagemType().equals("Orbital Precision Strike") && groundedTicks == 90 && !this.level().isClientSide()) {
            float randomPosX = (Mth.randomBetween(this.level().getRandom(), -5.0f, 5.0f));
            float randomPosY = (Mth.randomBetween(this.level().getRandom(), -5.0f, 5.0f));

            MissileProjectileEntity explosive = new MissileProjectileEntity(((LivingEntity) this.getOwner()), this.level(), 17, false);
            explosive.setPos(this.getX() + randomPosX, 200 + randomPosY, this.getZ());
            explosive.setDeltaMovement(0f, 0f, 0f);
            this.level().addFreshEntity(explosive);
        }
        if (getStratagemType().equals("Orbital Precision Strike") && groundedTicks == 100) {
            this.discard();
            groundedTicks = 0;
        }

        if (getStratagemType().equals("Orbital Railcannon Strike") && !this.level().isClientSide()) {

            List<LivingEntity> nearbyEntities = level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(20.0));
            double highestHealth = Double.MIN_VALUE;
            if (strongest == null) {
                for (LivingEntity nearbyEntity : nearbyEntities) {
                    if (nearbyEntity.getAttribute(Attributes.MAX_HEALTH) == null) continue;

                    double maxHealth = nearbyEntity.getAttributeValue(Attributes.MAX_HEALTH);
                    if (maxHealth > highestHealth) {
                        highestHealth = maxHealth;
                        strongest = nearbyEntity;
                    }
                }
            }
            if (strongest != null) {
                if (groundedTicks >= 60) {
                    strongest.hurt(damageSources().explosion(null), 550f);
                    this.playSound(ModSounds.EXPLOSION.get(), 10.0f, 1.0f);
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5, Level.ExplosionInteraction.NONE);
                    this.discard();
                    groundedTicks = 0;
                } else if (hasBeenGrounded) {
//                if (groundedTicks == 1) {
//                    this.setPos(this.getX(), this.getY() + 0.5D, this.getZ());
//                }
                    // Ungrounds the orb
                    this.inGround = false;
                    this.setNoPhysics(false); // ensures collisions work again
                    this.hasImpulse = true;   // tells MC to reprocess motion
                    this.setNoGravity(true);

                    Vec3 toTarget = strongest.position()
                            .add(0, strongest.getBbHeight(), 0)
                            .subtract(this.position())
                            .normalize()
                            .scale(0.8D);

                    Vec3 newMotion = this.getDeltaMovement().lerp(toTarget, 0.8);

                    this.setDeltaMovement(newMotion);
                }
            } else {
                if (groundedTicks >= 60) {
                    this.discard();
                    groundedTicks = 0;
                }
            }
        }

        // 120 Barrage Entity Stuff
        if (getStratagemType().equals("Orbital 120MM HE Barrage") && !this.level().isClientSide) {
            if (groundedTicks == 60) {
                if (random.nextBoolean() && random.nextBoolean()) {
                    this.playSound(ModSounds.ORBITAL_BARRAGE_CLEAR_THE_AREA.get(), 10000000.0f, 1.0f);
                } else if (random.nextBoolean() && !random.nextBoolean()) {
                    this.playSound(ModSounds.ORBITAL_BARRAGE_STAND_CLEAR.get(), 10000000.0f, 1.0f);
                }
            }
            if (groundedTicks > 75) {
                MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 25, 60,
                        groundedTicks,false, false));
            }
        }
        if (getStratagemType().equals("Orbital 120MM HE Barrage") && groundedTicks > 750) {
            this.discard();
            groundedTicks = 0;
        }

        // 380 Barrage Entity Stuff
        if (getStratagemType().equals("Orbital 380MM HE Barrage") && !this.level().isClientSide) {
            if (groundedTicks == 60) {
                if (random.nextBoolean() && random.nextBoolean()) {
                    this.playSound(ModSounds.ORBITAL_BARRAGE_CLEAR_THE_AREA.get(), 10000000.0f, 1.0f);
                } else if (random.nextBoolean() && !random.nextBoolean()) {
                    this.playSound(ModSounds.ORBITAL_BARRAGE_STAND_CLEAR.get(), 10000000.0f, 1.0f);
                }
            }
            if (groundedTicks > 75) {
                MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 50, 60,
                        groundedTicks,true, false));
            }
        }
        if (getStratagemType().equals("Orbital 380MM HE Barrage") && groundedTicks > 750) {
            this.discard();
            groundedTicks = 0;
        }

        // Napalm Barrage Entity Stuff
        if (getStratagemType().equals("Orbital Napalm Barrage") && !this.level().isClientSide) {
            if (groundedTicks == 60) {
                if (random.nextBoolean() && random.nextBoolean()) {
                    this.playSound(ModSounds.ORBITAL_BARRAGE_CLEAR_THE_AREA.get(), 10000000.0f, 1.0f);
                } else if (random.nextBoolean() && !random.nextBoolean()) {
                    this.playSound(ModSounds.ORBITAL_BARRAGE_STAND_CLEAR.get(), 10000000.0f, 1.0f);
                }
            }
            if (groundedTicks > 75) {
                MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 30, 60,
                        groundedTicks,true, true));
            }
        }
        if (getStratagemType().equals("Orbital Napalm Barrage") && groundedTicks > 750) {
            this.discard();
            groundedTicks = 0;
        }

        // Walking Barrage Entity Stuff
        if (getStratagemType().equals("Orbital Walking Barrage") && !this.level().isClientSide) {
            if (groundedTicks == 60) {
                if (random.nextBoolean() && random.nextBoolean()) {
                    this.playSound(ModSounds.ORBITAL_BARRAGE_CLEAR_THE_AREA.get(), 10000000.0f, 1.0f);
                } else if (random.nextBoolean() && !random.nextBoolean()) {
                    this.playSound(ModSounds.ORBITAL_BARRAGE_STAND_CLEAR.get(), 10000000.0f, 1.0f);
                }
            }
            if (groundedTicks > 75) {
                if (this.ownerDirection == Direction.NORTH) {
                    MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 15, 60,
                            groundedTicks,true, false, true, 0));
                }
                if (this.ownerDirection == Direction.SOUTH) {
                    MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 15, 60,
                            groundedTicks,true, false, true, 1));
                }
                if (this.ownerDirection == Direction.EAST) {
                    MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 15, 60,
                            groundedTicks,true, false, true, 2));
                }
                if (this.ownerDirection == Direction.WEST) {
                    MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 15, 60,
                            groundedTicks,true, false, true, 3));
                }
            }
        }
        if (getStratagemType().equals("Orbital Walking Barrage") && groundedTicks > 750) {
            this.discard();
            groundedTicks = 0;
        }

        // Orbital Laser Entity Stuff
        if (getStratagemType().equals("Orbital Laser") && !this.level().isClientSide) {
            if (groundedTicks == 60) {
                OrbitalLaserEntity laserEntity = new OrbitalLaserEntity(ModEntities.ORBITAL_LASER.get(), this.level());
                laserEntity.setOwner(this.getOwner());
                laserEntity.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(laserEntity);
            }
        }
        if (getStratagemType().equals("Orbital Laser") && groundedTicks > 60) {
            this.discard();
            groundedTicks = 0;
        }

        // EAGLE

        // 500Kg Bomb Entity Stuff
        if (getStratagemType().equals("Eagle 500KG Bomb") && !this.level().isClientSide) {
            if (groundedTicks == 40) {
                this.playSound(ModSounds.EAGLE_FLYBY.get(), 10.0f, 1.0f);
            }
            if (groundedTicks == 80) {
                EagleAirshipEntity eagleAirshipEntity = new EagleAirshipEntity(ModEntities.EAGLE_AIRSHIP.get(), this.level());
                eagleAirshipEntity.setStratagemType(getStratagemType());
                eagleAirshipEntity.setPos(this.getX(), this.getY() + 5, this.getZ());
                if (ownerDirection == Direction.NORTH || ownerDirection == Direction.SOUTH) {
                    eagleAirshipEntity.setYHeadRot(90);
                }
                this.level().addFreshEntity(eagleAirshipEntity);
            }
        }
        if (getStratagemType().equals("Eagle 500KG Bomb") && groundedTicks > 90) {
            this.discard();
            groundedTicks = 0;
        }

        // Cluster Bomb Entity Stuff
        if (getStratagemType().equals("Eagle Cluster Bomb") && !this.level().isClientSide) {
            if (groundedTicks == 25) {
                this.playSound(ModSounds.EAGLE_FLYBY.get(), 10.0f, 1.0f);
            }
            if (groundedTicks == 65) {
                EagleAirshipEntity eagleAirshipEntity = new EagleAirshipEntity(ModEntities.EAGLE_AIRSHIP.get(), this.level());
                eagleAirshipEntity.setStratagemType(getStratagemType());
                eagleAirshipEntity.setPos(this.getX(), this.getY() + 5, this.getZ());
                if (ownerDirection == Direction.NORTH || ownerDirection == Direction.SOUTH) {
                    eagleAirshipEntity.setYHeadRot(90);
                }
                this.level().addFreshEntity(eagleAirshipEntity);
            }
            if (groundedTicks == 82) {
                for (int i = 0; i < 4; i++) {
                    spawnClusterBomb();
                }
            }
            if (groundedTicks == 87) {
                for (int i = 0; i < 4; i++) {
                    spawnClusterBomb();
                }
            }
            if (groundedTicks == 92) {
                for (int i = 0; i < 4; i++) {
                    spawnClusterBomb();
                }
            }
            if (groundedTicks == 97) {
                for (int i = 0; i < 4; i++) {
                    spawnClusterBomb();
                }
            }
            if (groundedTicks == 102) {
                for (int i = 0; i < 4; i++) {
                    spawnClusterBomb();
                }
            }
        }
        if (getStratagemType().equals("Eagle Cluster Bomb") && groundedTicks > 102) {
            this.discard();
            groundedTicks = 0;
        }

        // Eagle Airstrike Entity Stuff

        if (getStratagemType().equals("Eagle Airstrike") && !this.level().isClientSide) {
            if (groundedTicks == 40) {
                this.playSound(ModSounds.EAGLE_FLYBY.get(), 10.0f, 1.0f);
            }
            if (groundedTicks == 80) {
                EagleAirshipEntity eagleAirshipEntity = new EagleAirshipEntity(ModEntities.EAGLE_AIRSHIP.get(), this.level());
                eagleAirshipEntity.setStratagemType(getStratagemType());
                eagleAirshipEntity.setPos(this.getX(), this.getY() + 5, this.getZ());
                if (ownerDirection == Direction.NORTH || ownerDirection == Direction.SOUTH) {
                    eagleAirshipEntity.setYHeadRot(90);
                }
                this.level().addFreshEntity(eagleAirshipEntity);
            }
        }
        if (getStratagemType().equals("Eagle Airstrike") && groundedTicks > 90) {
            this.discard();
            groundedTicks = 0;
        }

        // Napalm Airstrike Entity Stuff
        if (getStratagemType().equals("Eagle Napalm Airstrike") && !this.level().isClientSide) {
            if (groundedTicks == 40) {
                this.playSound(ModSounds.EAGLE_FLYBY.get(), 10.0f, 1.0f);
            }
            if (groundedTicks == 80) {
                EagleAirshipEntity eagleAirshipEntity = new EagleAirshipEntity(ModEntities.EAGLE_AIRSHIP.get(), this.level());
                eagleAirshipEntity.setStratagemType(getStratagemType());
                eagleAirshipEntity.setPos(this.getX(), this.getY() + 5, this.getZ());
                if (ownerDirection == Direction.NORTH || ownerDirection == Direction.SOUTH) {
                    eagleAirshipEntity.setYHeadRot(90);
                }
                this.level().addFreshEntity(eagleAirshipEntity);
            }
        }
        if (getStratagemType().equals("Eagle Napalm Airstrike") && groundedTicks > 90) {
            this.discard();
            groundedTicks = 0;
        }
    }

    private void spawnClusterBomb() {
        Player player = ((Player) this.getOwner());
        float randomPosX = (Mth.randomBetween(player.level().getRandom(), 17 * -1, 17));
        float randomPosZ = (Mth.randomBetween(player.level().getRandom(), 17 * -1, 17));

        ClusterBombProjectileEntity explosive = new ClusterBombProjectileEntity(player, player.level(), 3);
        explosive.setPos(this.getX() + randomPosX, this.getY() + 44, this.getZ() - randomPosZ);
        player.level().addFreshEntity(explosive);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.AIR);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.STRATAGEM_ORB_LAND.get();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STRATAGEM_TYPE, "");
    }

    public String getStratagemType() {
        return this.entityData.get(STRATAGEM_TYPE);
    }

    public void setStratagemType(String type) {
        this.entityData.set(STRATAGEM_TYPE, type);
    }

}