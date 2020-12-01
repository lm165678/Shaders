package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jme3.app.SimpleApplication;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JhonKkk
 */
public abstract class FastApplication extends SimpleApplication{

    protected int mWidth;
    protected int mHeight;
    protected FilterPostProcessor mFilterPostProcessor;
    protected BloomFilter mBloomFilter;
    protected FXAAFilter mFXAAFilter;
    protected Node mScene;
    public FastApplication() {
        this(900, 550, "FastApplication");
    }
    
    public FastApplication(String title){
        this(900, 550, title);
    }

    @Override
    public void initialize() {
        super.initialize(); //To change body of generated methods, choose Tools | Templates.
        
    }
    
    protected void initCamera(){
        if(mScene != null){
            Node mainCamera = (Node) mScene.getChild("mainCamera");
            if(mainCamera != null){
                cam.setLocation(mainCamera.getLocalTranslation());
                cam.setRotation(mainCamera.getLocalRotation());
                Object near = mainCamera.getUserData("near");
                if(near != null){
                    cam.setFrustumNear((float) near);
                }
                Object far = mainCamera.getUserData("far");
                if(far != null){
                    cam.setFrustumFar((float) far);
                }
                Object moveSpeed = mainCamera.getUserData("moveSpeed");
                if(moveSpeed != null){
                    flyCam.setMoveSpeed((float) moveSpeed);
                }
            }
        }
    }
    
    protected void markFilterPost(){
        FilterPostProcessor filterPostProcessor = null;
        //添加fxaa
        if(mFilterPostProcessor == null){
            filterPostProcessor = new FilterPostProcessor(assetManager);
            filterPostProcessor.setNumSamples(4);
        }
        else{
            filterPostProcessor = mFilterPostProcessor;
        }
        
        
        //创建bloom
        BloomFilter bloom=new BloomFilter(BloomFilter.GlowMode.Scene);
//        bloom.setDownSamplingFactor(2);
//        bloom.setBlurScale(1.3f);
//        bloom.setExposurePower(3.30f);
//        bloom.setExposureCutOff(0.1f);
//        bloom.setBloomIntensity(2.45f);
        filterPostProcessor.addFilter(bloom);
        mBloomFilter = bloom;
        
        FXAAFilter fxaaf = new FXAAFilter();
        fxaaf.setReduceMul(0.0f);
        fxaaf.setSubPixelShift(0.0f);
        filterPostProcessor.addFilter(fxaaf);
        mFXAAFilter = fxaaf;
        
        if(mFilterPostProcessor == null){
            mFilterPostProcessor = filterPostProcessor;
            viewPort.addProcessor(mFilterPostProcessor);
        }
    }
    
    public void focusGained(){
        Robot rb = null;
        try {
            rb = new Robot();
            rb.keyPress(KeyEvent.VK_CONTROL);
            rb.keyPress(KeyEvent.VK_SHIFT);
            rb.keyRelease(KeyEvent.VK_CONTROL);
            rb.keyRelease(KeyEvent.VK_SHIFT);
        } catch (AWTException ex) {
            Logger.getLogger(FastApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FastApplication(int w, int h, String title){
        super();
        this.mWidth = w;
        this.mHeight = h;
        //设置视锥范围
        settings = new AppSettings(true);
        //伽马矫正
        settings.setGammaCorrection(true);
        //4x采样
        settings.setSamples(4);
        settings.setResolution(this.mWidth, this.mHeight);
        settings.setTitle(title);
        setShowSettings(false);
    }
    
}
