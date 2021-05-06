package TreasureWorld;

public class Robot {

    private final Map map;
    private int x;
    private int y;
    private final int size;
    private int hX, hY;

    public Robot(int x, int y, int size, Map map) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.map = map;
    }

    public void step(){
        RobotAction action = RobotAction.getRandomAction();
        move(action);
        map.spawn(x, y);
    }

    private void move(RobotAction action) {
        if(action.getX() + x < size && action.getX() + x >= 0 && action.getY() + y < size
                && action.getY() + y >= 0 && (action.getX() + x != hX || action.getY() + y != hY)
                && !map.getLoc(action.getX() + x, action.getY() + y).isWall()){
            x += action.getX();
            y += action.getY();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setHubertXY(int x, int y) {
        hX = x;
        hY = y;
    }
}
