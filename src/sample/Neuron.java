package sample;
import java.util.Random;

public class Neuron {
    private double[] weight;
    private double out;
    public static Random random = new Random();
    public double rangeMin = -0.3;
    public double rangeMax = 0.3;

    public Neuron(int weightCount) {
        this.weight = new double[weightCount];
        this.out = 0.0;
        randomizeWeights();
    }

    public double getOut() {
        return out;
    }

    public void correctWeights(double[] deltaWeight) {
        for (int i = 0; i < weight.length; i++) {
            weight[i] += deltaWeight[i];
        }
    }

    public double[] getWeight() {
        return weight;
    }

    public void randomizeWeights() {
        for (int i = 0; i < weight.length; i++) {
            weight[i] = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        }
    }

    private double activationFunc(double value) {
        if (value >= 0)
            return 1.0;
        else
            return 0.0;
    }

    public void calcOut(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * weight[i];
        }
        this.out = activationFunc(sum);
    }
}
