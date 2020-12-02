float power = 0.5; // suitable range = 0.0 - 1.0
void main()
{
    vec3 color0 = texture2D(sampler0, iTexCoord.xy).xyz;
    vec3 color1 = texture2D(sampler1, iTexCoord.xy).xyz;
    oColor.rgb = mix(color0, color1, power);
    oColor.a = 1.0;
}