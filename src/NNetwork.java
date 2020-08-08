import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NNetwork {
    private final int in;
    private final int out;
    private final double lRate;

    private final double[] lastError;
    private final LinkedList<double[]> errors = new LinkedList<>();
    private final ArrayList<double[]> outputs = new ArrayList<>();
    private final ArrayList<double[][]> weights = new ArrayList<>();

    public NNetwork(int in, int out, List<Integer> numOfLayers, double lRate) throws IllegalArgumentException {
        LinkedList<Integer> layers = new LinkedList<>(numOfLayers);
        layers.addFirst(in);
        layers.add(out);

        for (int x : layers)
            if (x < 1)
                throw new IllegalArgumentException("Некорректное число нейронов.");

        this.in = in;
        this.out = out;
        this.lRate = lRate;
        this.lastError = new double[out];

        for (int i = 0; i < layers.size() - 1; i++) {
            weights.add(new double[layers.get(i + 1)][layers.get(i)]);

            for (int j = 0; j < weights.get(i).length; j++)
                for (int k = 0; k < weights.get(i)[0].length; k++)
                    weights.get(i)[j][k] = (1 / Math.sqrt(weights.get(i)[0].length)) * (2 * Math.random() - 0.99);
        }
    }

    private double[] sigmoid(double[] in) {
        double[] out = new double[in.length];

        for (int i = 0; i < in.length; i++)
            out[i] = 1 / (1 + Math.exp(-in[i]));

        return out;
    }

    private void setOutputs(double[] in) {
        outputs.clear();
        outputs.add(in);

        for (int i = 0; i < weights.size(); i++) {
            double[] multiply = Matrix.multiplication(weights.get(i), outputs.get(i));
            double[] res = sigmoid(multiply);
            outputs.add(res);
        }
    }

    public double[] getResult(double[] in) {
        if (in.length != this.in)
            throw new IllegalArgumentException("Некорректное число входных параметров.");

        setOutputs(in);

        return outputs.get(outputs.size() - 1);
    }

    public void train(double[] in, double[] out) {
        if (in.length != this.in || out.length != this.out)
            throw new IllegalArgumentException("Некорректное число входных/выходных параметров.");

        setOutputs(in);
        errors.clear();

        for (int i = 0; i < out.length; i++)
            lastError[i] = out[i] - outputs.get(outputs.size() - 1)[i];

        errors.addFirst(lastError);

        for (int i = weights.size() - 1; i > 0; i--) {
            double[][] transport = Matrix.transport(weights.get(i));
            double[] multiply = Matrix.multiplication(transport, errors.get(0));
            errors.addFirst(multiply);
        }

        for (int i = 0; i < weights.size(); i++)
            for (int j = 0; j < weights.get(i).length; j++)
                for (int k = 0; k < weights.get(i)[0].length; k++) {
                    double jErr = errors.get(i)[j];
                    double jOut = outputs.get(i + 1)[j];
                    double kOut = outputs.get(i)[k];
                    weights.get(i)[j][k] += lRate * jErr * jOut * (1 - jOut) * kOut;
                }
    }
}