package TreasureWorld;

import java.util.Random;

public class Map {

    private final int jX;
    private final int jY;
    private final int size;
    TileLocation[][] grid;

    public Map(int size){
        Random random = new Random();
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
                }

            }
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
}
