package Textures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoadingBarTextures {
    public static Texture texture = new Texture("Textures/loading_bar_frames.png");
    public static TextureRegion[] bars = new TextureRegion[]{
            new TextureRegion(texture, 0, 30, 486, 40),
            new TextureRegion(texture, 0, 151, 486, 40),
            new TextureRegion(texture, 0, 272, 486, 40),
            new TextureRegion(texture, 0, 393, 486, 40),
            new TextureRegion(texture, 0, 514, 486, 40),
            new TextureRegion(texture, 0, 635, 486, 40),
            new TextureRegion(texture, 0, 756, 486, 40),
            new TextureRegion(texture, 0, 877, 486, 40)};
}
