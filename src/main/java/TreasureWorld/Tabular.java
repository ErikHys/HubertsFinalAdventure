package TreasureWorld;

import java.util.ArrayList;
import java.util.Random;

public class Tabular {

    private final double alpha;
    private final Random random;
    private double[][][][][] weights;

    public Tabular(double alpha, int fov){
        this.alpha = alpha;
        weights = new double[fov+1][fov+1][fov+1][fov+1][6];
        random = new Random();
    }

    public Action getBestAction(int jewelX, int jewelY, int chemDistX,int chemDistY){
        ArrayList<Integer> maxIndices = new ArrayList<>();
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        for (int i = 0; i < 6; i++) {
            if(weights[jewelY][jewelX][chemDistY][chemDistX][i] > max){
                maxIndices = new ArrayList<>();
                max = weights[jewelY][jewelX][chemDistY][chemDistX][i];
                maxIdx = i;
                maxIndices.add(maxIdx);
            }else if (weights[jewelY][jewelX][chemDistY][chemDistX][i] == max){
                maxIndices.add(i);
            }
        }
        return Action.getActionByIndex(maxIndices.get(random.nextInt(maxIndices.size())));
    }

    public void update(double r, int jewelX, int jewelY, int chemDistX,int chemDistY, int actionIdx, int newJewelX
            , int newJewelDistY, int newChemDistX,int newChemDistY, int newActionIdx){
        weights[jewelY][jewelX][chemDistY][chemDistX][actionIdx] += alpha * (r + weights[newJewelDistY][newJewelX][newChemDistY][newChemDistX][newActionIdx] - weights[jewelY][jewelX][chemDistY][chemDistX][actionIdx]);
        if(r == 1){
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
