package net.team.helldivers.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class ShrapnelParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    private float rotationX, rotationY, rotationZ;
    private final float rotationXMod, rotationYMod, rotationZMod;
    private final float groundOffset = 0.01f;

    public ShrapnelParticle(ClientLevel level, double x, double y, double z,
                            double xSpeed, double ySpeed, double zSpeed,
                            SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = spriteSet;
//        this.setParticleSpeed(
//                Mth.nextDouble(level.random, -1, 1),
//                Mth.nextDouble(level.random, -1, 1),
//                Mth.nextDouble(level.random, -1, 1));

        this.quadSize = ((float) xSpeed);
        this.lifetime = level.random.nextInt(80) + 40;
        this.setSpriteFromAge(spriteSet);

        this.alpha = 1.0f;
        this.gravity = 1.0f;
//        this.friction = 1.0f;

        this.xd = Mth.nextDouble(level.random, -1, 1);
        this.yd = Mth.nextDouble(level.random, -1, 1);
        this.zd = Mth.nextDouble(level.random, -1, 1);

        this.rotationX = level.random.nextFloat() * 360f;
        this.rotationY = level.random.nextFloat() * 360f;
        this.rotationZ = level.random.nextFloat() * 360f;
        this.rotationXMod = level.random.nextFloat();
        this.rotationYMod = level.random.nextFloat();
        this.rotationZMod = level.random.nextFloat();
    }

//    @Override
//    public void tick() {
//        super.tick();
//
//        // Custom behavior on ground
//        if (this.onGround) {
//            this.yd = 0;
//            this.xd *= 0.7;
//            this.zd *= 0.7;
//        } else {
//            // Apply rotation while airborne
//            this.rotationX += this.rotationXMod;
//            this.rotationY += this.rotationYMod;
//            this.rotationZ += this.rotationZMod;
//        }
//
//        this.setSpriteFromAge(this.sprites);
//    }

//    @Override
//    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
//        super.render(buffer, camera, partialTicks);
//        Vec3 camPos = camera.getPosition();
//        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
//        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y());
//        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());
//
//        float scale = this.getQuadSize(partialTicks);
//
//        Vector3f[] corners = {
//                new Vector3f(-1, -1, 0), new Vector3f(-1, 1, 0),
//                new Vector3f(1, 1, 0),  new Vector3f(1, -1, 0)
//        };
//
//        Quaternionf rotation = this.onGround
//                ? eulerToQuaternion(90, 0, rotationZ)
//                : eulerToQuaternion(rotationX, rotationY, rotationZ);
//
//        for (Vector3f corner : corners) {
//            corner.rotate(rotation);
//            corner.mul(scale);
//            if (this.onGround) corner.add(x, y + groundOffset, z);
//            else corner.add(x, y, z);
//        }
//
//        float u0 = this.getU0();
//        float u1 = this.getU1();
//        float v0 = this.getV0();
//        float v1 = this.getV1();
//        int light = this.getLightColor(partialTicks);
//
//        buffer.vertex(corners[0].x(), corners[0].y(), corners[0].z()).uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light);
//        buffer.vertex(corners[1].x(), corners[1].y(), corners[1].z()).uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light);
//        buffer.vertex(corners[2].x(), corners[2].y(), corners[2].z()).uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light);
//        buffer.vertex(corners[3].x(), corners[3].y(), corners[3].z()).uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light);
//    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    private static Quaternionf eulerToQuaternion(float x, float y, float z) {
        x *= ((float) Math.PI / 180F);
        y *= ((float) Math.PI / 180F);
        z *= ((float) Math.PI / 180F);

        float sinX = Mth.sin(0.5F * x);
        float cosX = Mth.cos(0.5F * x);
        float sinY = Mth.sin(0.5F * y);
        float cosY = Mth.cos(0.5F * y);
        float sinZ = Mth.sin(0.5F * z);
        float cosZ = Mth.cos(0.5F * z);

        float qx = sinX * cosY * cosZ + cosX * sinY * sinZ;
        float qy = cosX * sinY * cosZ - sinX * cosY * sinZ;
        float qz = sinX * sinY * cosZ + cosX * cosY * sinZ;
        float qw = cosX * cosY * cosZ - sinX * sinY * sinZ;

        return new Quaternionf(qx, qy, qz, qw);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new ShrapnelParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}