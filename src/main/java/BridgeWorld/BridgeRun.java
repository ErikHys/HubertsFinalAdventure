package BridgeWorld;

import BridgeWorld.Weights.IWeights;
import BridgeWorld.Weights.Tabular;
import Textures.GeneralTexturesBridge;
import Textures.LoadingBarTextures;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Random;


/**
 * The main class for challenge 1.
 * Connects bridge, Hubert, Weights and rendering together
 */
public class BridgeRun implements ApplicationListener {

    private Random random;
    private Bridge bridge;
    private Thread thread;
    private int ep;
    private SpriteBatch batch;
    private int bridgeLength;
    private BridgeHubert bridgeHubert;
    private IWeights weights;

    /**
     * Constructor from libGDX
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        random = new Random();
        bridgeLength = 25;
        bridge = new Bridge(bridgeLength);
        weights = new Tabular(20, 0.001);
        bridgeHubert = new BridgeHubert(20, bridge, weights, 1.2);
        thread = new Thread(this::nStep);
        thread.start();
        ep = 0;
    }


    /**
     * Basic TD learning
     */
    public void simulateTD(){
        double wAvg = 0;
        for (ep = 0; ep < 10000000; ep++) {
            while (!bridgeHubert.finished()){
                int[] values = bridgeHubert.step();
                weights.update(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7],
                        values[8], values[9], values[10], values[11], values[12], values[13], values[14]);
                if((ep+1) % 500000 == 0) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            wAvg = getwAvg(wAvg);

        }


    }

    /**
     * Weighted average function
     * @param wAvg
     * @return
     */
    private double getwAvg(double wAvg) {
        wAvg += (bridgeHubert.getReward() - wAvg)*0.0001;
        if(ep % 10000 == 0) System.out.println("Ep: " + ep + " weighted avg: " + wAvg);
        reset();
        return wAvg;
    }

    /**
     * Make a new bridge to Hubert, and every 100000 episode increase the size of said bridge
     */
    private void reset(){

        if ((ep+1) % 100000 == 0){
            bridgeLength += 2;
            bridgeHubert.setEpsilon(0.05);
            System.out.println("Increasing difficulty");
        }
        bridge = new Bridge(bridgeLength);
        bridgeHubert.reset(bridge);
    }

    /**
     * nStep Q learning
     */
    public void nStep(){
        // Converges faster, but Hubert still likes to dance at the middle, if I give him minus reward for each step he doesn't converge.
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
                //Uncomment this you want slower render te see what is going on.
                if((ep+1) % 100000 == 0) {
                    try {
                        Thread.sleep(50);
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

    /**
     * nStep for the final task, where Hubert just tries to maintain the bridge for as long as possible
     */
    public void nStepCont(){
        int n = 15;
        double wAvg = 0;
        double wAvgSteps = 0;
        for (ep = 0; ep < 10000000; ep++) {
            ArrayList<int[]> nValues = new ArrayList<>();
            int step = 1;
            while (!bridgeHubert.finishedCont()){
                int[] values = bridgeHubert.step();
                nValues.add(values);
                if (nValues.size() == n){
                    updateNStep(nValues);
                }
                bridge.step(bridgeHubert.getLoc());
                step++;
                if((ep+1) % 100000 == 0) {
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
            wAvg += ((bridgeHubert.getReward())/(double)step - wAvg) * 0.0001;
            wAvgSteps += (step - wAvgSteps) * 0.0001;
            if((ep+1)% 400 == 0)System.out.println(wAvg + " " + wAvgSteps);
            reset();

        }
    }

    /**
     * Update weights according to nStep  algorithm
     * @param nValues ArrayList of n StateActions
     */
    private void updateNStep(ArrayList<int[]> nValues) {
        double g = 0;
        for (int i = 0; i < nValues.size(); i++) {
            double gi = nValues.get(i)[0]*Math.pow(0.7, i);
            g += gi;
        }
        int[] old = nValues.get(0);
        int[] prime = nValues.get(nValues.size()-1);
        weights.update(g, old[1], old[2], old[3], prime[4], prime[5], prime[6], old[7],
                prime[8], old[9], prime[10], old[11], prime[12], old[13], prime[14]);
        nValues.remove(0);
    }


    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (Texture bgLayer: GeneralTexturesBridge.BGS){
            batch.draw(bgLayer, 0, 0, 800, 480);
        }
        // Set this to true if you don't want to see loading bar
        if((ep+1) % 500000 == 0){
            renderBridge();
            renderHubert();
        }else {
            batch.draw(LoadingBarTextures.bars[((ep/(500000/8)) % 8)], 400-243, 220);
        }
        batch.end();
    }

    /**
     * Renders Hubert and the information about him, charge, if he has a log, and if he has fixed enough nests
     */
    private void renderHubert() {
        TextureRegion currentDirHubert = bridgeHubert.isFacingRight() ? GeneralTexturesBridge.HUBERT_RIGHT : GeneralTexturesBridge.HUBERT_LEFT;
        batch.draw(currentDirHubert, bridgeHubert.getLoc()*32, 240);
        if(bridgeHubert.isHasLog()) batch.draw(GeneralTexturesBridge.BRIDGE_WORKING, 32, 370, 16, 48);
        if(bridgeHubert.getFixedNests() < 2)batch.draw(GeneralTexturesBridge.NEST, 96, 370, 30, 16);
        for (int i = 0; i < bridgeHubert.getCharge(); i++) {
            batch.draw(GeneralTexturesBridge.POWER, 32 + 32*i, 180, 32, 32);
        }
    }

    /**
     * Renders the bridge
     */
    private void renderBridge() {
        for (int i = 0; i < bridge.getLength(); i++) {
            if(bridge.getLoc(i).isWorking()){
                batch.draw(GeneralTexturesBridge.BRIDGE_WORKING, 32*i, 210);
            }else {
                batch.draw(GeneralTexturesBridge.BRIDGE_HOLE, 32*i, 271);
            }
            if(!bridge.getLoc(i).isNestFixed()){
                batch.draw(GeneralTexturesBridge.NEST, 32*i, 300, 37, 20);
            }
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     * On close
     */
    @Override
    public void dispose() {
        thread.interrupt();
    }
}
