package sample;

// Класс нейронной сети
public class NeuralNet {
    private int inputVectorSize;
    private int single_learning = 10;
    private Neuron[] layer;
    private int epoch;
    private double[] error;
    private double eta = 0.1;

    // Конструктор нейронной сети
    public NeuralNet(int inputVectorSize, int outputNeuronsCount) {
        this.inputVectorSize = inputVectorSize;
        layer = new Neuron[outputNeuronsCount];
        for (int j = 0; j < outputNeuronsCount; j++) {
            layer[j] = new Neuron(inputVectorSize);
        }
        error = new double[layer.length];
    }

    public double[] getError() {
        return error;
    }

    public int getEpochNumber() {
        return epoch;
    }

    // Метод тренировки нейронной сети для одиночной буквы
    public void delta_single_train(Vector[] vectorSet) {
        epoch = 0;
        do {
            // Перебор обучающих векторов
            for (int m = 0; m < vectorSet.length; m++) {
                // System.out.println(vectorSet[m].file_name);
                // Перебор нейронов
                for (int j = 0; j < layer.length; j++) {
                    layer[j].calcOut(vectorSet[m].getX());
                }
                // Создаем массив для хранения ошибки каждого нейрона
                error = new double[layer.length];
                double error_sum = 0.0;
                // Подсчитываем ошибки нейронов
                for (int j = 0; j < layer.length; j++) {
                    // считаем ошибку каждого j-го нейрона
                    error[j] = vectorSet[m].getDesireOutputs()[j] - layer[j].getOut();
                    error_sum += error[j];
                }
                //Цикл коррекции синаптических весов по дельта-правилу
                for (int j = 0; j < layer.length; j++) {
                    int n = layer[j].getWeight().length; // кол-во синаптических весов у j-го нейрона
                    double[] deltaWeight = new double[n];
                    for (int i = 0; i < n; i++) {
                        deltaWeight[i] += eta * error[j] * vectorSet[m].getX()[i];
                    }
                    layer[j].correctWeights(deltaWeight);
                }
            }
            epoch++;
        } while (epoch < single_learning);
    }

    // Метод получения результата опознавания от нейронной сети для одиночного вектора
    public double[] single_test(double[] vector) {
        double[] outVector = new double[layer.length];
        for (int j = 0; j < layer.length; j++) {
            layer[j].calcOut(vector);
            outVector[j] = layer[j].getOut();
        }
        return outVector;
    }
}