package BridgeWorld.Weights;

import BridgeWorld.Action;


public interface IWeights {


    Action getBestAction(int loc, int distStart, int hasLog, int hasBrokenNest, int maxNests, int charge);

    void update(double r, int dist, int oldLog, int actionIndex, int newDist, int newLog, int actionIndex1,
                int distStart, int newDistStart, int oldNest, int newNest, int maxNests, int newMaxNests, int charge, int newCharge);
}
