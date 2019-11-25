layout (points) in;
layout (triangle_strip) out;
layout (max_vertices = 12) out;

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_ViewMatrix;
uniform float g_Time;
uniform float g_Tpf;
const float PI = 3.1415926;
const float oscillateDelta = 0.05;
const float w = 0.01f;
const float h = 0.5f;
out vec2 uvInFS;

#define PI 3.1415926
#define WindStrength 0.8
#define WindFrequency vec2(0.05, 0.05)

int LFSR_Rand_Gen(in int n)
{
    n = (n << 13) ^ n; 
    return (n * (n*n*15731+789221) + 1376312589) & 0x7fffffff;
}

mat3 RotY(float r) {
	float cosR = cos(r);
	float sinR = sin(r);
	return mat3(
		cosR, 0.0, -sinR,
		0.0,  1.0, 0.0,
		sinR, 0.0, cosR
	);
}

mat3 AngleAxis3x3(float angle, vec3 axis) {
	float c = cos(angle), s = sin(angle);

	float t = 1.0 - c;
	float x = axis.x;
	float y = axis.y;
	float z = axis.z;
	
	return mat3(
		t * x * x + c, t * x * y + s * z, t * x * z - s * y,
		t * x * y - s * z, t * y * y + c, t * y * z + s * x,
		t * x * z + s * y, t * y * z - s * x, t * z * z + c
	);
}
// 伪随机
float random (float n) {
    return fract(sin(n)*1000000.0f);
}
mat3 GetWindMat(vec2 pos) {
	vec2 uv = pos * 0.01 + WindFrequency * g_Tpf;
	//vec2 windSample = texture(texBlds[int(distortionId)], uv).xy * 2.0 - 1.0;
	//windSample *= WindStrength;
        vec2 windSample = vec2(1, 0);
	vec3 wind = normalize(vec3(windSample.x, windSample.y, 0.0));
	mat3 windRotation = AngleAxis3x3(PI * windSample.x, wind);
	return windRotation;
}


void main(){
    float currentVertexHeight = 0.0f;
    int t = -1;
    int uv = 0;
    float currentV = 0;
    float offsetV = float(1.0f / 5.0f);
    vec4 p = gl_in[0].gl_Position;
    for (int i = 1; i <= 12; i++) {

        float viewz = (g_ViewMatrix * vec4(p.xyz, 1.0)).z;
        mat3 windMat = GetWindMat(p.xz + vec2(viewz));
        mat3 rotMat = RotY(random(i) * PI);

        vec3 offset = vec3(t * w, currentVertexHeight, 0);
        //offset.y -= windForce * windCoEff * 0.8;
        offset = rotMat * offset;
        gl_Position = g_WorldViewProjectionMatrix * vec4(gl_in[0].gl_Position.xyz + offset.xyz,1.0);
        t *= -1;

        uvInFS = vec2(uv, currentV);
        uv += t;
        EmitVertex();
        if(i % 2 == 0){
            currentV += offsetV;
            currentVertexHeight = currentV * h;
        }
    }

    EndPrimitive();
}

