#version 150

uniform sampler2D Sampler0;
uniform sampler2D LightMap;
uniform sampler2D Sampler2;

uniform vec4 ColorModulator;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

// Simple pseudo-random function
float random(vec2 p) {
    vec2 k1 = vec2(23.14069263277926, 2.665144142690225);
    return fract(cos(dot(p, k1)) * 12345.6789);
}

void main() {
    // Sample the texture
    vec4 color = texture(Sampler0, texCoord0);
    
    // Discard transparent fragments
    if (color.a < 0.1) {
        discard;
    }
    
    // Calculate light level from lightmap
    vec2 lightUV = vec2(0.0, 0.0); // Simplified for now
    vec4 lightmap = texture(LightMap, lightUV);
    float lightLevel = max(lightmap.r, lightmap.g * 0.5);
    
    // Add some sparkle effect
    float time = GameTime * 0.5;
    vec2 uv = texCoord0 * 10.0;
    float sparkle = sin(uv.x * 20.0 + time * 10.0) * cos(uv.y * 20.0 + time * 8.0);
    sparkle = smoothstep(0.8, 1.0, sparkle * sparkle * 2.0);
    
    // Refraction effect
    vec2 refractOffset = normal.xy * 0.02 * (0.5 + 0.5 * sin(time * 2.0));
    vec4 refractColor = texture(Sampler0, texCoord0 + refractOffset);
    
    // Combine effects
    color = mix(color, refractColor, 0.3 * lightLevel);
    color.rgb += vec3(sparkle * 0.5 * lightLevel);
    
    // Apply fog
    float fogFactor = smoothstep(0.75, 1.0, vertexDistance / 16.0);
    color.rgb = mix(color.rgb, vec3(0.8, 0.9, 1.0), fogFactor * 0.7);
    
    // Final color with alpha
    fragColor = color * vertexColor * ColorModulator;
}
