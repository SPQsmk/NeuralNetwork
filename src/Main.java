import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> layers = new ArrayList<>();
        layers.add(115);

        NNetwork nNetwork = new NNetwork(784, 10, layers, 0.134);

        try (Scanner trainScanner = new Scanner(new File("train.csv"));
             Scanner testScanner = new Scanner(new File("test.csv"))){
            double[] input = new double[784];
            double[] output = new double[10];

            while (trainScanner.hasNextLine()) {
                String[] strings = trainScanner.nextLine().split(",");
                int realNum = Integer.parseInt(strings[0]);

                for (int i = 1; i < strings.length; i++) {
                    input[i - 1] = (Double.parseDouble(strings[i]) / 255) * 0.99 + 0.01;
                }

                for (int i = 0; i < 10; i++) {
                    if (i == realNum)
                        output[i] = 0.99;
                    else
                        output[i] = 0.01;
                }

                nNetwork.train(input, output);
            }

            double ok = 0;
            double count = 0;

            while (testScanner.hasNextLine()){
                String[] strings = testScanner.nextLine().split(",");
                int realNum = Integer.parseInt(strings[0]);

                for (int i = 1; i < strings.length; i++){
                    input[i - 1] = (Double.parseDouble(strings[i]) / 255) * 0.99 + 0.01;
                }

                double[] res = nNetwork.getResult(input);
                ArrayList<Double> result = new ArrayList<>();

                for (int i = 0; i < res.length; i++) {
                    result.add(res[i]);
                }

                int maxIndex = result.indexOf(Collections.max(result));

                if (realNum == maxIndex)
                    ok += 1;
                count += 1;

                System.out.println("Real number : " + realNum + " Network result : " + maxIndex);
            }
            System.out.printf("%s%%", ok / count * 100);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}