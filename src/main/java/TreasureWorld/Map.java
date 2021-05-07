package TreasureWorld;


import java.util.Random;

/**
 * A map that Hubert can traverse and interact with
 */
public class Map {

    private final int jX;
    private final int jY;
    private final int size;
    private final Robot[] robots;
    private final Random random;
    TileLocation[][] grid;

    /**
     *
     * @param size the size of the map, map = sizs*size
     */
    public Map(int size){
        random = new Random();
        this.size = size;
        grid = new TileLocation[size][size];
        jX = random.nextInt(size/2) + 2;
        jY = random.nextInt(size/2) + 2;
        //Fill the map with random items
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == jY && j == jX){
                    grid[i][j] = new TileLocation(new Jewel());
//                    grid[i][j].chemical();
                }else {
                    grid[i][j] = new TileLocation(null);
                    if(i > 0 && i < size-1 && j > 0 && j < size-1)
                        if (random.nextDouble() < 0.02){
                            grid[i][j].chemical();
                        }
                        else if (random.nextDouble() < 0.02){
                            grid[i][j].wall();
                        }
                        else if (random.nextDouble() < 0.008){
                            grid[i][j].garbage();
                        }
                        else if (random.nextDouble() < 0.02){
                            grid[i][j].treasure();
                        }
                }

            }
        }
        robots = new Robot[2];
        for (int i = 0; i < robots.length; i++) {
            robots[i] = new Robot(random.nextInt(size/2) + 2, random.nextInt(size/2) + 2, size, this);
        }
        grid[size-1][0].bin();
    }


    /**
     * Get a specific location from the map.
     * if(map.getLoc(x, y).isJewel()) -> end episode
     * @param x
     * @param y
     * @return The specific location of x, y
     */
    public TileLocation getLoc(int x, int y){
        return grid[y][x];
    }

    public int getSize() {
        return size;
    }

    /**
     * Get the closest location of a specific type, returns a index based on that. A Jewel three cells below (x,y) gives index 0
     * If you don't see one index 25...
     * @param x
     * @param y
     * @param fov
     * @param t
     * @param <T>
     * @return
     */
    public <T> int getTypeLoc(int x, int y, int fov, Class<T> t) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int idx = 0;
        int minIdx = 25;
        for (int i = -fov; i <= fov; i++) {
            for (int j = -fov; j <= fov; j++) {
                int i1 = Math.abs(j) + Math.abs(i);
                if(i1 > fov)continue;
                if (j + x >= 0 && j + x < size && i+y >= 0 && i+y < size) {
                    if (grid[i + y][j + x].getItem() != null && grid[i + y][j + x].getItem().getClass() == t){
                        if (i1 < (minX == Integer.MAX_VALUE ? minX : Math.abs(minX) + Math.abs(minY))) {
                            minX = j;
                            minY = i;
                            minIdx = idx;
                        }
                    }

                }idx++;
            }
        }
        return minIdx;
    }

    /**
     * Same as {@link Map#getTypeLoc(int, int, int, Class)} but for robots
     * @param x
     * @param y
     * @param fov
     * @return
     */
    public int getRobotDist(int x, int y, int fov) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int idx = 0;
        int minIdx = 25;
        for (int i = -fov; i <= fov; i++) {
            for (int j = -fov; j <= fov; j++) {
                int i1 = Math.abs(j) + Math.abs(i);
                if(i1 > fov)continue;
                if (j + x >= 0 && j + x < size && i+y >= 0 && i+y < size){
                    for (Robot robot: robots){
                        if(j + x == robot.getX() && i + y == robot.getY()) {
                            if (i1 < (minX == Integer.MAX_VALUE ? minX : Math.abs(minX) + Math.abs(minY))) {
                                minX = j;
                                minY = i;
                                minIdx = idx;
                            }
                        }
                    }
                }idx++;
            }
        }
        return minIdx;
    }

    /**
     * All random robots on the map does a step
     */
    public void step() {
        for (Robot robot : robots){
            robot.step();
        }
    }

    public Robot[] getRobots() {
        return robots;
    }


    /**
     * Spawn som garbage
     * @param x
     * @param y
     */
    public void spawn(int x, int y) {
        if(random.nextDouble() < 0.0005){
            grid[y][x].garbage();
        }
    }

    public int getBinSafeDist(int x, int y, int fov) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int idx = 0;
        int minIdx = 25;
        for (int i = -fov; i <= fov; i++) {
            for (int j = -fov; j <= fov; j++) {
                int i1 = Math.abs(j) + Math.abs(i);
                if(i1 > fov)continue;
                if (j + x >= 0 && j + x < size && i+y >= 0 && i+y < size) {
                    if (grid[i + y][j + x].getItem() != null && grid[i + y][j + x].isHasBin()){
                        if (i1 < (minX == Integer.MAX_VALUE ? minX : Math.abs(minX) + Math.abs(minY))) {
                            minX = j;
                            minY = i;
                            minIdx = idx;
                        }
                    }

                }idx++;
            }
        }
        return minIdx;
    }
}
