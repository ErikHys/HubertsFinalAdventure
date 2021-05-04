package BridgeWorld;

import BridgeWorld.Weights.IWeights;
import BridgeWorld.Weights.Tabular;
import Textures.GeneralTextures;
import Textures.LoadingBarTextures;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BridgeRun implements ApplicationListener {

    private Random random;
    private Bridge bridge;
    private Thread thread;
    private int ep;
    private SpriteBatch batch;
    private int bridgeLength;
    private BridgeHubert bridgeHubert;
    private IWeights weights;

    @Override
    public void create() {
        batch = new SpriteBatch();
        random = new Random();
        bridgeLength = random.nextInt(5) + 15;
        bridge = new Bridge(bridgeLength);
        weights = new Tabular(6, 0.01);
        bridgeHubert = new BridgeHubert(6, bridge, weights, 0.2);
        thread = new Thread(this::nStep);
        thread.start();
        ep = 0;
    }


    public void simulateTD(){
        double wAvg = 0;
        for (ep = 0; ep < 10000000; ep++) {
            while (!bridgeHubert.finished()){
                int[] values = bridgeHubert.step();
                weights.update(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7],
                        values[8], values[9], values[10], values[11], values[12]);
                if((ep+1) % 500000 == 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            wAvg = getwAvg(wAvg);

        }


    }

    private double getwAvg(double wAvg) {
        wAvg += (bridgeHubert.getReward() - wAvg)*0.001;
        if(ep % 100000 == 0) System.out.println(wAvg);
        bridgeLength = random.nextInt(5) + 15;
        bridge = new Bridge(bridgeLength);
        bridgeHubert.reset(bridge);
        if (ep == 1000000){
            bridgeHubert.setEpsilon(0.05);
        }
        return wAvg;
    }

    //This does not converge at all
    public void simulateMonteCarlo(){
        double wAvg = 0;
        for (ep = 0; ep < 10000000; ep++) {
            ArrayList<int[]> mcValues = new ArrayList<>();
            int limit = 0;
            while (!bridgeHubert.finished() && limit < 1000000) {
                mcValues.add(bridgeHubert.mcStep());
                if((ep+1) % 500000 == 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                limit++;
            }
            Collections.reverse(mcValues);
            int g = mcValues.get(0)[0];
            for (int[] value: mcValues){
//                weights.update(g - value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7], value[8], value[9], value[10]);
            }

            wAvg = getwAvg(wAvg);
        }
    }

    // Converges faster, but Hubert still likes to dance at the middle, if I give him minus reward for each step he doesn't converge.
    // Does not converge with nests
    public void nStep(){
        int n = 5;
        double wAvg = 0;
        for (ep = 0; ep < 10000000; ep++) {
            ArrayList<int[]> nValues = new ArrayList<>();
            while (!bridgeHubert.finished()){
                int[] values = bridgeHubert.step();
                nValues.add(values);
                if (nValues.size() == n){
                    updateNStep(nValues);
                }
                if((ep+1) % 500000 == 0) {
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            while (nValues.size() > 0){
                updateNStep(nValues);
            }

            wAvg = getwAvg(wAvg);

        }
    }

    private void updateNStep(ArrayList<int[]> nValues) {
        double g = 0;
        for (int i = 0; i < nValues.size(); i++) {
            double gi = nValues.get(i)[0]*Math.pow(0.9, i+1);
            g += gi;
        }
        int[] old = nValues.get(0);
        int[] prime = nValues.get(nValues.size()-1);
        weights.update(g, old[1], old[2], old[3], prime[4], prime[5], prime[6], old[7], prime[8], old[9], prime[10], old[11], prime[12]);
        nValues.remove(0);
    }


    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (Texture bgLayer: GeneralTextures.BGS){
            batch.draw(bgLayer, 0, 0, 800, 480);
        }
        if((ep+1) % 500000 == 0){
            renderBridge();
            renderHubert();
        }else {
            batch.draw(LoadingBarTextures.bars[((ep/(500000/8)) % 8)], 400-243, 220);
        }
        batch.end();
    }

    private void renderHubert() {
        TextureRegion currentDirHubert = bridgeHubert.isFacingRight() ? GeneralTextures.HUBERT_RIGHT : GeneralTextures.HUBERT_LEFT;
        batch.draw(currentDirHubert, bridgeHubert.getLoc()*32, 240);
        if(bridgeHubert.isHasLog()) batch.draw(GeneralTextures.BRIDGE_WORKING, 32, 370, 16, 48);
        if(bridgeHubert.getFixedNests() < 2)batch.draw(GeneralTextures.NEST, 96, 370, 30, 16);

    }

    private void renderBridge() {
        for (int i = 0; i < bridgeLength; i++) {
            if(bridge.getLoc(i).isWorking()){
                batch.draw(GeneralTextures.BRIDGE_WORKING, 32*i, 210);
            }else {
                batch.draw(GeneralTextures.BRIDGE_HOLE, 32*i, 271);
            }
            if(!bridge.getLoc(i).isNestFixed()){
                batch.draw(GeneralTextures.NEST, 32*i, 300, 37, 20);
            }
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
