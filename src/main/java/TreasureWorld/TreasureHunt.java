package TreasureWorld;

import Textures.GeneralTexturesBridge;
import Textures.GeneralTexturesMap;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import utils.Pair;

import java.util.ArrayList;
import java.util.Random;

public class TreasureHunt implements ApplicationListener {

    private TreasureHubert hubert;
    private Map map;
    private Thread thread;
    private SpriteBatch batch;
    private Tabular weights;
    private Random random;

    @Override
    public void create() {
        batch = new SpriteBatch();
        map = new Map(7);
        weights = new Tabular(0.01, 5);
        hubert = new TreasureHubert(0, 0, map, weights, 5);
        thread = new Thread(this::nStep);
        thread.start();
        random = new Random();

    }

    public void simulateDynaQ(){
        int fov = 62;
        int[][][][][][] model = new int[fov][fov][fov][fov][6][];
        ArrayList<int[]> visitedStateActions = new ArrayList<>();
        double wAvgStep = 0;
        int dynaN = 50;
        for (int i = 0; i < 10000000; i++) {
            int step = 0;
            while (!hubert.finished()){
                // Do action
                int[] values = hubert.step();
                map.step();
                //Add state to model
                if(model[values[2]][values[1]][values[4]][values[3]][values[5]] == null){
                    visitedStateActions.add(new int[]{values[1], values[2], values[3], values[4], values[5]});
                }

                // Update Q
                weights.update(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10]);

                //Update model
                model[values[2]][values[1]][values[4]][values[3]][values[5]] = new int[]{values[0], values[6], values[7], values[8], values[9]};

                // Update Q based on model
                for (int j = 0; j < dynaN; j++) {
                    dynaQUpdate(visitedStateActions, model);
                }
                if(i == 100000){
                    hubert.setEpsilon(0.1);
//                    try {
//                        Thread.sleep(15);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }

                step++;
            }


            wAvgStep += 0.001 * (hubert.getReward()-wAvgStep);
            if (i % 1000 == 0){
                System.out.println("Ep: " + i + " Weighted avg(0.001): " + wAvgStep);
            }
            map = new Map(7);
            hubert.reset(map);
        }

    }

    private void dynaQUpdate(ArrayList<int[]> visitedStateActions, int[][][][][][] model) {
        int[] randomActionState = visitedStateActions.get(random.nextInt(visitedStateActions.size()));
        int[] rewardState = model[randomActionState[1]][randomActionState[0]][randomActionState[3]][randomActionState[2]][randomActionState[4]];
        int actionPrime = weights.getBestAction(rewardState[1], rewardState[2], rewardState[3], rewardState[4]).getActionIndex();
        weights.update(rewardState[0], randomActionState[0], randomActionState[1], randomActionState[2],
                randomActionState[3], randomActionState[4], rewardState[1], rewardState[2], rewardState[3], rewardState[4], actionPrime);
    }

    public void nStep(){
        int n = 10;
        double wAvgStep = 0;
        for (int i = 0; i < 10000000; i++) {
            int step = 0;
            ArrayList<int[]> nValues = new ArrayList<>();
            while (!hubert.finished() && step < 5000){
                int[] values = hubert.step();
                nValues.add(values);
                if(nValues.size() == n){
                    updateNStep(nValues);
                }
                map.step();
                if(i % 100000 == 0 && i != 0){
                    hubert.setEpsilon(0.1);
                    try {
                        Thread.sleep(55);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                step++;

            }
            while (nValues.size() > 0){
                updateNStep(nValues);
            }
            wAvgStep += 0.001 * (hubert.getReward()-wAvgStep);
            if (i % 1000 == 0){
                System.out.println("Ep: " + i + " Weighted avg(0.001): " + wAvgStep);
            }
            map = new Map(7);
            hubert.reset(map);


        }
    }

    private void updateNStep(ArrayList<int[]> nValues) {
        double g = 0;
        for (int i = 0; i < nValues.size(); i++) {
            g += nValues.get(i)[0]*Math.pow(0.9, i);
        }
        int[] old = nValues.get(0);
        int[] prime = nValues.get(nValues.size()-1);
        weights.update(g, old[1], old[2], old[3], old[4], old[5], prime[6], prime[7], prime[8], prime[9], prime[10]);
        nValues.remove(0);
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
                if(map.getLoc(j, i).isJewel()){
                    batch.draw(GeneralTexturesMap.JEWEL, j*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2, 32, 32);
                }
                if (map.getLoc(j, i).isChemical()){
                    batch.draw(GeneralTexturesMap.PLANTCHEM,  j*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2, 32, 32);
                }
                if (map.getLoc(j, i).isWall()){
                    batch.draw(GeneralTexturesMap.STONEWALL,  j*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2, 32, 32);
                }
            }
            batch.draw(GeneralTexturesMap.TOP[i%3], i*32+(800-32 * map.getSize())/2, map.getSize()*32+(480-32 * map.getSize())/2);
            batch.draw(GeneralTexturesMap.BOT[i%3], i*32+(800-32 * map.getSize())/2, -1*32+(480-32 * map.getSize())/2);
            batch.draw(GeneralTexturesMap.RIGHT[i%3], map.getSize()*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2);
            batch.draw(GeneralTexturesMap.LEFT[i%3], -1*32+(800-32 * map.getSize())/2, i*32+(480-32 * map.getSize())/2);
        }
        for (Robot robot: map.getRobots()){
            batch.draw(GeneralTexturesMap.ROBOT, robot.getX()*32+(800-32 * map.getSize())/2, robot.getY()*32+(480-32 * map.getSize())/2, 32, 32);
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
