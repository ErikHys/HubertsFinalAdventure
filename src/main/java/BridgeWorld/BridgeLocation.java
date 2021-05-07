package BridgeWorld;


import java.util.Random;

/**
 * A class to represent one point on the bridge
 */
public class BridgeLocation {

    private final int maxHP = 10000000;
    private boolean working;
    private boolean nestFixed;
    private final boolean start;
    private final boolean end;
    private int hp;


    /**
     * Use this constructor for start and end node
     * @param start true if start, false if end
     * @param i
     */
    public BridgeLocation(boolean start, int i) {
        this.start = start;
        end = !start;
        working = true;
        nestFixed = true;
        hp = maxHP;


    }

    /**
     * Use this constructor for all none start/end nodes
     * @param i
     */
    public BridgeLocation(int i) {
        start = false;
        end = false;
        working = true;
        nestFixed = true;
        hp = maxHP;

    }

    /**
     * Fix a hole
     */
    public void fix() {
        working = true;
        hp = maxHP;
    }

    /**
     * Make a hole
     * @param working enter false to make a hole
     */
    public void setWorking(boolean working) {
        this.working = working;
        if(!working)hp = 0;
    }


    public void setNest() {
        nestFixed = false;
    }

    public boolean isWorking() {
        return working;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isEnd() {
        return end;
    }

    public boolean isNestFixed() {
        return nestFixed;
    }

    public void fixNest() {
        nestFixed = true;
    }

    /**
     * Randomly break log, or spawn a new nest based on hp of log
     * @param random
     */
    public void step(Random random) {
        double p = random.nextDouble();
        if(p > hp/(double)maxHP && isWorking()){
            setWorking(false);
        }else hp--;
        if(p/100 > hp/(double)maxHP && isNestFixed()){
            setNest();
        }

    }
}
