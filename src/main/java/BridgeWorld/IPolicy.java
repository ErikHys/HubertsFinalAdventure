package BridgeWorld;

import utils.Pair;

public interface IPolicy {
    void updateWeights(Pair<Integer, Action> a, int g);
}
