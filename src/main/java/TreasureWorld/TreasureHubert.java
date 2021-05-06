package TreasureWorld;


import java.util.ArrayList;
import java.util.Random;

public class TreasureHubert {

    private double epsilon;
    private final int fov;
    private Map map;
    private final Tabular weights;
    private int x;
    private int y;
    private Random random;
    private ArrayList<Items> bag;
    private int reward;
    private int pointReward;
    private boolean isDead;

    public TreasureHubert(int startX, int startY, Map map, Tabular weights, int fov){
        x = startX;
        y = startY;
        random = new Random();
        epsilon = 0.2;
        this.map = map;
        bag = new ArrayList<>();
        this.weights = weights;
        this.fov = fov;
    }

    private int move(Action action){
        int oldR = reward;
        switch (action){
            case PICKUP -> pickup();
            case DROP -> drop();
            case RIGHT, LEFT, DOWN, UP -> changePos(action);
        }
        for (Robot robot: map.getRobots()){
            robot.setHubertXY(x, y);
        }
        return reward - oldR;
    }
    
    private Action epsilonGreedy(int jewelDist, int chemDist, int wallDist, int robotDist){
        double p = random.nextDouble();
        if (p < epsilon){
            return Action.getRandomAction();
        }
        return weights.getBestAction(jewelDist, chemDist, wallDist, robotDist);
    }

    public int[] step(){
        int jewelDirDist = map.getTypeLoc(x, y, fov, Jewel.class);
        int chemDist = map.getTypeLoc(x, y, fov, Chemical.class);
        int wallDist = map.getTypeLoc(x, y, fov, Wall.class);
        int robotDist = map.getRobotDist(x, y, fov);
        Action action = epsilonGreedy(jewelDirDist, chemDist, wallDist, robotDist);
        int r = move(action);
        int newJewelDirDist = map.getTypeLoc(x, y, fov, Jewel.class);
        int newChemDist = map.getTypeLoc(x, y, fov, Chemical.class);
        int newWallDist = map.getTypeLoc(x, y, fov, Wall.class);
        int newRobotDist = map.getRobotDist(x, y, fov);
        Action newAction = epsilonGreedy(newJewelDirDist, newChemDist, newWallDist, newRobotDist);
        return new int[]{r, jewelDirDist, chemDist, wallDist, robotDist, action.getActionIndex(), newJewelDirDist, newChemDist, newWallDist, newRobotDist, newAction.getActionIndex()};
    }
    private void changePos(Action action) {
        if(action.getX() + x < map.getSize() && action.getX() + x >= 0 && action.getY() + y < map.getSize()
                && action.getY() + y >= 0){
            if(map.getLoc(x+action.getX(), y+action.getY()).isWall()){
                reward -= 5;
                return;
            }
            for(Robot robot: map.getRobots()){
                if(x+action.getX() == robot.getX() &&  y+action.getY() == robot.getY()){
                    reward -= 5;
                    return;
                }
            }
            x += action.getX();
            y += action.getY();
            if(map.getLoc(x, y).isChemical()){
                reward -= 5;
                pointReward--;
                isDead = true;
            }
        }
    }

    private void drop() {
    }

    private void pickup() {

        if(map.getLoc(x, y).isJewel()){
            bag.add(map.getLoc(x, y).pickupItem());
            reward += 5;
            pointReward++;
        }

    }

    public boolean finished() {
        for (Items items: bag){
            if(items.getClass() == Jewel.class){
                return true;
        }

        }
        if(isDead)return true;
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void reset(Map map){
        this.map = map;
        bag = new ArrayList<>();
        reward = 0;
        pointReward = 0;
        isDead = false;
    }

    public int getReward() {
        return reward;
    }

    public void setEpsilon(double e) {
        epsilon = e;
    }

    public int getPointReward() {
        return pointReward;
    }
}
