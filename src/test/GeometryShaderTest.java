/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 *
 * @author JhonKkk
 */
public class GeometryShaderTest extends FastApplication{

    public GeometryShaderTest(String title){
        super(title);
    }
    
    @Override
    public void simpleInitApp() {
        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(new int[]{0}));
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(new float[]{0, 0, 0}));
        mesh.setMode(Mesh.Mode.Points);
        mesh.setBound(new BoundingBox(new Vector3f(0, 0, 0), 10, 10, 10));
        mesh.updateCounts();
        Geometry geometry = new Geometry("Test", mesh);
        geometry.updateGeometricState();
        geometry.setMaterial(new Material(assetManager, "Materials/Geom/SimpleGeom.j3md"));
        rootNode.attachChild(geometry);
    }
    
    public final static void main(String[] args){
        new GeometryShaderTest("GeometryShaderTest").start();
    }
}
