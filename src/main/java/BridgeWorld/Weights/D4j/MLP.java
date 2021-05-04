package BridgeWorld.Weights.D4j;

import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;

public class MLP {

    private final MultiLayerConfiguration conf;
    private final MultiLayerNetwork network;

    public MLP(int in, int out) throws IOException {

        int batchSize = 128; // how many examples to simultaneously train in the network
        EmnistDataSetIterator.Set emnistSet = EmnistDataSetIterator.Set.BALANCED;
        EmnistDataSetIterator emnistTrain = new EmnistDataSetIterator(emnistSet, batchSize, true);
        EmnistDataSetIterator emnistTest = new EmnistDataSetIterator(emnistSet, batchSize, false);
        
        conf = new NeuralNetConfiguration.Builder()
                .seed(27)
                .updater(new Adam())
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(in) // Number of input datapoints.
                        .nOut(1000) // Number of output datapoints.
                        .activation(Activation.RELU) // Activation function.
                        .weightInit(WeightInit.XAVIER) // Weight initialization.
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(1000) // Number of input datapoints.
                        .nOut(1000) // Number of output datapoints.
                        .activation(Activation.RELU) // Activation function.
                        .weightInit(WeightInit.XAVIER) // Weight initialization.
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(1000) // Number of input datapoints.
                        .nOut(1000) // Number of output datapoints.
                        .activation(Activation.RELU) // Activation function.
                        .weightInit(WeightInit.XAVIER) // Weight initialization.
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(1000)
                        .nOut(out)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .build();
        network = new MultiLayerNetwork(conf);
        network.init();
        network.addListeners(new ScoreIterationListener(100));
        for (int i = 0; i < 10; i++) {
            System.out.println("Epoch: " + (i+1) + "/" + 10);
            network.fit(emnistTrain);
        }
        Evaluation eval = network.evaluate(emnistTest);
        System.out.println(eval.accuracy());

    }

    public static void main(String[] args) throws IOException {
        MLP mlp = new MLP(784, 47);
    }
}
