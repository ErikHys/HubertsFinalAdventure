package BridgeWorld.Weights;

import BridgeWorld.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Tabular implements IWeights{

    private final double[][][][][][] weights;
    private final Random random;
    private final double alpha;

    public Tabular(int fov, double alpha){
        random = new Random();
        weights = new double[2][fov][2][2][2][4];
        this.alpha = alpha;
    }



    @Override
    public Action getBestAction(int dist, int distStart, int hasLog, int hasBrokenNest, int maxNests) {
        ArrayList<Integer> maxIndices = new ArrayList<>();
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        for (int i = 0; i < 4; i++) {
            if(weights[hasLog][dist][distStart][hasBrokenNest][maxNests][i] > max){
                maxIndices = new ArrayList<>();
                max = weights[hasLog][dist][distStart][hasBrokenNest][maxNests][i];
                maxIdx = i;
                maxIndices.add(maxIdx);
            }else if (weights[hasLog][dist][distStart][hasBrokenNest][maxNests][i] == max){
                maxIndices.add(i);
            }
        }

        return Action.getActionByIndex(maxIndices.get(random.nextInt(maxIndices.size())));
    }

    @Override
    public void update(double r, int dist, int oldLog, int actionIndex, int newDist, int newLog, int actionIndex1,
                       int distStart, int newDistStart, int oldNest, int newNest, int maxNests, int newMaxNests) {
        weights[oldLog][dist][distStart][oldNest][maxNests][actionIndex] = alpha * (r + weights[newLog][newDist][newDistStart][newNest][newMaxNests][actionIndex1] - weights[oldLog][dist][distStart][oldNest][maxNests][actionIndex]);
    }
}
