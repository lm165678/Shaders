#import "Common/ShaderLib/GLSLCompat.glsllib"
float amount = 0.60; // suitable range = 0.00 - 1.00
float power = 0.5; // suitable range = 0.0 - 1.0

void main()
{
  vec3 color = texture2D(sampler0, iTexCoord.xy).xyz;
  vec4 sum = vec4(0);
  vec3 bloom;
  
  for(int i= -3 ;i < 3; i++)
  {
    sum += texture2D(sampler0, iTexCoord + vec2(-1, i)*0.004) * amount;
    sum += texture2D(sampler0, iTexCoord + vec2(0, i)*0.004) * amount;
    sum += texture2D(sampler0, iTexCoord + vec2(1, i)*0.004) * amount;
  }
      
  if (color.r < 0.3 && color.g < 0.3 && color.b < 0.3)
  {
    bloom = sum.xyz*sum.xyz*0.012 + color;
  }
  else
  {
    if (color.r < 0.5 && color.g < 0.5 && color.b < 0.5)
      {
         bloom = sum.xyz*sum.xyz*0.009 + color;
      }
    else
      {
        bloom = sum.xyz*sum.xyz*0.0075 + color;
      }
  }
  
  //bloom = mix(color, bloom, power);
  oColor.rgb = bloom;
  oColor.a = 1.0;
}