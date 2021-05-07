package TreasureWorld;


import java.util.Random;

/**
 * Store all the information of a single tile
 */
public class TileLocation {

    private final int textureInt;
    private Items item;
    private boolean hasBin;
    private boolean hasSafe;


    /**
     *
     * @param item The item belonging to this tile
     */
    public TileLocation(Items item){
        this.item = item;
        textureInt = new Random().nextInt(15);

    }

    /**
     * Remove the item
     * @return
     */
    public Items pickupItem() {
        Items temp = item;
        item = null;
        return temp;
    }

    /**
     *
     * @return item on this tile
     */
    public Items getItem() {
        return item;
    }

    public boolean isJewel(){
        return item instanceof Jewel;
    }
    public int getTextureInt() {
        return textureInt;
    }

    /**
     * Make this tile a chemical tile
     */
    public void chemical() {
        item = new Chemical();
    }


    public boolean isChemical() {
        return item instanceof Chemical;
    }

    /**
     * Make this tile a wall tile
     */
    public void wall() {
        item = new Wall();
    }

    public boolean isWall() {
        return item instanceof Wall;
    }

    /**
     * Spawn some garbage on this tile
     */
    public void garbage() {
        if(!isJewel() && !isChemical())item = new Garbage();
    }

    public boolean isGarbage() {
        return item instanceof Garbage;
    }

    public void bin() {
        hasBin = true;
    }

    public boolean isHasBin() {
        return hasBin;
    }

    public boolean isTreasure() {
        return item instanceof Treasure;
    }

    /**
     * Spawn some treasure on this tile
     */
    public void treasure() {
        item = new Treasure();
    }
}
