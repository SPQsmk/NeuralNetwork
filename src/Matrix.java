public class Matrix {
    public static double[][] transport(double[][] m) {
        double[][] res = new double[m[0].length][m.length];

        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                res[j][i] = m[i][j];

        return res;
    }

    public static double[] multiplication(double[][] a, double[] b) {
        if (a[0].length != b.length)
            throw new IllegalArgumentException("Умножение для заданных матриц выполнить невозможно");

        double[] res = new double[a.length];

        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++)
                res[i] += a[i][j] * b[j];

        return res;
    }
}