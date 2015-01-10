/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.stage;

import com.jme3.app.Application;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.OBJLoader;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import tiled.io.*;
import tiled.core.*;

/**
 *
 * @author Heisenberg
 */
public class StageLoader {

    private Application application;

    public StageLoader(Application app) {
        application = app;
        reader = new TMXMapReader();
    }
    private TMXMapReader reader;

    private boolean tileDimensionsSet = false;
    private float commonTileWidth = 128;
    private float commonTileHeight = 128;
    public Node loadLevel(String path) throws Exception {
        AssetManager assetManager = application.getAssetManager();
        assetManager.registerLoader(TMXLoader.class, "tmx");

        Map mp = reader.readMap((InputStream) assetManager.loadAsset(path));


        // mp.getTileSets();
//
//        Texture tex = assetManager.loadTexture("Maps/Tiles/tile2.png");




        Node convmap = new Node();



//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//
//        mat.setTexture("ColorMap", tex);

        String basePath = "Maps/";
        HashMap<String, Material> tileSet = new HashMap<>();
        Vector<TileSet> tileSets = mp.getTileSets();
     
        
        Iterator<TileSet> tsiter = tileSets.iterator();
        while (tsiter.hasNext()) {
            TileSet tset = tsiter.next();
            for (Tile t : tset) {
                Texture tex = assetManager.loadTexture(basePath + "Tiles/" + t.getAssetName());
                tex.setMagFilter(Texture.MagFilter.Nearest);

                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                mat.setTransparent(true);
                mat.setTexture("ColorMap", tex);
                tileSet.put(t.getAssetName(), mat);
            }
        }


        Vector<MapLayer> layers = mp.getLayers();

        
       float lastZ = 0;
        Iterator<MapLayer> iter = layers.iterator();
        for (int z = 0; iter.hasNext(); z++) {
            MapLayer l = iter.next();

            if (l instanceof TileLayer) {
                TileLayer tl = (TileLayer) l;
                for (int x = 0; x < tl.getWidth(); x++) {
                    for (int y = 0; y < tl.getHeight(); y++) {

                        if (tl.contains(x, y)) {
                            Tile tle = tl.getTileAt(x, y);

                            if (tle != null) {
                                //System.out.println(tle.getId());

                                Quad q = new Quad(1, 1);
                                Geometry g = new Geometry("quad", q);
                                
                                g.setLocalTranslation(x, -y, z*0.001f);
                                
                                Material m = tileSet.get(tle.getAssetName());
                               if(!tileDimensionsSet) {
                                Image img= m.getTextureParam("ColorMap").getTextureValue().getImage();
                                commonTileHeight = img.getHeight();
                                commonTileWidth = img.getWidth();
                                tileDimensionsSet = true;
                               }
                                if (m.isTransparent()) {
                                    g.setQueueBucket(RenderQueue.Bucket.Transparent);


                                }
                                g.setMaterial(m);
                                //g.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);  // !
                                // cube2Geo.setQueueBucket(Bucket.Transparent);   
                                convmap.attachChild(g);
                            }
                        }
                    }
                }
            } else if (l instanceof ObjectGroup) {
                ObjectGroup og = (ObjectGroup) l;
                System.out.println("o layer");
                Iterator<MapObject> oiter = og.getObjects();
                while (oiter.hasNext()) {
                    MapObject obj = oiter.next();

                    if(obj.getType().equals("image")) {

                    Material m = tileSet.get(obj.getImageSource());
                    Image img = m.getTextureParam("ColorMap").getTextureValue().getImage();
                    Quad q = new Quad(img.getWidth()/commonTileWidth, img.getWidth()/commonTileHeight);
                    Geometry g = new Geometry("quad", q);
                    
                    g.setLocalTranslation(obj.getX()/commonTileWidth, -obj.getY()/commonTileHeight,1f+ lastZ);
                    lastZ+=0.001f;
                    if (m.isTransparent()) {
                        g.setQueueBucket(RenderQueue.Bucket.Transparent);
                        


                    }
                    g.setMaterial(m);
                    //g.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);  // !
                    // cube2Geo.setQueueBucket(Bucket.Transparent);   
                    convmap.attachChild(g);
                    } else if(obj.getType().equals("shape")) {
                        Rectangle rect = obj.getBounds();
                      
                        Box b = new Box(((float)rect.getWidth())/(2*commonTileWidth),
                               ( (float) rect.getHeight())/(2*commonTileHeight), 2f);
                        Geometry geom = new Geometry("Box", b);
                      //  System.out.println(b);
                        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                        mat.setColor("Color", ColorRGBA.Blue);
                        geom.setMaterial(mat);
                        geom.setLocalTranslation(((float)rect.getCenterX())/commonTileWidth,-((float) rect.getMinY())/commonTileHeight, 1);
                        float angle = obj.getRotationDeg();
                        geom.setLocalRotation(new Quaternion().fromAngles(0, 0, angle / 180 * FastMath.PI ));
                        convmap.attachChild(geom);
                    }
                    // System.out.println(obj.ge);
//                    System.out.println("imgsrc " +obj.getImageSource());
//                    System.out.println(obj.getBounds().x);
//                    System.out.println(obj.getImage(1));
                    //System.out.println(getAttribute(obj, "width", 1337));


                }
            }
        }
        // mp.
        return convmap;
    }
}
