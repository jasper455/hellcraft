package net.team.helldivers.util.Headshots;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HeadHitbox {
    public Vec3 offset;
    public Vec3 size;
    public boolean isVert;//this feild determines if the body plan is vertical or horizontal. basically if the hitbox should be rotated with the body or not

    public HeadHitbox(Vec3 offset, Vec3 size, boolean isVert) {
        this.offset = offset;
        this.size = size;
        this.isVert = isVert;
    }
    public boolean isVert(){
        return isVert;
    }

    public AABB getBox(AABB boundingBox) {
        Vec3 center = boundingBox.getCenter().add(offset);
        double x = size.x / 2.0;
        double y = size.y / 2.0;
        double z = size.z / 2.0;
        return new AABB(
            center.x - x, center.y - y, center.z - z,
            center.x + x, center.y + y, center.z + z
        );
    }
}
