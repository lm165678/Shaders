/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package post;

import com.jme3.asset.AssetManager;
import com.jme3.effect.Particle;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import java.util.ArrayList;

/**
 *
 * @author JhonKkk
 */
public class SimplerBloomFilter extends Filter{
    private Pass downPass;
    private Pass fastBloomPass;
    private RenderManager renderManager;
    private ViewPort viewPort;

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        this.renderManager = renderManager;
        this.viewPort = vp;
        material = new Material(manager, "MatDefs/Post/SimplerBloomNodes.j3md");
        material.selectTechnique("TextureMix", renderManager);
//        downPass = new Pass("DownPass");
//        // 二分之一分辨率进行采样
//        downPass.init(renderManager.getRenderer(), w / 4, h / 4, Image.Format.RGBA8, Image.Format.Depth);
//        downPass.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);
//        downPass.getRenderedTexture().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        
        // fastBloomPass
        final Material fastBloomPassMaterial = new Material(manager, "MatDefs/Post/SimplerBloomNodes.j3md");
        fastBloomPass = new Pass("FastBloomPass"){
            @Override
            public boolean requiresSceneAsTexture() {
                return true; //To change body of generated methods, choose Tools | Templates.
            }
            
            
        };
        fastBloomPass.init(renderManager.getRenderer(), w / 8, h / 8, Image.Format.RGBA8, Image.Format.Depth);
        fastBloomPass.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);
        fastBloomPass.getRenderedTexture().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        fastBloomPass.setPassMaterial(fastBloomPassMaterial);
        fastBloomPassMaterial.selectTechnique("FastBloom", renderManager);
        postRenderPasses = new ArrayList<>();
        postRenderPasses.add(fastBloomPass);
        
        
        // 最后,混合bloom生成最终效果
        material.setTexture("Texture2", fastBloomPass.getRenderedTexture());
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    @Override
    protected void postQueue(RenderQueue queue) {
        super.postQueue(queue); //To change body of generated methods, choose Tools | Templates.
//        renderManager.getRenderer().setBackgroundColor(ColorRGBA.BlackNoAlpha);            
//        renderManager.getRenderer().setFrameBuffer(downPass.getRenderFrameBuffer());
//        renderManager.getRenderer().clearBuffers(true, true, true);
//        renderManager.renderViewPortQueues(viewPort, false);         
//        renderManager.setForcedTechnique(null);
//        renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
    }
    
    
}
