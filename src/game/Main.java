package game;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import game.stage.StageLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        SpriteManager man = new SpriteManager(1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, rootNode, assetManager);
       stateManager.attach(man);
       StageLoader sl = new StageLoader(this);
        try {
            rootNode.attachChild(sl.loadLevel("Maps/testmap.tmx"));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        flyCam.setMoveSpeed(15);
        //getCamera().setParallelProjection(true);
        System.out.println("test");
        System.out.println("Penispenispenis");
        
        
//        Box b = new Box(1, 1, 1);
//        Geometry geom = new Geometry("Box", b);
//
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//        geom.setMaterial(mat);
//
//        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
//         getCamera().setFrustumTop(getCamera().getFrustumTop()+tpf);
//         getCamera().setFrustumBottom(getCamera().getFrustumBottom()-tpf);
//         getCamera().setFrustumRight(getCamera().getFrustumRight()+tpf);
//         getCamera().setFrustumLeft(getCamera().getFrustumLeft()-tpf);

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code

    }
}
