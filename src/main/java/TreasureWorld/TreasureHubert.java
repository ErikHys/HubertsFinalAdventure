package TreasureWorld;

import java.util.Random;

/**
 * A Hubert that hunts for jewels, treasure and throws away garbage
 */
public class TreasureHubert {

    private double epsilon;
    private final int fov;
    private Map map;
    private final Tabular weights;
    private int x;
    private int y;
    private Random random;
    private int reward;
    private boolean isDead;
    private int jewel;
    private int garbageCnt;
    private int treasureCnt;

    /**
     *
     * @param startX
     * @param startY
     * @param map
     * @param weights Weights for decision making
     * @param fov How many steps ahead can Hubert see
     */

    public TreasureHubert(int startX, int startY, Map map, Tabular weights, int fov){
        x = startX;
        y = startY;
        random = new Random();
        epsilon = 0.2;
        this.map = map;
        this.weights = weights;
        this.fov = fov;
    }

    /**
     * Do a action
     * @param action
     * @return The reward of that action
     */
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

    /**
     * Find a epsilon greedy action
     * See {@link Map#getTypeLoc(int, int, int, Class)} for info on indexes
     * @param jewelDist Index of closest jewel
     * @param chemDist index of closest chemical
     * @param binDist index of closest bin
     * @param robotDist index of closest robot
     * @param garbageDist index of closest garbage or treasure
     * @param hasTreasureGarbage 1 or 0 based on if Hubert has any garbage or treasure
     * @return the epsilon greedy action to take
     */
    private Action epsilonGreedy(int jewelDist, int chemDist, int binDist, int robotDist, int garbageDist, int hasTreasureGarbage){
        double p = random.nextDouble();
        if (p < epsilon){
            return Action.getRandomAction();
        }
        return weights.getBestAction(jewelDist, chemDist, binDist, robotDist, garbageDist, hasTreasureGarbage);
    }

    /**
     * Do a time step
     * @return reward, old state, old action, new state, new action
     */
    public int[] step(){
        int jewelDirDist = map.getTypeLoc(x, y, fov, Jewel.class);
        int chemDist = map.getTypeLoc(x, y, fov, Obstacle.class);
        int binDist = map.getBinSafeDist(x, y, fov);
        int robotDist = map.getRobotDist(x, y, fov);
        int garbageDist = map.getTypeLoc(x, y, fov, GoodItems.class);
        int hasTreasureGarbage = treasureCnt+garbageDist > 0 ? 1 : 0;
        Action action = epsilonGreedy(jewelDirDist, chemDist, binDist, robotDist, garbageDist, hasTreasureGarbage);
        int r = move(action);
        int newJewelDirDist = map.getTypeLoc(x, y, fov, Jewel.class);
        int newChemDist = map.getTypeLoc(x, y, fov, Obstacle.class);
        int newBinDist = 0;
        int newRobotDist = map.getRobotDist(x, y, fov);
        int newGarbageDist = map.getTypeLoc(x, y, fov, GoodItems.class);
        int newHasTreasureGarbage = treasureCnt+garbageDist > 0 ? 1 : 0;
        Action newAction = epsilonGreedy(newJewelDirDist, newChemDist, newBinDist, newRobotDist, newGarbageDist, newHasTreasureGarbage);
        return new int[]{r, jewelDirDist, chemDist, binDist, robotDist, garbageDist, hasTreasureGarbage, action.getActionIndex(), newJewelDirDist, newChemDist, newBinDist, newRobotDist, newGarbageDist, newHasTreasureGarbage, newAction.getActionIndex()};
    }

    /**
     * Change position based on a action
     * @param action
     */
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
                reward -= 100;
                isDead = true;
            }
        }
    }

    /**
     * Drop an item if Hubert is at a bin
     */
    private void drop() {
        if(garbageCnt > 0 && map.getLoc(x, y).isHasBin()){
            reward += garbageCnt*5;
            reward += treasureCnt*5;
            garbageCnt = 0;
            treasureCnt = 0;

        }
    }

    /**
     * Pick up an item
     */
    private void pickup() {

        if(map.getLoc(x, y).isJewel() || map.getLoc(x, y).isGarbage() || map.getLoc(x, y).isTreasure()){
            if(map.getLoc(x, y).isJewel()){
                reward += 50;
                jewel = 1;
            }
            else if(map.getLoc(x, y).isGarbage() && garbageCnt < 10) {
                garbageCnt++;
                map.getLoc(x,y).pickupItem();
            }
            else if(map.getLoc(x, y).isTreasure() && treasureCnt < 10){
                treasureCnt++;

                map.getLoc(x,y).pickupItem();
            }
            reward += 5;

        }

    }

    /**
     * Check if Hubert is dead or has found the jewel
     * @return true if episode is done
     */
    public boolean finished() {
        if(jewel == 1){
            return true;
        }
        return isDead;
    }

    /**
     * Check if Hubert has run into chemicals
     * @return true if Hubert has run into chemicals
     */
    public boolean finishedCont(){
        return isDead;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Reset Hubert to get him ready for a new episode
     * @param map The new map
     */
    public void reset(Map map){
        this.map = map;
        reward = 0;
        isDead = false;
        jewel = 0;
        garbageCnt = 0;
        treasureCnt = 0;
    }

    public int getReward() {
        return reward;
    }

    public void setEpsilon(double e) {
        epsilon = e;
    }

    public int getGarbageCnt() {
        return garbageCnt;
    }

    public int getTreasureCnt() {
        return treasureCnt;
    }
}
