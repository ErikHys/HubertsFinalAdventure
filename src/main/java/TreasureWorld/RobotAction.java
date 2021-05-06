package TreasureWorld;

import java.util.List;
import java.util.Random;

public enum RobotAction {
    LEFT (0, -1, 0),
    RIGHT (0, 1, 1),
    UP (-1, 0, 2),
    DOWN (1, 0, 3);


    private final int actionIndex;
    private final int x;
    private final int y;
    private static final List<RobotAction> VALUES = List.of(values());
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
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    RobotAction(int i, int j, int k) {
        x = i;
        y = j;
        actionIndex = k;
    }

    public static RobotAction getRandomAction(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static RobotAction getActionByIndex(int i){
        switch (i){
            case 0 -> {
                return LEFT;
            }
            case 1 -> {
                return RIGHT;
            }
            case 2 -> {
                return UP;
            }
            case 3 -> {
                return DOWN;
            }
        }
        return getRandomAction();
    }
}
