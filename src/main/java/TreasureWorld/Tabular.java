package TreasureWorld;

import java.util.ArrayList;
import java.util.Random;

/**
 * Similar to {@link BridgeWorld.Weights.Tabular} but for the second challenge
 */
public class Tabular {

    private final double alpha;
    private final Random random;
    private double[][][][][][][] weights;

    public Tabular(double alpha, int fov){
        this.alpha = alpha;
        weights = new double[fov][fov][fov][fov][fov][2][6];
        random = new Random();
    }

    public Action getBestAction(int jewel, int chemical, int wall,int robot, int garbage, int hasTreasureGarbage){
        ArrayList<Integer> maxIndices = new ArrayList<>();
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        for (int i = 0; i < 6; i++) {
            if(weights[jewel][chemical][wall][robot][garbage][hasTreasureGarbage][i] > max){
                maxIndices = new ArrayList<>();
                max = weights[jewel][chemical][wall][robot][garbage][hasTreasureGarbage][i];
                maxIdx = i;
                maxIndices.add(maxIdx);
            }else if (weights[jewel][chemical][wall][robot][garbage][hasTreasureGarbage][i] == max){
                maxIndices.add(i);
            }
        }
        return Action.getActionByIndex(maxIndices.get(random.nextInt(maxIndices.size())));
    }

    public void update(double r, int jewel, int chemical, int wall,int robot, int garbage, int hasTreasureGarbage, int actionIdx, int newJewel
            , int newChemical, int newWall,int newRobot, int newGarbage, int newHasTreasureGarbage, int newActionIdx){
        weights[jewel][chemical][wall][robot][garbage][hasTreasureGarbage][actionIdx] += alpha * (r + weights[newJewel][newChemical][newWall][newRobot][newGarbage][newHasTreasureGarbage][newActionIdx] - weights[jewel][chemical][wall][robot][garbage][hasTreasureGarbage][actionIdx]);
    }
}
