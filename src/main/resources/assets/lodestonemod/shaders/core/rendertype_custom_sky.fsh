#version 150

#moj_import <matrix.glsl>

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D Sampler0;

void main() {
    fragColor = texture(Sampler0, texCoord);
}