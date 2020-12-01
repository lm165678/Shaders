uniform sampler2D m_GrassShapeMap;
uniform sampler2D m_GrassColorMap;
in vec2 uvInFS;
void main(){
 //gl_FragColor=vec4(1.0,0.0,1.0,0.5);
 vec4 alpha = texture2D(m_GrassShapeMap, uvInFS);
 vec4 color = texture2D(m_GrassColorMap, uvInFS);
 gl_FragColor = vec4(color.rgb, alpha.g);
}