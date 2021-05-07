package BridgeWorld;

import java.util.Random;

/**
 * Bridge class, creates a random bridge give a size.
 */
public class Bridge {
    private BridgeLocation[] bridgeItems;
    private Random random;

    public Bridge(int length){
        bridgeItems = new BridgeLocation[length];
        bridgeItems[0] = new BridgeLocation(true, 0);
        bridgeItems[bridgeItems.length-1] = new BridgeLocation(false, bridgeItems.length-1);
        for (int i = 1; i < bridgeItems.length-1; i++) {
            bridgeItems[i] = new BridgeLocation(i);
        }
        setupBridge();
    }

    private void setupBridge() {
        random = new Random();
        double p = 0.2;
        for (int i = 1; i < bridgeItems.length-1; i++) {
            if(random.nextDouble() < p){
                bridgeItems[i].setWorking(false);
            }else if(random.nextDouble() < p){
                bridgeItems[i].setNest();
            }
        }
    }

    /**
     * Fixes a log at given location
     * @param loc location of broken log
     */
    public void fixLoc(int loc){
        bridgeItems[loc].fix();
    }

    /**
     *
     * @param loc Location of Bridge
     * @return {@link BridgeLocation} The specified location
     */
    public BridgeLocation getLoc(int loc) {
        return bridgeItems[loc];
    }


    public int getLength() {
        return bridgeItems.length;
    }


    /**
     * Get distance to closest hole
     * @param fov Length Hubert can see
     * @param loc Location of Hubert
     * @return distance to closest Hole
     */
    public int getDist(int fov, int loc) {
        for (int i = 1; i < fov; i++) {
            if(i+loc == getLength()) break;
            if(!bridgeItems[loc+i].isWorking()){
                return i;
            }
        }
        return 0;
    }

    /**
     * Decays all logs to the right of location
     * @param loc
     */
    public void step(int loc){
        for (int i = loc+1; i < getLength(); i++) {
            bridgeItems[i].step(random);
        }
    }

    /**
     * Fix a birds nests at location
     * @param loc
     */
    public void fixNest(int loc) {
        bridgeItems[loc].fixNest();
    }
}
