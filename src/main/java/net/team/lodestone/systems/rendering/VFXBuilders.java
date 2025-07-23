//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.team.lodestone.systems.rendering;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class VFXBuilders {

    public static class WorldVFXBuilder {
        protected float r = 1.0F;
        protected float g = 1.0F;
        protected float b = 1.0F;
        protected float a = 1.0F;
        protected int light = 15728880;
        protected float u0 = 0.0F;
        protected float v0 = 0.0F;
        protected float u1 = 1.0F;
        protected float v1 = 1.0F;
        protected MultiBufferSource bufferSource;
        protected RenderType renderType;
        protected VertexFormat format;
        protected WorldVertexConsumerActor supplier;
        protected VertexConsumer vertexConsumer;
        protected HashMap<Object, Consumer<WorldVFXBuilder>> modularActors;
        protected int modularActorAddIndex;
        protected int modularActorGetIndex;
        public static final HashMap<VertexFormatElement, WorldVertexConsumerActor> CONSUMER_INFO_MAP = new HashMap();

        public static WorldVFXBuilder createWorld() {
            return new WorldVFXBuilder();
        }

        public WorldVFXBuilder replaceBufferSource(MultiBufferSource bufferSource) {
            this.bufferSource = bufferSource;
            return this;
        }

        public WorldVFXBuilder setRenderType(RenderType renderType) {
            return this.setRenderTypeRaw(renderType).setFormat(renderType.format()).setVertexConsumer(this.bufferSource.getBuffer(renderType));
        }

        public WorldVFXBuilder setRenderTypeRaw(RenderType renderType) {
            this.renderType = renderType;
            return this;
        }

        public WorldVFXBuilder setFormat(VertexFormat format) {
            ImmutableList<VertexFormatElement> elements = format.getElements();
            return this.setFormatRaw(format).setVertexSupplier((consumer, last, builder, x, y, z, u, v) -> {
                UnmodifiableIterator var10 = elements.iterator();

                while(var10.hasNext()) {
                    VertexFormatElement element = (VertexFormatElement)var10.next();
                    ((WorldVertexConsumerActor)CONSUMER_INFO_MAP.get(element)).placeVertex(consumer, last, this, x, y, z, u, v);
                }

                consumer.endVertex();
            });
        }

        public WorldVFXBuilder setFormatRaw(VertexFormat format) {
            this.format = format;
            return this;
        }

        public WorldVFXBuilder setVertexSupplier(WorldVertexConsumerActor supplier) {
            this.supplier = supplier;
            return this;
        }

        public WorldVFXBuilder setVertexConsumer(VertexConsumer vertexConsumer) {
            this.vertexConsumer = vertexConsumer;
            return this;
        }

        public VertexConsumer getVertexConsumer() {
            if (this.vertexConsumer == null) {
                this.setVertexConsumer(this.bufferSource.getBuffer(this.renderType));
            }

            return this.vertexConsumer;
        }

        public WorldVFXBuilder addModularActor(Consumer<WorldVFXBuilder> actor) {
            return this.addModularActor(this.modularActorAddIndex, actor);
        }

        public WorldVFXBuilder addModularActor(Object key, Consumer<WorldVFXBuilder> actor) {
            if (this.modularActors == null) {
                this.modularActors = new HashMap();
            }

            this.modularActors.put(key, actor);
            return this;
        }

        public Optional<HashMap<Object, Consumer<WorldVFXBuilder>>> getModularActors() {
            return Optional.ofNullable(this.modularActors);
        }

        public Optional<Consumer<WorldVFXBuilder>> getNextModularActor() {
            return Optional.ofNullable(this.modularActors).map((m) -> {
                return (Consumer)m.get(this.modularActorGetIndex++);
            });
        }

        public MultiBufferSource getBufferSource() {
            return this.bufferSource;
        }

        public RenderType getRenderType() {
            return this.renderType;
        }

        public VertexFormat getFormat() {
            return this.format;
        }

        public WorldVertexConsumerActor getSupplier() {
            return this.supplier;
        }

        public WorldVFXBuilder setColor(Color color) {
            return this.setColor((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue());
        }

        public WorldVFXBuilder setColor(Color color, float a) {
            return this.setColor(color).setAlpha(a);
        }

        public WorldVFXBuilder setColor(float r, float g, float b, float a) {
            return this.setColor(r, g, b).setAlpha(a);
        }

        public WorldVFXBuilder setColor(float r, float g, float b) {
            this.r = r / 255.0F;
            this.g = g / 255.0F;
            this.b = b / 255.0F;
            return this;
        }

        public WorldVFXBuilder setColorRaw(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }

        public WorldVFXBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public WorldVFXBuilder setLight(int light) {
            this.light = light;
            return this;
        }

        public WorldVFXBuilder setUV(float u0, float v0, float u1, float v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }

        public WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width) {
            Minecraft minecraft = Minecraft.getInstance();
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            return this.renderBeam(last, start, end, width, cameraPosition);
        }

        public WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Consumer<WorldVFXBuilder> consumer) {
            Minecraft minecraft = Minecraft.getInstance();
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            return this.renderBeam(last, start, end, width, cameraPosition, consumer);
        }

        public WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition) {
            return this.renderBeam(last, start, end, width, cameraPosition, (builder) -> {
            });
        }

        public WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition, Consumer<WorldVFXBuilder> consumer) {
            Vec3 delta = end.subtract(start);
            Vec3 normal = start.subtract(cameraPosition).cross(delta).normalize().multiply((double)(width / 2.0F), (double)(width / 2.0F), (double)(width / 2.0F));
            Vec3[] positions = new Vec3[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float)positions[0].x, (float)positions[0].y, (float)positions[0].z, this.u0, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float)positions[1].x, (float)positions[1].y, (float)positions[1].z, this.u1, this.v1);
            consumer.accept(this);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float)positions[2].x, (float)positions[2].y, (float)positions[2].z, this.u1, this.v0);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float)positions[3].x, (float)positions[3].y, (float)positions[3].z, this.u0, this.v0);
            return this;
        }

