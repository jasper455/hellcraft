package net.team.helldivers.util.Headshots;

import net.minecraft.world.phys.Vec3;

public class HeadHitboxRaw {
    public double[] offset;
    public double[] size;
    public boolean isVert;

    public HeadHitbox toHeadHitbox() {
        return new HeadHitbox(
            new Vec3(offset[0], offset[1], offset[2]),
            new Vec3(size[0], size[1], size[2]),
            isVert
        );
    }
}
