/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.stage;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Heisenberg
 */
public class TMXLoader implements AssetLoader {

    @Override
    public InputStream load(AssetInfo assetInfo) throws IOException {
        return assetInfo.openStream();
    }
}
