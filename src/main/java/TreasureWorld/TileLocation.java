package TreasureWorld;

import Textures.GeneralTexturesMap;

import java.util.Random;

public class TileLocation {

    private final int textureInt;
    private Items item;

    public TileLocation(Items item){
        this.item = item;
        textureInt = new Random().nextInt(15);
    }

    public Items pickupItem() {
        Items temp = item;
        item = null;
        return temp;
    }

    public Items getItem() {
        return item;
    }

    public int getTextureInt() {
        return textureInt;
    }
}
