package TreasureWorld;

import Textures.GeneralTexturesMap;

import java.util.Random;

public class TileLocation {

    private final int textureInt;
    private Items item;
    private boolean hasBin;
    private boolean hasSafe;

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

    public boolean isJewel(){
        return item instanceof Jewel;
    }
    public int getTextureInt() {
        return textureInt;
    }

    public void chemical() {
        item = new Chemical();
    }

    public boolean isChemical() {
        return item instanceof Chemical;
    }

    public void wall() {
        item = new Wall();
    }

    public boolean isWall() {
        return item instanceof Wall;
    }

    public void garbage() {
        item = new Garbage();
    }

    public boolean isGarbage() {
        return item instanceof Garbage;
    }

    public void bin() {
        hasBin = true;
    }

    public void safe(){
        hasSafe = true;
    }

    public boolean isHasBin() {
        return hasBin;
    }

    public boolean isHasSafe() {
        return hasSafe;
    }
}
