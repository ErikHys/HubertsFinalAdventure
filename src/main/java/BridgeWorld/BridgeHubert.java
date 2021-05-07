package BridgeWorld;

import BridgeWorld.Weights.IWeights;

import java.util.Random;

/**
 * Class for representing Hubert's values and decision making.
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

    /**
     *
     * @param view How far Hubert can see
     * @param bridge The current bridge
     * @param weights The weights class for making decisions
     * @param e epsilon for epsilon greedy
     */
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
        charge = 3;
    }

    /**
     * Hubert does a move
     * @param action the action Hubert should preform
     * @return the reward of that action
     */
    public int move(Action action){
        int oldReward = reward;
        switch (action){
            case FIX -> fix();
            case LEFT, RIGHT -> changePos(action.getActionNumber());
            case PICKUP -> pickup();
            case CHARGE -> chargeHubert();
        }
        return reward - oldReward;
    }

    /**
     * Charges Hubert one charge if at the start.
     */
    private void chargeHubert() {
        if(bridge.getLoc(loc).isStart() && charge < 3){
            charge++;
        }
    }

    /**
     * Picks up a log if he is at the start
     */
    private void pickup() {
        if(bridge.getLoc(loc).isStart() && !hasLog) {
            hasLog = true;
        }
    }

    /**
     * Changes the Hubert's Position, and punishes if Hubert falls off
     * @param actionNumber the change(-1, 1) left/right
     */
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

        }else reward -= 5;

    }

    /**
     * Fixes a hole or nest
     */
    private void fix() {
        if (loc+1 < bridge.getLength() && !bridge.getLoc(loc+1).isWorking() && hasLog && charge > 0){
            reward += 25;
            bridge.fixLoc(loc+1);
            hasLog = false;
            charge--;

        }else if(!bridge.getLoc(loc).isNestFixed() && charge > 0){
            if(fixedNests < maxNests) reward += 50;
            else reward -= 50;
            bridge.fixNest(loc);
            fixedNests++;
            charge--;
        }else reward--;
    }

    /**
     * Checks if Hubert is finished with the run
     * @return true if Hubert has fallen off or reached the end
     */
    public boolean finished() {
        if(bridge.getLoc(loc).isEnd() || isDead){
            if(!isDead){
                reward += 500;
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @return true if Hubert has fallen off
     */
    public boolean finishedCont() {
        return isDead;
    }

    /**
     * Hubert does a time step.
     * He looks around, makes a decision, looks around again.
     * @return Reward, old state, old action, new state, new action
     */
    public int[] step(){
        return stepUtil();
    }

    /**
     * See {@link BridgeHubert#step()}
     * @return
     */
    private int[] stepUtil(){
        int dist = bridge.getDist(fov, loc);
        int oldLog = hasLog ? 1 : 0;
        int distStart = loc == 0 ? 1 : 0;
        int oldHasNestF = bridge.getLoc(loc).isNestFixed() ? 1 : 0;
        int max = fixedNests < maxNests ? 1 : 0;
        int cCharge = charge;
        Action action = epsilonGreedy(dist, distStart);
        int  r = move(action);
        int newDist = bridge.getDist(fov, loc);
        int newLog = hasLog ? 1 : 0;
        int newDistStart = loc == 0 ? 1 : 0;
        Action newAction = epsilonGreedy(newDist, distStart);
        int newHasNestF = bridge.getLoc(loc).isNestFixed() ? 1 : 0;
        int newMax = fixedNests < maxNests ? 1 : 0;
        int newCharge = charge;
        return new int[]{r, dist, oldLog, action.getActionIndex(), newDist, newLog, newAction.getActionIndex(), distStart, newDistStart, oldHasNestF, newHasNestF, max, newMax, cCharge, newCharge};

    }

    /**
     * Does a epsilon greedy move
     * @param dist Distance to closest hole
     * @param distStart (1, 0) if Hubert is at the start
     * @return The epsilon greedy action to take
     */
    private Action epsilonGreedy(int dist, int distStart) {
        double p = random.nextDouble();
        if (p < epsilon){
            return Action.getRandomAction();
        }
        return weights.getBestAction(dist, distStart, hasLog ? 1 : 0, bridge.getLoc(loc).isNestFixed() ? 1 : 0, fixedNests < maxNests ? 1 : 0, charge);
    }


    public void reset(Bridge bridge) {
        loc = 0;
        reward = 0;
        hasLog = true;
        isDead = false;
        fixedNests = 0;
        charge = 3;
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

    public int getCharge() {
        return charge;
    }


}
