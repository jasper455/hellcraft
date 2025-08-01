package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.block.custom.BotContactMineBlock;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.util.Headshots.HeadHitbox;
import net.team.helldivers.util.Headshots.HeadHitboxRegistry;

import java.util.Map;

import com.ibm.icu.text.MessagePattern.Part;
import net.team.helldivers.worldgen.dimension.ModDimensions;

public class BulletProjectileEntity extends AbstractArrow {
    public Vec2 groundedOffset;
    private Vec3 previousPos;
    private int lifetime = 0;
    private boolean isShotgun;
    private boolean isAmr;
    private int maxLife = 40;
    private int maxShotgunLife = 10;

    public BulletProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BulletProjectileEntity(LivingEntity shooter, Level level, boolean isShotgun, boolean isAmr) {
        super(ModEntities.BULLET.get(), shooter, level);
        this.previousPos = new Vec3(this.getX(), this.getY(), this.getZ());
        this.isShotgun = isShotgun;
        this.isAmr = isAmr;
    }


    @Override
    protected ItemStack getPickupItem() {
        return Items.AIR.getDefaultInstance();
    }

    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if(checkHeadShot(entity, result.getLocation())){
            entity.hurt(this.damageSources().arrow(this, this.getOwner()), 4);
            System.out.println("HEADSHOT");
        }
        else{
            entity.hurt(this.damageSources().arrow(this, this.getOwner()), 3);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        BlockState block = Minecraft.getInstance().level.getBlockState(pos);
        if (block.is(BlockTags.IMPERMEABLE) || block.getBlock() instanceof IronBarsBlock) {
            this.level().destroyBlock(pos, false);
        }
        if (block.getBlock() instanceof BotContactMineBlock) {
            this.level().setBlockAndUpdate(result.getBlockPos(), Blocks.AIR.defaultBlockState());
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(result.getBlockPos()).inflate(3.0)).forEach(entity -> {
                entity.hurt(this.level().damageSources().explosion(null), 12.5F);
            });
        }
        if(this.level().isClientSide){
            //add particles on hit
            BlockParticleOption blockParticle = new BlockParticleOption(ParticleTypes.BLOCK, block);
            for(int i=0;i<20;i++){
                double rand1 = Math.random()*plusmin();
                double rand2 = Math.random()*plusmin();          
                double rand3 = Math.random()*plusmin();            
                this.level().addParticle(blockParticle,this.getX(), this.getY(), this.getZ(),rand1, rand2, rand3);
            }
        }
        //yes this is odd. yes I spent hours trying to figure out why this wasnt working. yes, giving an extra few ticks of life allows particles to spawn. Idk
        if(this.isShotgun){
            this.lifetime = maxShotgunLife-3;
        }
        else{
            this.lifetime = maxLife-3;
        }
    }

    @Override
    public void tick() {
        this.previousPos = new Vec3(this.getX(), this.getY(), this.getZ());

        if (this.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            this.discard();
        }

        super.tick();
        lifetime++;
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(this.isAmr ? 10 : 5));
        if (this.level().isClientSide) {
            Entity owner = this.getOwner();
            if (owner instanceof Player player) {
                // Create particles along the path
                Vec3 current = new Vec3(this.getX(), this.getY(), this.getZ());
                Vec3 direction = current.subtract(previousPos);
                int particleCount = 5; // Adjust based on speed

                for (int i = 0; i < particleCount; i++) {
                    double factor = i / (double) particleCount;
                    Vec3 pos = previousPos.add(direction.scale(factor));

                    DustParticleOptions dustParticle = new DustParticleOptions(
                            Vec3.fromRGB24(0x000000).toVector3f(), 0.5F);

                    this.level().addParticle(dustParticle,
                            pos.x, pos.y, pos.z,
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
        if (lifetime >= maxLife) {
            this.discard();
        } else if (this.isShotgun && lifetime >= maxShotgunLife) {
            this.discard();
        }
    }

    @Override
        protected SoundEvent getDefaultHitGroundSoundEvent() {
            return SoundEvents.BONE_BLOCK_HIT;
    }
        
    private static int plusmin(){
    if(Math.random()>0.5){
            return -1;
        }
        else{
            return 1;
        }
    }
    //TODO: fix collision check for head hitboxes
    //TODO: add rotation for head hitboxes based on entity look dir
    //TODO: add the rest of the entities to the HeadLocations json
    private static boolean checkHeadShot(Entity entity, Vec3 pos) {
        Map<String, HeadHitbox> hitboxes = HeadHitboxRegistry.getAll();
        if (entity instanceof EnderDragonPart part) {
            EnderDragon dragon = part.getParent();
            System.out.println("dragon");
            if(dragon.head.getBoundingBox().contains(pos)) return true;
        }
        else if (hitboxes != null) {
            ResourceLocation id = EntityType.getKey(entity.getType());
            HeadHitbox headHitbox = hitboxes.get(id.toString());
            if (headHitbox != null) {
                AABB box = headHitbox.getBox(entity.getBoundingBox());
                System.out.println("Checking box: " + box);
                if(box.contains(pos)) return true;
                return false;
            }
            System.out.println("entity not found");
        }
        return false;
    }
}