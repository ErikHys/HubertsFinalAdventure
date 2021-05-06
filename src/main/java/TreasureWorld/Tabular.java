package TreasureWorld;

import java.util.ArrayList;
import java.util.Random;

public class Tabular {

    private final double alpha;
    private final Random random;
    private double[][][][][][] weights;

    public Tabular(double alpha, int fov){
        this.alpha = alpha;
        weights = new double[fov][fov][fov][fov][fov][6];
        random = new Random();
    }

    public Action getBestAction(int jewel, int chemical, int wall,int robot, int garbage){
        ArrayList<Integer> maxIndices = new ArrayList<>();
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        for (int i = 0; i < 6; i++) {
            if(weights[jewel][chemical][wall][robot][garbage][i] > max){
                maxIndices = new ArrayList<>();
                max = weights[jewel][chemical][wall][robot][garbage][i];
                maxIdx = i;
                maxIndices.add(maxIdx);
            }else if (weights[jewel][chemical][wall][robot][garbage][i] == max){
                maxIndices.add(i);
            }
        }
        return Action.getActionByIndex(maxIndices.get(random.nextInt(maxIndices.size())));
    }

    public void update(double r, int jewel, int chemical, int wall,int robot, int garbage, int actionIdx, int newJewel
            , int newChemical, int newWall,int newRobot, int newGarbage, int newActionIdx){
        weights[jewel][chemical][wall][robot][garbage][actionIdx] += alpha * (r + weights[newJewel][newChemical][newWall][newRobot][newGarbage][newActionIdx] - weights[jewel][chemical][wall][robot][garbage][actionIdx]);
    }
}
