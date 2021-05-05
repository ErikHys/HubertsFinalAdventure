package TreasureWorld;

import Textures.GeneralTexturesBridge;
import Textures.GeneralTexturesMap;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class TreasureHunt implements ApplicationListener {

    private TreasureHubert hubert;
    private Map map;
    private Thread thread;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        map = new Map(10);
        hubert = new TreasureHubert(0, 0, map);
        thread = new Thread(this::simulate);
        thread.start();

    }

    public void simulate(){
        while (!hubert.finished()){
            hubert.step();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        renderMap();
        renderHubert();
        batch.end();
    }

    private void renderHubert() {
        batch.draw(GeneralTexturesBridge.HUBERT_LEFT, hubert.getX()*32+(800-32 * map.getSize())/2, hubert.getY()*32+(480-32 * map.getSize())/2);
    }

    private void renderMap() {
        batch.draw(GeneralTexturesMap.CORNERS[0], map.getSize()*32+(800-32 * map.getSize())/2, map.getSize()*32+(480-32 * map.getSize())/2);
        batch.draw(GeneralTexturesMap.CORNERS[1], map.getSize()*32+(800-32 * map.getSize())/2, -1*32+(480-32 * map.getSize())/2);
        batch.draw(GeneralTexturesMap.CORNERS[2], -1*32+(800-32 * map.getSize())/2, map.getSize()*32+(480-32 * map.getSize())/2);
        batch.draw(GeneralTexturesMap.CORNERS[3], -1*32+(800-32 * map.getSize())/2, -1*32+(480-32 * map.getSize())/2);
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                batch.draw(GeneralTexturesMap.GROUNDTILES[map.getLoc(j ,i).getTextureInt()], j*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2, 32, 32);
                if(map.getLoc(j, i).getItem() != null){
                    batch.draw(GeneralTexturesMap.JEWEL, j*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2, 32, 32);
                }
            }
            batch.draw(GeneralTexturesMap.TOP[i%3], i*32+(800-32 * map.getSize())/2, map.getSize()*32+(480-32 * map.getSize())/2);
            batch.draw(GeneralTexturesMap.BOT[i%3], i*32+(800-32 * map.getSize())/2, -1*32+(480-32 * map.getSize())/2);
            batch.draw(GeneralTexturesMap.RIGHT[i%3], map.getSize()*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2);
            batch.draw(GeneralTexturesMap.LEFT[i%3], -1*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2);

        }


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
