/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.BlenderKey;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import java.util.Collection;

/**
 *
 * @author JhonKkk
 */
public class FbxLoadTest extends FastApplication{

    @Override
    public void simpleInitApp() {
        AmbientLight al = new AmbientLight(ColorRGBA.White);
        rootNode.addLight(al);
        BlenderKey blenderKey = new BlenderKey("Models/blender/dog.blend");
        Spatial dog = assetManager.loadModel(blenderKey);
        
        rootNode.attachChild(dog);
        
        dog = rootNode.getChild("dog.ms3d.ao");
        dog.center();
        System.out.println("dog:" + dog);
        AnimControl animControl = dog.getControl(AnimControl.class);
        Collection<String> s = animControl.getAnimationNames();
        AnimChannel cc = animControl.createChannel();
        for(String sg : s){
            System.out.println("test.FbxLoadTest.simpleInitApp():" + sg);
        }
    }
    
    public final static void main(String[] args){
        new FbxLoadTest().start();
    }
    
}
