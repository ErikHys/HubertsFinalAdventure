package Textures;

import BridgeWorld.Bridge;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GeneralTexturesBridge {
    public static Texture HUBERT_LEFT_TEX = new Texture("Textures/Wall-e_Robot_left.png");
    public static Texture HUBERT_RIGHT_TEX = new Texture("Textures/Wall-e_Robot_right.png");
    public static TextureRegion HUBERT_LEFT = new TextureRegion(HUBERT_LEFT_TEX, 0, 0, 32, 32);
    public static TextureRegion HUBERT_RIGHT = new TextureRegion(HUBERT_RIGHT_TEX, 0, 0, 32, 32);
    public static Texture BRIDGE = new Texture("Textures/bridge.png");
    public static TextureRegion BRIDGE_WORKING = new TextureRegion(BRIDGE, 0, 0, 32, 96);
    public static TextureRegion BRIDGE_HOLE = new TextureRegion(BRIDGE, 0, 110, 32, 43);
    public static Texture[] BGS = new Texture[]{
            new Texture("Textures/Backgrounds/skill-desc_0003_bg.png"),
            new Texture("Textures/Backgrounds/skill-desc_0002_far-buildings.png"),
            new Texture("Textures/Backgrounds/skill-desc_0001_buildings.png"),
            new Texture("Textures/Backgrounds/skill-desc_0000_foreground.png")
    };
    public static Texture NEST = new Texture("Textures/bird-nest.png");

}
