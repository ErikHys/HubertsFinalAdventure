package TreasureWorld;

import java.awt.*;
import java.util.Random;

public class Map {

    private final int jX;
    private final int jY;
    private final int size;
    private final Robot[] robots;
    private final Random random;
    TileLocation[][] grid;

    public Map(int size){
//        int[] seeds = new int[]{22, 29, 31, 45, 67, 97};
//        Random random = new Random(seeds[new Random().nextInt(seeds.length)]);
        random = new Random();
        this.size = size;
        grid = new TileLocation[size][size];
        jX = random.nextInt(size);
        jY = random.nextInt(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == jY && j == jX){
                    grid[i][j] = new TileLocation(new Jewel());
                }else {

                    grid[i][j] = new TileLocation(null);
                    if (random.nextDouble() < 0.01){
                        grid[i][j].chemical();
                    }
                    else if (random.nextDouble() < 0.01){
                        grid[i][j].wall();
                    }
                    else if (random.nextDouble() < 0.01){
                        grid[i][j].garbage();
                    }
                }

            }
        }
        robots = new Robot[1];
        for (int i = 0; i < robots.length; i++) {
            robots[i] = new Robot(random.nextInt(size/2) + 2, random.nextInt(size/2) + 2, size, this);
        }
        grid[size-1][0].bin();
        grid[size-1][1].safe();
    }


    public TileLocation getLoc(int x, int y){
        return grid[y][x];
    }

    public int getjX() {
        return jX;
    }

    public int getjY() {
        return jY;
    }

    public int getSize() {
        return size;
    }

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
//                    if (grid[i + y][j + x].getItem() != null && grid[i + y][j + x].getItem().getClass() == Wall.class){
//                        if (i1 < (minX == Integer.MAX_VALUE ? minX : Math.abs(minX) + Math.abs(minY))) {
//                            minX = j;
//                            minY = i;
//                            minIdx = idx;
//                        }
//                    }


                }idx++;
            }
        }
        return minIdx;
    }
    public void step() {
        for (Robot robot : robots){
            robot.step();
        }
    }

    public Robot[] getRobots() {
        return robots;
    }


    public void spawn(int x, int y) {
        if(random.nextDouble() < 0.005){
            grid[y][x].garbage();
        }
    }
}
