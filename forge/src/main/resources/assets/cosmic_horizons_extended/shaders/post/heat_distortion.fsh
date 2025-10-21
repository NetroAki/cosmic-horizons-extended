#version 150

// Input from the previous render pass
uniform sampler2D DiffuseSampler;

// Uniforms
uniform float Time;
uniform float Intensity;
uniform float Speed;
uniform float Distortion;
uniform float Rise;

// Input from vertex shader
in vec2 texCoord;

// Output to the screen
out vec4 fragColor;

// Noise function for distortion
float rand(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

// Simple noise function
float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    
    // Four corners in 2D of a tile
    float a = rand(i);
    float b = rand(i + vec2(1.0, 0.0));
    float c = rand(i + vec2(0.0, 1.0));
    float d = rand(i + vec2(1.0, 1.0));
    
    // Smooth interpolation
    vec2 u = f * f * (3.0 - 2.0 * f);
    
    // Mix 4 corners
    return mix(a, b, u.x) + 
           (c - a) * u.y * (1.0 - u.x) + 
           (d - b) * u.x * u.y;
}

void main() {
    // Base UV coordinates
    vec2 uv = texCoord;
    
    // Only apply effect to the upper part of the screen (hot air rises)
    float heightFactor = 1.0 - uv.y;
    
    // Calculate distortion amount based on intensity and height
    float distortion = Intensity * Distortion * heightFactor * heightFactor;
    
    // Add some noise-based distortion
    float noise1 = noise(uv * 2.0 + Time * Speed * 0.5);
    float noise2 = noise(uv * 4.0 - Time * Speed * 0.3);
    
    // Combine noises for more natural look
    float noisePattern = mix(noise1, noise2, 0.5);
    
    // Apply distortion to UVs
    uv.x += (noisePattern - 0.5) * distortion;
    
    // Sample the texture with distorted UVs
    vec4 color = texture(DiffuseSampler, uv);
    
    // Add a slight color shift for heat effect (reddish tint)
    float heatEffect = Intensity * heightFactor * 0.1;
    color.rgb = mix(color.rgb, vec3(1.0, 0.7, 0.5), heatEffect * 0.3);
    
    // Output the final color
    fragColor = color;
}
