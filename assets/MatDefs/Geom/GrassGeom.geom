layout (points) in;
layout (triangle_strip) out;
layout (max_vertices = 12) out;

uniform mat4 g_WorldViewProjectionMatrix;
uniform float g_Time;
uniform float g_Tpf;
const float PI = 3.1415926;
const float oscillateDelta = 0.05;
const float w = 0.01f;
const float h = 0.5f;
out vec2 uvInFS;

int LFSR_Rand_Gen(in int n)
{
  // <<, ^ and & require GL_EXT_gpu_shader4.
  n = (n << 13) ^ n; 
  return (n * (n*n*15731+789221) + 1376312589) & 0x7fffffff;
}

float LFSR_Rand_Gen_f( in int n )
{
  return float(LFSR_Rand_Gen(n));
}

float noise3f(in vec3 p)
{
  ivec3 ip = ivec3(floor(p));
  vec3 u = fract(p);
  u = u*u*(3.0-2.0*u);

  int n = ip.x + ip.y*57 + ip.z*113;

  float res = mix(mix(mix(LFSR_Rand_Gen_f(n+(0+57*0+113*0)),
                          LFSR_Rand_Gen_f(n+(1+57*0+113*0)),u.x),
                      mix(LFSR_Rand_Gen_f(n+(0+57*1+113*0)),
                          LFSR_Rand_Gen_f(n+(1+57*1+113*0)),u.x),u.y),
                 mix(mix(LFSR_Rand_Gen_f(n+(0+57*0+113*1)),
                          LFSR_Rand_Gen_f(n+(1+57*0+113*1)),u.x),
                      mix(LFSR_Rand_Gen_f(n+(0+57*1+113*1)),
                          LFSR_Rand_Gen_f(n+(1+57*1+113*1)),u.x),u.y),u.z);

  return 1.0 - res*(1.0/1073741824.0);
}

// 伪随机
float random (float n) {
    return fract(sin(n)*1000000.0f);
}

void main(){
    float currentVertexHeight = 0.0f;
    int t = -1;
    int uv = 0;
    float currentV = 0;
    float offsetV = float(1.0f / 5.0f);
    float windCoEff = 0;
    vec4 p = gl_in[0].gl_Position;
    for (int i = 1; i <= 12; i++) {
        vec2 wind = vec2(sin(g_Tpf * PI * 5.0f), sin(g_Tpf * PI * 5.0f));
        wind.x += (sin(g_Tpf + p.x / 25) + sin((g_Tpf + p.x / 15) + 50)) * 0.5;
        wind.y += cos(g_Tpf + p.z / 80);
        wind *= smoothstep(0.0f, 1.0f, 1.0f - random(1.0f));
        
        float oscillationStrength = 0.00f;
        float sinSkewCoeff = random(1.0f);
        float lerpCoeff = (sin(oscillationStrength * g_Tpf + sinSkewCoeff) + 1.0) / 2;
        vec2 leftWindBound = wind * (1.0f - oscillateDelta);
        vec2 rightWindBound = wind * (1.0f + oscillateDelta);

        wind = smoothstep(leftWindBound, rightWindBound, vec2(lerpCoeff, lerpCoeff));

        float randomAngle = smoothstep(-PI, PI, random(1.0f));
        float randomMagnitude = smoothstep(0.0f, 1.0f, random(1.0f));
        vec2 randomWindDir = vec2(sin(randomAngle), cos(randomAngle));
        wind += randomWindDir * randomMagnitude;
        
        float windForce = length(wind);

        vec3 offset = vec3(t * w, currentVertexHeight, 0);
        offset.xz += wind.xy * windCoEff;
        //offset.y -= windForce * windCoEff * 0.8;
        gl_Position = g_WorldViewProjectionMatrix * vec4(gl_in[0].gl_Position.xyz + offset.xyz,1.0);
        t *= -1;

        uvInFS = vec2(uv, currentV);
        uv += t;
        EmitVertex();
        if(i % 2 == 0){
            currentV += offsetV;
            currentVertexHeight = currentV * h;
        }
        if(i % 2 == 1){
            windCoEff += offsetV;
        }
    }

    EndPrimitive();
}

