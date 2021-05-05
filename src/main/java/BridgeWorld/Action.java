package BridgeWorld;


import java.util.List;
import java.util.Random;

public enum Action {
    LEFT (-1, 0),
    RIGHT (1, 1),
    PICKUP (0, 2),
    FIX (0, 3),
    CHARGE (0, 3);


    private final int actionIndex;
    private int actionNumber;
    private static final List<Action> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     *
     * @return returns a number for indexing
     */
    public int getActionIndex() {
        return actionIndex;
    }

    /**
     *
     * @return Returns a number for movement
     */
    public int getActionNumber() {
        return actionNumber;
    }

    Action(int i, int j) {
        actionNumber = i;
        actionIndex = j;
    }

    public static Action getRandomAction(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static Action getActionByIndex(int i){
        switch (i){
            case 0 -> {
                return LEFT;
            }
            case 1 -> {
                return RIGHT;
            }
            case 2 -> {
                return PICKUP;
            }
            case 3 -> {
                return FIX;
            }
            case 4 -> {
                return CHARGE;
            }
        }
        return getRandomAction();
    }
}
