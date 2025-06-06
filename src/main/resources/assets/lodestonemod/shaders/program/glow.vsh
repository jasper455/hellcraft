#version 150

#moj_import <lodestone:common_math.glsl>

// Samplers
uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;
// Multi-Instance uniforms
uniform samplerBuffer DataBuffer;
uniform int InstanceCount;
// Matrices needed for world position calculation
uniform mat4 ProjMat;
uniform mat4 invProjMat;
uniform mat4 invViewMat;
uniform vec3 cameraPos;
uniform float InSize;
uniform float OutSize;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 diffuseColor = texture(DiffuseSampler, texCoord);
    vec3 worldPos = getWorldPos(MainDepthSampler, texCoord, invProjMat, invViewMat, cameraPos);
    // Its important to set the fragColor to the diffuseColor before applying the effect!
    fragColor = diffuseColor;

    float radius = 10.0; // Change this value to modify the falloff of the light, or make it a uniform
    for (int instance = 0; instance < InstanceCount; instance++) {
        int index = instance * 6;// Each instance has 6 values
        // 0-2: center, 3-5: color
        vec3 center = fetch3(DataBuffer, index);
        vec3 color = fetch3(DataBuffer, index + 3);

        float distance = length(worldPos - center);
        if (distance <= radius) {
            float falloff = 1.0 - clamp(distance / radius, 0.0, 1.0);
            fragColor.rgb *= (color * falloff + 1.0);
        }
    }
}