//        public WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, float width) {
//            return this.renderTrail(stack, trailSegments, (f) -> {
//                return width;
//            }, (f) -> {
//            });
//        }
//
//        public WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc) {
//            return this.renderTrail(stack, trailSegments, widthFunc, (f) -> {
//            });
//        }
//
//        public WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
//            return this.renderTrail(stack.last().pose(), trailSegments, widthFunc, vfxOperator);
//        }
//
//        public WorldVFXBuilder renderTrail(Matrix4f pose, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
//            if (trailSegments.size() < 2) {
//                return this;
//            } else {
//                List<Vector4f> positions = trailSegments.stream().map(TrailPoint::getMatrixPosition).peek((p) -> {
//                    p.mul(pose);
//                }).toList();
//                int count = trailSegments.size() - 1;
//                float increment = 1.0F / (float)count;
//                TrailRenderPoint[] points = new TrailRenderPoint[trailSegments.size()];
//
//                for(int i = 1; i < count; ++i) {
//                    float width = (Float)widthFunc.apply(increment * (float)i);
//                    Vector4f previous = (Vector4f)positions.get(i - 1);
//                    Vector4f current = (Vector4f)positions.get(i);
//                    Vector4f next = (Vector4f)positions.get(i + 1);
//                    points[i] = new TrailRenderPoint(current, RenderHelper.perpendicularTrailPoints(previous, next, width));
//                }
//
//                points[0] = new TrailRenderPoint((Vector4f)positions.get(0), RenderHelper.perpendicularTrailPoints((Vector4f)positions.get(0), (Vector4f)positions.get(1), (Float)widthFunc.apply(0.0F)));
//                points[count] = new TrailRenderPoint((Vector4f)positions.get(count), RenderHelper.perpendicularTrailPoints((Vector4f)positions.get(count - 1), (Vector4f)positions.get(count), (Float)widthFunc.apply(1.0F)));
//                return this.renderPoints(points, this.u0, this.v0, this.u1, this.v1, vfxOperator);
//            }
//        }
//
//        public WorldVFXBuilder renderPoints(TrailRenderPoint[] points, float u0, float v0, float u1, float v1, Consumer<Float> vfxOperator) {
//            int count = points.length - 1;
//            float increment = 1.0F / (float)count;
//            vfxOperator.accept(0.0F);
//            points[0].renderStart(this.getVertexConsumer(), this, u0, v0, u1, Mth.lerp(increment, v0, v1));
//
//            for(int i = 1; i < count; ++i) {
//                float current = Mth.lerp((float)i * increment, v0, v1);
//                vfxOperator.accept(current);
//                points[i].renderMid(this.getVertexConsumer(), this, u0, current, u1, current);
//            }
//
//            vfxOperator.accept(1.0F);
//            points[count].renderEnd(this.getVertexConsumer(), this, u0, Mth.lerp((float)count * increment, v0, v1), u1, v1);
//            return this;
//        }

        public WorldVFXBuilder renderQuad(PoseStack stack, float size) {
            return this.renderQuad(stack, size, size);
        }

        public WorldVFXBuilder renderQuad(PoseStack stack, float width, float height) {
            Vector3f[] positions = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F)};
            return this.renderQuad(stack, positions, width, height);
        }

        public WorldVFXBuilder renderQuad(PoseStack stack, Vector3f[] positions, float size) {
            return this.renderQuad(stack, positions, size, size);
        }

        public WorldVFXBuilder renderQuad(PoseStack stack, Vector3f[] positions, float width, float height) {
            Vector3f[] var5 = positions;
            int var6 = positions.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Vector3f position = var5[var7];
                position.mul(width, height, width);
            }

            return this.renderQuad(stack.last().pose(), positions);
        }

        public WorldVFXBuilder renderQuad(Matrix4f last, Vector3f[] positions) {
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[0].x(), positions[0].y(), positions[0].z(), this.u0, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[1].x(), positions[1].y(), positions[1].z(), this.u1, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[2].x(), positions[2].y(), positions[2].z(), this.u1, this.v0);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[3].x(), positions[3].y(), positions[3].z(), this.u0, this.v0);
            return this;
        }

        public WorldVFXBuilder renderSphere(PoseStack stack, float radius, int longs, int lats) {
            Matrix4f last = stack.last().pose();
            float startU = this.u0;
            float startV = this.v0;
            float endU = 6.2831855F * this.u1;
            float endV = 3.1415927F * this.v1;
            float stepU = (endU - startU) / (float)longs;
            float stepV = (endV - startV) / (float)lats;

            for(int i = 0; i < longs; ++i) {
                for(int j = 0; j < lats; ++j) {
                    float u = (float)i * stepU + startU;
                    float v = (float)j * stepV + startV;
                    float un = (float)(i + 1) * stepU + startU;
                    float vn = (float)(j + 1) * stepV + startV;
                    Vector3f p0 = parametricSphere(u, v, radius);
                    Vector3f p1 = parametricSphere(u, vn, radius);
                    Vector3f p2 = parametricSphere(un, v, radius);
                    Vector3f p3 = parametricSphere(un, vn, radius);
                    float textureU = u / endU * radius;
                    float textureV = v / endV * radius;
                    float textureUN = un / endU * radius;
                    float textureVN = vn / endV * radius;
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p0.x(), p0.y(), p0.z(), textureU, textureV);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p3.x(), p3.y(), p3.z(), textureUN, textureVN);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                }
            }

            return this;
        }

        public WorldVFXBuilder renderSphere(VertexConsumer vertexConsumer, PoseStack stack, float radius, int longs, int lats) {
            Matrix4f last = stack.last().pose();
            float startU = 0.0F;
            float startV = 0.0F;
            float endU = ((float)Math.PI * 2F);
            float endV = (float)Math.PI;
            float stepU = (endU - startU) / (float)longs;
            float stepV = (endV - startV) / (float)lats;

            for(int i = 0; i < longs; ++i) {
                for(int j = 0; j < lats; ++j) {
                    float u = (float)i * stepU + startU;
                    float v = (float)j * stepV + startV;
                    float un = i + 1 == longs ? endU : (float)(i + 1) * stepU + startU;
                    float vn = j + 1 == lats ? endV : (float)(j + 1) * stepV + startV;
                    Vector3f p0 = parametricSphere(u, v, radius);
                    Vector3f p1 = parametricSphere(u, vn, radius);
                    Vector3f p2 = parametricSphere(un, v, radius);
                    Vector3f p3 = parametricSphere(un, vn, radius);
                    float textureU = u / endU;
                    float textureV = v / endV;
                    float textureUN = un / endU;
                    float textureVN = vn / endV;
                    vertexPosColortextureLight(vertexConsumer, last, p0.x(), p0.y(), p0.z(), this.r, this.g, this.b, this.a, textureU, textureV, this.light);
                    vertexPosColortextureLight(vertexConsumer, last, p2.x(), p2.y(), p2.z(), this.r, this.g, this.b, this.a, textureUN, textureV, this.light);
                    vertexPosColortextureLight(vertexConsumer, last, p1.x(), p1.y(), p1.z(), this.r, this.g, this.b, this.a, textureU, textureVN, this.light);
                    vertexPosColortextureLight(vertexConsumer, last, p3.x(), p3.y(), p3.z(), this.r, this.g, this.b, this.a, textureUN, textureVN, this.light);
                    vertexPosColortextureLight(vertexConsumer, last, p1.x(), p1.y(), p1.z(), this.r, this.g, this.b, this.a, textureU, textureVN, this.light);
                    vertexPosColortextureLight(vertexConsumer, last, p2.x(), p2.y(), p2.z(), this.r, this.g, this.b, this.a, textureUN, textureV, this.light);
                }
            }

            return this;
        }

        public WorldVFXBuilder renderCylinder(VertexConsumer vertexConsumer, PoseStack stack, float radius, int longs, float height) {
            Matrix4f last = stack.last().pose();
            float startU = 0.0F;
            float endU = 6.2831855F;
            float stepU = (endU - startU) / (float)longs;

            int i;
            float u;
            float un;
            Vector3f p0;
            Vector3f p1;
            Vector3f p2;
            Vector3f p3;
            float textureU;
            float textureV;
            float textureUN;
            float textureVN;
            for(i = 0; i < longs; ++i) {
                u = (float)i * stepU + startU;
                un = i + 1 == longs ? endU : (float)(i + 1) * stepU + startU;
                p0 = parametricSphere(u, 0.0F, radius);
                p1 = parametricSphere(u, height, radius);
                p2 = parametricSphere(un, 0.0F, radius);
                p3 = parametricSphere(un, height, radius);
                textureU = u / endU;
                textureV = 0.0F;
                textureUN = un / endU;
                textureVN = 1.0F;
                float x0 = (float)Math.cos(u) * radius;
                float z0 = (float)Math.sin(u) * radius;
                float x1 = (float)Math.cos(un) * radius;
                float z1 = (float)Math.sin(un) * radius;

                // Bottom
                addVertex(vertexConsumer, last, x0, 0, z0, 1, 1, 1, 0.3f, u / endU, 0f, light);
                addVertex(vertexConsumer, last, x1, 0, z1, 1, 1, 1, 0.3f, un / endU, 0f, light);
                addVertex(vertexConsumer, last, x0, height, z0, 1, 1, 1, 0.3f, u / endU, 1f, light);
                addVertex(vertexConsumer, last, x1, 0, z1, 1, 1, 1, 0.3f, un / endU, 0f, light);
                addVertex(vertexConsumer, last, x1, height, z1, 1, 1, 1, 0.3f, un / endU, 1f, light);
                addVertex(vertexConsumer, last, x0, 0, z0, 1, 1, 1, 0.3f, u / endU, 1f, light);

                addVertex(vertexConsumer, last, x0, height, z0, 1, 1, 1, 0.3f, u / endU, 1f, light);
                addVertex(vertexConsumer, last, x1, 0, z1, 1, 1, 1, 0.3f, un / endU, 0f, light);
                addVertex(vertexConsumer, last, x0, 0, z0, 1, 1, 1, 0.3f, u / endU, 0f, light);

                addVertex(vertexConsumer, last, x0, 0, z0, 1, 1, 1, 0.3f, u / endU, 1f, light);
                addVertex(vertexConsumer, last, x1, height, z1, 1, 1, 1, 0.3f, un / endU, 1f, light);
                addVertex(vertexConsumer, last, x1, 0, z1, 1, 1, 1, 0.3f, un / endU, 0f, light);
            }

//            for(i = 0; i < longs; ++i) {
//                u = (float)i * stepU + startU;
//                un = i + 1 == longs ? endU : (float)(i + 1) * stepU + startU;
//                p0 = RenderHelper.parametricSphere(u, 0.0F, radius);
//                p1 = RenderHelper.parametricSphere(u, height, radius);
//                p2 = RenderHelper.parametricSphere(un, 0.0F, radius);
//                p3 = RenderHelper.parametricSphere(un, height, radius);
//                textureU = u / endU;
//                textureV = 0.0F;
//                textureUN = un / endU;
//                textureVN = 1.0F;
//                float x0 = (float)Math.cos(u) * radius;
//                float z0 = (float)Math.sin(u) * radius;
//                float x1 = (float)Math.cos(un) * radius;
//                float z1 = (float)Math.sin(un) * radius;
//
//                // Bottom
//                addVertex(vertexConsumer, last, x0, 0, z0, 1, 1, 1, 0.3f, u / endU, 0f, light);
//                addVertex(vertexConsumer, last, x1, 0, z1, 1, 1, 1, 0.3f, un / endU, 0f, light);
//                addVertex(vertexConsumer, last, x0, height, z0, 1, 1, 1, 0.3f, u / endU, 1f, light);
//                addVertex(vertexConsumer, last, x1, 0, z1, 1, 1, 1, 0.3f, un / endU, 0f, light);
//                addVertex(vertexConsumer, last, x1, height, z1, 1, 1, 1, 0.3f, un / endU, 1f, light);
//                addVertex(vertexConsumer, last, x0, height, z0, 1, 1, 1, 0.3f, u / endU, 1f, light);
//            }

            return this;
        }

        public Vector3f parametricSphere(float u, float v, float radius) {
            float x = radius * Mth.sin(v) * Mth.cos(u);
            float y = radius * Mth.cos(v);
            float z = radius * Mth.sin(v) * Mth.sin(u);
            return new Vector3f(x, y, z);
        }

        public static void vertexPosColortextureLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v, int light) {
            vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
        }

        private void addVertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z,
                               float r, float g, float b, float a,
                               float u, float v, int light) {
            consumer
                    .vertex(matrix, x, y, z)
                    .color(r, g, b, a)
                    .uv(u, v)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(0, 1, 0)
                    .endVertex();
        }

        private Vector3f spherePoint(float radius, float theta, float phi) {
            float x = (float) (radius * Math.sin(phi) * Math.cos(theta));
            float y = (float) (radius * Math.cos(phi));
            float z = (float) (radius * Math.sin(phi) * Math.sin(theta));
            return new Vector3f(x, y, z);
        }

        static {
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_POSITION, (consumer, last, builder, x, y, z, u, v) -> {
                if (last == null) {
                    consumer.vertex((double)x, (double)y, (double)z);
                } else {
                    consumer.vertex(last, x, y, z);
                }

            });
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_COLOR, (consumer, last, builder, x, y, z, u, v) -> {
                consumer.color(builder.r, builder.g, builder.b, builder.a);
            });
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_UV0, (consumer, last, builder, x, y, z, u, v) -> {
                consumer.uv(u, v);
            });
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_UV2, (consumer, last, builder, x, y, z, u, v) -> {
                consumer.uv2(builder.light);
            });
        }

        public interface WorldVertexConsumerActor {
            void placeVertex(VertexConsumer var1, Matrix4f var2, WorldVFXBuilder var3, float var4, float var5, float var6, float var7, float var8);
        }
    }
}
