package TreasureWorld;

import java.awt.*;
import java.util.Random;

public class Map {

    private final int jX;
    private final int jY;
    private final int size;
    private final Robot[] robots;
    TileLocation[][] grid;

    public Map(int size){
        Random random = new Random();
        this.size = size;
        grid = new TileLocation[size][size];
        jX = random.nextInt(size/2) + 2;
        jY = random.nextInt(size/2) + 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == jY && j == jX){
                    grid[i][j] = new TileLocation(new Jewel());
                }else {

                    grid[i][j] = new TileLocation(null);
                    if (random.nextDouble() < 0.02){
                        grid[i][j].chemical();
                    }
                    if (random.nextDouble() < 0.02){
                        grid[i][j].wall();
                    }
                }

            }
        }
        robots = new Robot[2];
        for (int i = 0; i < robots.length; i++) {
            robots[i] = new Robot(random.nextInt(size/2) + 2, random.nextInt(size/2) + 2, size, this);
        }
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

    public <T> int[] getJewelDir(int x, int y, int fov, Class<T> t) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (int i = -fov; i <= fov; i++) {
            for (int j = -fov; j <= fov; j++) {
                int i1 = Math.abs(j) + Math.abs(i);
                if(i1 > fov)continue;
                if (j + x >= 0 && j + x < size && i+y >= 0 && i+y < size && grid[i+y][j+x].getItem() != null && grid[i+y][j+x].getItem().getClass() == t){
                    if(i1 < (minX == Integer.MAX_VALUE ? minX : Math.abs(minX) + Math.abs(minY))){
                        minX = j;
                        minY = i;
                    }
                }
            }
        }
        if(minX != Integer.MAX_VALUE)return new int[]{minX+fov, minX+fov};
        return new int[]{2*fov+1, 2*fov+1};
    }

    public int[] getChemWallDir(int x, int y, int fov) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (int i = -fov; i <= fov; i++) {
            for (int j = -fov; j <= fov; j++) {
                int i1 = Math.abs(j) + Math.abs(i);
                if(i1 > fov)continue;
                if (j + x >= 0 && j + x < size && i+y >= 0 && i+y < size && grid[i+y][j+x].getItem() != null && (grid[i+y][j+x].isWall() || grid[i+y][j+x].isChemical())){
                    if(i1 < (minX == Integer.MAX_VALUE ? minX : Math.abs(minX) + Math.abs(minY))){
                        minX = j;
                        minY = i;
                    }
                }
            }
        }
        for (Robot robot : robots){
            if(Math.abs(robot.getX() - x) <= fov && Math.abs(robot.getY() - y) <= fov){
                if(Math.abs(robot.getX() - x) + Math.abs(robot.getY() - y) < (minX == Integer.MAX_VALUE ? minX : Math.abs(minX) + Math.abs(minY))){
                    minX = robot.getX() - x;
                    minY = robot.getY() - y;
                }
            }
        }
        if(minX != Integer.MAX_VALUE)return new int[]{minX+fov, minX+fov};
        return new int[]{2*fov+1, 2*fov+1};
    }

    public void step() {
        for (Robot robot : robots){
            robot.step();
        }
    }

    public Robot[] getRobots() {
        return robots;
    }
}
