package main;

public class Util {
//    static int[][] getBinoms(int a, int b) {
//        int[][] binoms = new int[a][b];
//        for (int i = 0; i < a; i++) {
//            binoms[i][0] = 1;
//        }
//        for (int j = 0; j < b; j++) {
//            binoms[0][j] = 1;
//        }
//        for (int i = 0; i < a; i++) {
//            for (int j = 0; j < b; j++) {
//                binoms[i][j] = binoms[i - 1][j] + binoms[i][j - 1];
//            }
//        }
//        return binoms;
//    }
//
//    int[] shapeToArray(int shape) {
//        int[] shapeArr = new int[WIDTH];
//        for (int i = 0; i < WIDTH; i++) {
//            shapeArr[i] = shape % (HEIGHT + 1);
//            shape /= HEIGHT + 1;
//        }
//        return shapeArr;
//    }

    static long[][] calcBinoms(int n) {
        long[][] binoms = new long[n + 1][];
        for (int i = 0; i <= n; i++) {
            binoms[i] = new long[i + 2];
            binoms[i][0] = 1;
            binoms[i][i + 1] = 0;
            for (int j = 1; j <= i; j++) {
                binoms[i][j] = binoms[i - 1][j - 1] + binoms[i - 1][j];
            }
        }
        return binoms;
    }

    static int pow(int a, int b) {
        int result = 1;
        for (int i = 0; i < b; i++) {
            result *= a;
        }
        return result;
    }

    static int intArrayMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        for (int i: arr) {
            min = Math.min(min, i);
        }
        return min;
    }
}
