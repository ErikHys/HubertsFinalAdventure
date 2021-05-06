package Textures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GeneralTexturesMap {

    public static Texture FOREST = new Texture("Textures/forest_tiles.png");
    public static TextureRegion[] GROUNDTILES = new TextureRegion[]{
            new TextureRegion(FOREST, 0, 0, 32, 32),
            new TextureRegion(FOREST, 32, 0, 32, 32),
            new TextureRegion(FOREST, 64, 0, 32, 32),
            new TextureRegion(FOREST, 96, 0, 32, 32),
            new TextureRegion(FOREST, 128, 0, 32, 32),
            new TextureRegion(FOREST, 0, 32, 32, 32),
            new TextureRegion(FOREST, 32, 32, 32, 32),
            new TextureRegion(FOREST, 64, 32, 32, 32),
            new TextureRegion(FOREST, 96, 32, 32, 32),
            new TextureRegion(FOREST, 128, 32, 32, 32),
            new TextureRegion(FOREST, 0, 64, 32, 32),
            new TextureRegion(FOREST, 32, 64, 32, 32),
            new TextureRegion(FOREST, 64, 64, 32, 32),
            new TextureRegion(FOREST, 96, 64, 32, 32),
            new TextureRegion(FOREST, 128, 64, 32, 32)
    };
    public static TextureRegion[] CORNERS = new TextureRegion[]{
            new TextureRegion(FOREST, 128, 32*11, 32, 32),
            new TextureRegion(FOREST, 128, 32*10, 32, 32),
            new TextureRegion(FOREST, 9*32, 32*11, 32, 32),
            new TextureRegion(FOREST, 9*32, 32*10, 32, 32)
    };
    public static TextureRegion[] TOP = new TextureRegion[]{
            new TextureRegion(FOREST, 192, 128, 32, 32),
            new TextureRegion(FOREST, 224, 128, 32, 32),
            new TextureRegion(FOREST, 256, 128, 32, 32)
    };
    public static TextureRegion[] BOT = new TextureRegion[]{
            new TextureRegion(FOREST, 0, 128, 32, 32),
            new TextureRegion(FOREST, 32, 128, 32, 32),
            new TextureRegion(FOREST, 64, 128, 32, 32)
    };
    public static TextureRegion[] RIGHT = new TextureRegion[]{
            new TextureRegion(FOREST, 288, 128, 32, 32),
            new TextureRegion(FOREST, 320, 128, 32, 32),
            new TextureRegion(FOREST, 352, 128, 32, 32)
    };
    public static TextureRegion[] LEFT = new TextureRegion[]{
            new TextureRegion(FOREST, 96, 128, 32, 32),
            new TextureRegion(FOREST, 128, 128, 32, 32),
            new TextureRegion(FOREST, 160, 128, 32, 32)
    };
    public static TextureRegion JEWEL = new TextureRegion(new Texture("Textures/03.png"), 0, 0, 76, 88);

    public static TextureRegion PLANTCHEM = new TextureRegion(FOREST, 32*13, 32, 32, 32);
    public static TextureRegion STONEWALL = new TextureRegion(FOREST, 32*11, 32, 32, 32);
    public static TextureRegion ROBOT = new TextureRegion(new Texture("Textures/elias_robot.png"));
}
