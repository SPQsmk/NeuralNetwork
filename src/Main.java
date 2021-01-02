import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> layers = new ArrayList<>();
        layers.add(4);

        NNetwork nNetwork = new NNetwork(3, 1, layers, 15);

        try (Scanner scanner = new Scanner(new File("data.txt"))) {
            ArrayList<Double> train = new ArrayList<>();
            ArrayList<Double> data = new ArrayList<>();

            for (int i = 0; i < 2000; i++) {
                if (scanner.hasNextDouble()) {
                    train.add(scanner.nextDouble());
                }
            }

            while (scanner.hasNextDouble()) {
                data.add(scanner.nextDouble());
            }

            double[] input = new double[3];
            double[] output = new double[1];

            double max = Collections.max(train);
            double min = Collections.min(train);

            for (int i = 0; i < train.size() - 3; i++) {
                input[0] = (train.get(i) - min) / (max - min);
                input[1] = (train.get(i + 1) - min) / (max - min);
                input[2] = (train.get(i + 2) - min) / (max - min);
                output[0] = (train.get(i + 3) - min) / (max - min) / 10;

                nNetwork.train(input, output);
            }

            double accuracy = 0.0;
            double count = 0;

            for (int i = 0; i < data.size() - 3; i++) {
                input[0] = (data.get(i) - min) / (max - min);
                input[1] = (data.get(i + 1) - min) / (max - min);
                input[2] = (data.get(i + 2) - min) / (max - min);
                output[0] = data.get(i + 3);

                double[] out = nNetwork.getResult(input);
                double res = out[0] * 10 * (max - min) + min;

//                accuracy += (Math.abs(res - output[0])) / output[0] * 100;
                accuracy += (res - output[0]) * (res - output[0]);
                count++;

                System.out.println("real: " + output[0] + " forecast: " + res);
            }

            System.out.println("accuracy: " + (accuracy / count));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}