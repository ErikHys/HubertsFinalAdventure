package BridgeWorld;

import com.badlogic.gdx.graphics.Texture;

public class BridgeLocation {

    private boolean working;
    private boolean nest;
    private boolean nestFixed;
    private boolean bird;
    private final boolean start;
    private final boolean end;
    private int textureLength;
    private final int index;



    public BridgeLocation(boolean start, int i) {
        this.start = start;
        end = !start;
        textureLength = 1;
        nest = false;
        bird = false;
        working = true;
        index = i;
        nestFixed = true;

    }

    public BridgeLocation(int i) {
        start = false;
        end = false;
        textureLength = 1;
        nest = false;
        bird = false;
        working = true;
        index = i;
        nestFixed = true;
    }

    public void fix() {
        working = true;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public void setBird(boolean bird) {
        this.bird = bird;
        if(bird) textureLength++;
    }

    public void removeBird(){
        bird = false;
        textureLength--;
    }

    public void setNest(boolean nest) {
        this.nest = nest;
        if (nest)textureLength++;
        nestFixed = false;
    }

    public boolean isWorking() {
        return working;
    }



    public boolean isNest() {
        return nest;
    }

    public boolean isBird() {
        return bird;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isEnd() {
        return end;
    }

    public Texture[] getTextures(){
        Texture[] textures = new Texture[textureLength];

        return null;
    }

    public int getIndex() {
        return index;
    }

    public double getStateValue(){
        double max = 1.0;
        if (isStart()) return 3.0/max;
        if (isEnd()) return 4.0/max;
        if (!isWorking()) return 1.0/max;
        if (isWorking()) return 2.0/max;
        return 0;
    }

    public boolean isNestFixed() {
        return nestFixed;
    }

    public void fixNest() {
        nestFixed = true;
    }
}
