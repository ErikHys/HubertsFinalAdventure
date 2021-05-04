package BridgeWorld;

import java.util.Random;

public class Bridge {
    private BridgeLocation[] bridgeItems;
    private BridgeHubert hubert;

    public Bridge(int length){
        bridgeItems = new BridgeLocation[length];
        bridgeItems[0] = new BridgeLocation(true, 0);
        bridgeItems[bridgeItems.length-1] = new BridgeLocation(false, bridgeItems.length-1);
        for (int i = 1; i < bridgeItems.length-1; i++) {
            bridgeItems[i] = new BridgeLocation(i);
        }
        setupBridge();
    }


    public void setHubert(BridgeHubert hubert) {
        this.hubert = hubert;
    }

    public void printBridge(){
        for (int i = 0; i < bridgeItems.length; i++) {
            BridgeLocation bridgeLoc = bridgeItems[i];
            if (hubert != null && hubert.getLoc() == i) System.out.print("H");
            else if (bridgeLoc.isStart()) System.out.print("S");
            else if (bridgeLoc.isEnd()) System.out.print("E");
            else if (bridgeLoc.isWorking()) System.out.print("=");
            else System.out.print("X");
        }
        System.out.println();

    }

    private void setupBridge() {
        Random random = new Random();
        double p = 0.2;
        for (int i = 1; i < bridgeItems.length-1; i++) {
            if(random.nextDouble() < p){
                bridgeItems[i].setWorking(false);
            }else if(random.nextDouble() < p){
                bridgeItems[i].setNest(true);
            }
        }
    }


    public BridgeLocation[] getView(int view, int loc){
        BridgeLocation[] hubertFOV = new BridgeLocation[view+1];
        for (int i = loc, j = 0; i <= loc+view; i++, j++) {
            hubertFOV[j] = bridgeItems[i];
        }
        return hubertFOV;
    }

    public void fixLoc(int loc){
        bridgeItems[loc].fix();
    }

    public BridgeLocation getLoc(int loc) {
        return bridgeItems[loc];
    }

    public int getLength() {
        return bridgeItems.length;
    }


    public int getDist(int fov, int loc) {
        for (int i = 1; i < fov; i++) {
            if(i+loc == getLength()) break;
            if(!bridgeItems[loc+i].isWorking()){
                return i;
            }
        }
        return 0;
    }

    public int getDistStart(int fov, int loc) {
        for (int i = 0; i < fov; i++) {
            if(i+loc == getLength()) break;
            if(!bridgeItems[loc+i].isStart()){
                return i;
            }
        }
        return fov;
    }

    public void fixNest(int loc) {
        bridgeItems[loc].fixNest();
    }
}
