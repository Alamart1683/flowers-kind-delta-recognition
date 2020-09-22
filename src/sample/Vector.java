package sample;

public class Vector {
    private double[] x; // массив входных весов
    private double[] desireOutputs; // массив желаемых значений
    String file_name;
    // конструктор вектора
    public Vector(double[] x, double[] desireOutputs, String file_name) {
        this.x = new double[x.length];
        x[0] = 1;
        for (int i = 0; i < x.length; i++) {
            this.x[i] = x[i];
        }
        this.desireOutputs = desireOutputs;
        this.file_name = file_name;
    }

    public double[] getX() {
        return x;
    }

    public double[] getDesireOutputs() {
        return desireOutputs;
    }
}
