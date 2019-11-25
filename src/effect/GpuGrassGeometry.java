/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package effect;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.Arrays;

/**
 * 基于gpu加速渲染实现的草体<br>
 * 缺点是几何着色器性能并不是最好的
 * @author JhonKkk
 */
public class GpuGrassGeometry extends Node{
    protected AssetManager mAssetManager;
    public GpuGrassGeometry(){
        
    }
    /**
     * 创建草坪
     * @param location 草坪中心位置
     * @param w 草坪宽度
     * @param h 草坪高度
     * @param wsli
     * @param hsli
     * @param assetManager 
     */
    public Geometry mark(Vector3f location, int w, int h, int wsli, int hsli, AssetManager assetManager){
        mAssetManager = assetManager;
        //计算宽度方向每一个草的占率
        float wfv = wsli * 1.0f / w;
        float hfv = hsli * 1.0f / h;
        wfv = 1.0f / wfv;
        hfv = 1.0f / hfv;
        //创建草位置信息
        float ws = w * 0.5f;
        float hs = h * 0.5f;
        Vector3f g = location.subtract(ws, 0, hs);
        float[] positions = new float[(int)(wsli * hsli * 3)];
        int[] indices = new int[wsli * hsli];
        int tg = 0;
        int id = 0;
        float x = g.x, z = 0;
        for(int iw = 0;iw < wsli;iw++){
            x += wfv;
            z = g.z;
            for(int ih = 0;ih < hsli;ih++){
//                g.x += wfv;
                z += hfv;
                positions[tg] = x;
                positions[tg + 1] = g.y;
                positions[tg + 2] = z;
                tg+=3;
                indices[id] = id;
                id++;
            }
        }
//        System.out.println("位置:" + Arrays.toString(positions));
//        System.out.println("索引:" + Arrays.toString(indices));
        return markGrass(indices, positions);
    }
    /**
     * 创建草
     * @param location
     * @return 
     */
    protected Geometry markGrass(int[] indices, float[] positions){
        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(positions));
        mesh.setMode(Mesh.Mode.Points);
        mesh.setBound(new BoundingBox(new Vector3f(0, 0, 0), 10, 10, 10));
        mesh.updateCounts();
        Geometry geometry = new Geometry("Test", mesh);
        geometry.updateGeometricState();
        geometry.setMaterial(new Material(mAssetManager, "MatDefs/Geom/GrassGeom.j3md"));
        geometry.getMaterial().setTexture("GrassShapeMap", mAssetManager.loadTexture("Textures/grass/shape.png"));
        geometry.getMaterial().setTexture("GrassColorMap", mAssetManager.loadTexture("Textures/grass/color.png"));
        geometry.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        geometry.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Off);
        geometry.getMaterial().setReceivesShadows(true);
        geometry.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        return geometry;
    }
}
