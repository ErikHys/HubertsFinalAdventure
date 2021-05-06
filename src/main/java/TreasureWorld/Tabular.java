package TreasureWorld;

import java.util.ArrayList;
import java.util.Random;

public class Tabular {

    private final double alpha;
    private final Random random;
    private double[][][][][] weights;

    public Tabular(double alpha, int fov){
        this.alpha = alpha;
        weights = new double[61+1][61+1][61+1][61+1][6];
        random = new Random();
    }

    public Action getBestAction(int jewel, int chemical, int wall,int robot){
        ArrayList<Integer> maxIndices = new ArrayList<>();
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        for (int i = 0; i < 6; i++) {
            if(weights[jewel][chemical][wall][robot][i] > max){
                maxIndices = new ArrayList<>();
                max = weights[jewel][chemical][wall][robot][i];
                maxIdx = i;
                maxIndices.add(maxIdx);
            }else if (weights[jewel][chemical][wall][robot][i] == max){
                maxIndices.add(i);
            }
        }
        return Action.getActionByIndex(maxIndices.get(random.nextInt(maxIndices.size())));
    }

    public void update(double r, int jewel, int chemical, int wall,int robot, int actionIdx, int newJewel
            , int newChemical, int newWall,int newRobot, int newActionIdx){
        weights[jewel][chemical][wall][robot][actionIdx] += alpha * (r + weights[newJewel][newChemical][newWall][newRobot][newActionIdx] - weights[jewel][chemical][wall][robot][actionIdx]);
        if(r == 1){
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
