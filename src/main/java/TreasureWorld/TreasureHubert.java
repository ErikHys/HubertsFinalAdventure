package TreasureWorld;


import java.util.ArrayList;
import java.util.Random;

public class TreasureHubert {

    private final double epsilon;
    private final Map map;
    private int x;
    private int y;
    private Random random;
    private ArrayList<Items> bag;

    public TreasureHubert(int startX, int startY, Map map){
        x = startX;
        y = startY;
        random = new Random();
        epsilon = 1.1;
        this.map = map;
        bag = new ArrayList<>();
    }

    private int[] move(Action action){
        switch (action){
            case PICKUP -> pickup();
            case DROP -> drop();
            case RIGHT, LEFT, DOWN, UP -> changePos(action);
        }
        return null;
    }
    
    private Action epsilonGreedy(){
        double p = random.nextDouble();
        if (p < epsilon){
            return Action.getRandomAction();
        }
        return Action.getRandomAction();
    }

    public int[] step(){
        Action action = epsilonGreedy();
        return move(action);
    }
    private void changePos(Action action) {
        if(action.getX() + x < map.getSize() && action.getX() + x >= 0 && action.getY() + y < map.getSize() && action.getY() + y >= 0){
            x += action.getX();
            y += action.getY();
        }
    }

    private void drop() {
    }

    private void pickup() {
        Items items = map.getLoc(x, y).pickupItem();
        if(items != null){
            bag.add(items);
        }

    }

    public boolean finished() {
        for (Items items: bag){
            if(items.getClass() == Jewel.class){
                return true;
        }

        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
