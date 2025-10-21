#version 150

// Input vertex data
in vec2 Position;
in vec2 UV0;

// Output to fragment shader
out vec2 texCoord;

void main() {
    // Pass through the texture coordinates
    texCoord = UV0;
    
    // Output the position
    gl_Position = vec4(Position.xy, 0.0, 1.0);
}
