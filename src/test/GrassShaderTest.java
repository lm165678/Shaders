/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jme3.bounding.BoundingBox;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.shader.UniformBinding;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.terrain.Terrain;
import com.jme3.util.BufferUtils;
import effect.GpuGrassGeometry;

/**
 *
 * @author JhonKkk
 */
public class GrassShaderTest extends FastApplication{

    public GrassShaderTest(String title){
        super(title);
    }
    
    @Override
    public void simpleInitApp() {
        mScene = (Node) assetManager.loadModel("Scenes/scene1.j3o");
        Geometry quad = (Geometry) mScene.getChild("Quad");
//        Geometry grass = markGrass(quad.getWorldTranslation());
        GpuGrassGeometry grass = new GpuGrassGeometry("grass", assetManager);
//        grass.markGrass(quad.getWorldTranslation(), 5, 5, 70, 90, assetManager);
//        grass.addGrassLocation(quad.getWorldTranslation());
//        grass.markGrass();
        Terrain terrain = (Terrain) mScene.getChild("terrain-scene1");
        //在地形周围250个点创建草体
        Vector2f l2 = new Vector2f(quad.getWorldTranslation().x, quad.getWorldTranslation().z);
        for(float i = 0;i < 10;i+=0.01f){
            for(float ii = 0;ii < 10;ii+=0.01f){
                grass.addGrassLocation(new Vector3f(l2.x + i, terrain.getHeight(new Vector2f(l2.x + i, l2.y + ii)), l2.y + ii));
            }
        }
        grass.markGrass();
        initCamera();
        cam.setFrustumPerspective(45.0f, mWidth / mHeight * 1.0f, 0.01f, 1000.0f);
        markFilterPost();
//        initShoaw();
        rootNode.attachChild(mScene);
        rootNode.attachChild(grass);
    }
    
    protected void initShoaw(){
        DirectionalLight l = (DirectionalLight) mScene.getLocalLightList().get(1);
        int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(l);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.8f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
        dlsr.displayDebug();
        viewPort.addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(l);
        dlsf.setLambda(0.55f);
        dlsf.setShadowIntensity(0.8f);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
        dlsf.setEnabled(false);
        mFilterPostProcessor.addFilter(dlsf);
    }
    
    /**
     * 创建草
     * @param location
     * @return 
     */
    protected Geometry markGrass(Vector3f location){
        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(new int[]{0}));
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(new float[]{location.x, location.y, location.z}));
        mesh.setMode(Mesh.Mode.Points);
        mesh.setBound(new BoundingBox(new Vector3f(0, 0, 0), 10, 10, 10));
        mesh.updateCounts();
        Geometry geometry = new Geometry("Test", mesh);
        geometry.updateGeometricState();
        geometry.setMaterial(new Material(assetManager, "MatDefs/Grass/GrassGeom.j3md"));
        geometry.getMaterial().setTexture("GrassShapeMap", assetManager.loadTexture("Textures/grass/shape.png"));
        geometry.getMaterial().setTexture("GrassColorMap", assetManager.loadTexture("Textures/grass/color.png"));
        geometry.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        geometry.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        return geometry;
    }
    
    public final static void main(String[] args){
        new GrassShaderTest("GeometryShaderTest").start();
    }
}
