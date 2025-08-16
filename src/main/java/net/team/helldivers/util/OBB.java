package net.team.helldivers.util;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OBB {
    public Vec3 center;
    public static int[][] edges = {
                {0,1},{0,2},{0,4},
                {7,5},{7,6},{7,3},
                {1,3},{1,5},
                {2,3},{2,6},
                {4,5},{4,6}};
    public Vec3[] axes = new Vec3[3]; // local x, y, z axes (normalized)
    public float[] halfLengths = new float[3]; // half sizes along each axis
    public OBB(AABB aabb) {
        this.center = new Vec3(
            (float)((aabb.minX + aabb.maxX) / 2.0),
            (float)((aabb.minY + aabb.maxY) / 2.0),
            (float)((aabb.minZ + aabb.maxZ) / 2.0)
        );

        this.halfLengths[0] = (float)((aabb.maxX - aabb.minX) / 2.0);
        this.halfLengths[1] = (float)((aabb.maxY - aabb.minY) / 2.0);
        this.halfLengths[2] = (float)((aabb.maxZ - aabb.minZ) / 2.0);

        this.axes[0] = new Vec3(1, 0, 0);
        this.axes[1] = new Vec3(0, 1, 0);
        this.axes[2] = new Vec3(0, 0, 1);
    }
    public void rotatePitch(float radians, Vec3 axis) {
        axes[1] = rotateAroundAxis(axes[1], axes[0], radians);
        axes[2] = rotateAroundAxis(axes[2], axes[0], radians);
    }
    public void rotateYaw(float radians, Vec3 axis) {
        axes[0] = rotateAroundAxis(axes[0], axis, radians);
        axes[2] = rotateAroundAxis(axes[2], axis, radians);
    }
    public void moveX(double ammount){
        this.center = this.center.add(ammount, 0, 0);
    }
    public void moveY(double ammount){
        this.center = this.center.add(0, ammount, 0);
    }
    public void moveZ(double ammount){
        this.center = this.center.add(0, 0, ammount);
    }
    private Vec3 rotateAroundAxis(Vec3 v, Vec3 axis, float angle) {
        axis = axis.normalize();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        // Rodrigues' rotation formula
        Vec3 term1 = v.scale(cos);
        Vec3 term2 = axis.cross(v).scale(sin);
        Vec3 term3 = axis.scale(axis.dot(v) * (1 - cos));

        return term1.add(term2).add(term3).normalize();
    }
    public boolean contains(Vec3 point){
        Vec3 dir = point.subtract(center);

        // Project vector from center to point onto each axis and check bounds
        for (int i = 0; i < 3; i++){
            double dist = dir.dot(axes[i]);
            if (Math.abs(dist) > halfLengths[i]){
                return false;
            }
        }
        return true;
    }
    public Vec3[] getCorners() {
        Vec3 c = center;
        Vec3[] a = axes;
        float[] h = halfLengths;

        Vec3 x = a[0].scale(h[0]);
        Vec3 y = a[1].scale(h[1]);
        Vec3 z = a[2].scale(h[2]);

        return new Vec3[]{
            c.add(x).add(y).add(z),   // +x +y +z
            c.add(x).add(y).subtract(z), // +x +y -z
            c.add(x).subtract(y).add(z), // +x -y +z
            c.add(x).subtract(y).subtract(z), // +x -y -z
            c.subtract(x).add(y).add(z), // -x +y +z
            c.subtract(x).add(y).subtract(z), // -x +y -z
            c.subtract(x).subtract(y).add(z), // -x -y +z
            c.subtract(x).subtract(y).subtract(z), // -x -y -z
        };
    }

}
