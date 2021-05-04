package BridgeWorld;

import BridgeWorld.Weights.IWeights;

import java.util.Random;

/**
 *
 */
public class BridgeHubert {

    private double epsilon;
    private Bridge bridge;
    private int fov;
    private int loc;
    private int reward;
    private IWeights weights;
    private boolean hasLog;
    private Random random;
    private boolean facingRight;
    private boolean isDead;
    private int charge;
    private int fixedNests;
    private int maxNests;

    public BridgeHubert(int view, Bridge bridge, IWeights weights, double e) {
        fov = view;
        loc = 0;
        hasLog = true;
        this.bridge = bridge;
        epsilon = e;
        random = new Random();
        this.weights = weights;
        isDead = false;
        facingRight = true;
        maxNests = 2;
        fixedNests = 0;
    }

    public int move(Action action){
        int oldReward = reward;
        switch (action){
            case FIX -> fix();
            case LEFT, RIGHT -> changePos(action.getActionNumber());
            case PICKUP -> pickup();
        }
        return reward - oldReward;
    }

    private void pickup() {
        if(bridge.getLoc(loc).isStart() && !hasLog) {
            hasLog = true;
            reward += 5;
        }
        else {
            reward--;
        }
    }

    private void changePos(int actionNumber) {
        if (loc+actionNumber < bridge.getLength() && loc+actionNumber >= 0) {
            if (!bridge.getLoc(loc+actionNumber).isWorking()){
                isDead = true;
                reward -= 1000;

            }else {
                loc += actionNumber;
                facingRight = actionNumber > 0;
                finished();
            }

        }else {
            reward -= 5;
        }

    }

    private void fix() {
        if (!bridge.getLoc(loc+1).isWorking() && hasLog){
            reward += 25;
            bridge.fixLoc(loc+1);
            hasLog = false;

        }else if(!bridge.getLoc(loc).isNestFixed()){
            if(fixedNests < maxNests) reward += 50;
            else reward -= 10;
            bridge.fixNest(loc);
            fixedNests++;
        }else {
            reward--;
        }
    }

    public boolean finished() {
        if(bridge.getLoc(loc).isEnd() || isDead){
            if(!isDead){
                reward += 250;
            }else {
                reward -= 500;
            }
            return true;
        }
        return false;
    }

    public int[] step(){
        return stepUtil();
    }

    private int[] stepUtil(){
        int dist = bridge.getDist(fov, loc);
        int oldLog = hasLog ? 1 : 0;
        int distStart = loc == 0 ? 1 : 0;
        int oldHasNestF = bridge.getLoc(loc).isNestFixed() ? 1 : 0;
        int max = fixedNests < maxNests ? 1 : 0;
        Action action = epsilonGreedy(dist, distStart);
        int  r = move(action);
        int newDist = bridge.getDist(fov, loc);
        int newLog = hasLog ? 1 : 0;
        int newDistStart = loc == 0 ? 1 : 0;
        Action newAction = epsilonGreedy(newDist, distStart);
        int newHasNestF = bridge.getLoc(loc).isNestFixed() ? 1 : 0;
        int newMax = fixedNests < maxNests ? 1 : 0;
        return new int[]{r, dist, oldLog, action.getActionIndex(), newDist, newLog, newAction.getActionIndex(), distStart, newDistStart, oldHasNestF, newHasNestF, max, newMax};

    }

    public int[] mcStep(){
        int[] values = stepUtil();
        values[0] = reward;
        return values;
    }

    private Action epsilonGreedy(int dist, int distStart) {
        double p = random.nextDouble();
        if (p < epsilon){
            return Action.getRandomAction();
        }
        return weights.getBestAction(dist, distStart, hasLog ? 1 : 0, bridge.getLoc(loc).isNestFixed() ? 1 : 0, fixedNests < maxNests ? 1 : 0);
    }



    private int getNextLog(Action action) {

        if (action == Action.PICKUP && bridge.getLoc(loc).isStart()) return 1;
        if (action == Action.FIX && !bridge.getLoc(loc+1).isWorking()) return 0;
        return hasLog ? 1 : 0;
    }

    private int getNextPos(int actionNumber) {
        if (loc + actionNumber < bridge.getLength() && loc + actionNumber >= 0 && bridge.getLoc(loc + actionNumber).isWorking()) {
                return loc + actionNumber;
        }else {
            return 0;
        }
    }



    public void reset(Bridge bridge) {
        loc = 0;
        reward = 0;
        hasLog = true;
        isDead = false;
        fixedNests = 0;
        this.bridge = bridge;
    }

    public int getLoc() {
        return loc;
    }

    public int getReward() {
        return reward;
    }

    public IWeights getWeights() {
        return weights;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public boolean isHasLog() {
        return hasLog;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public int getFixedNests() {
        return fixedNests;
    }


}
