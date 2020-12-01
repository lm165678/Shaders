/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.ColorOverlayFilter;
import com.jme3.scene.Node;
import post.SimplerBloomFilter;

/**
 *
 * @author JhonKkk
 */
public class SimplerBloomTest extends FastApplication{
    public SimplerBloomTest(String title){
        super(title);
    }
    public static void main(String[] args) {
        new SimplerBloomTest("SimplerBloomTest").start();
    }

    @Override
    public void simpleInitApp() {
        mScene = (Node) assetManager.loadModel("Scenes/scene1.j3o");
        rootNode.attachChild(mScene);
        initCamera();
        cam.setFrustumPerspective(45.0f, mWidth / mHeight * 1.0f, 0.01f, 1000.0f);
        
        FilterPostProcessor filterPostProcessor = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(filterPostProcessor);
//        BloomFilter bloom=new BloomFilter();
//        bloom.setDownSamplingFactor(2);
//        bloom.setBlurScale(1.37f);
//        bloom.setExposurePower(3.30f);
//        bloom.setExposureCutOff(0.8f);
//        bloom.setBloomIntensity(2.45f);
//        filterPostProcessor.addFilter(bloom);
        SimplerBloomFilter sbf = new SimplerBloomFilter();
        filterPostProcessor.addFilter(sbf);
//        ColorOverlayFilter
    }
    
}
