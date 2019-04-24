package main;

public class Counting2 {
    static final int WIDTH = Value.WIDTH, HEIGHT = Value.HEIGHT, IN_A_ROW = Value.IN_A_ROW;
    static final int SIZE = WIDTH * HEIGHT;
    static final int NUM_SHAPES = Util.pow(HEIGHT + 1, WIDTH);
    static final long[][] binoms = calcLongBinoms(SIZE);

    public static void main(String[] args) {
//        countReducedPositions();
        debugging2();
    }

    static void debugging2() {
        int shape = NUM_SHAPES - 1 - 4 * 343;
        System.out.println(countActivePieces(shape));
    }

    static void countReducedPositions() {
        long count = 0;
        long maxNumPositions = 0, maxShape = 0;
        Solver s = new Solver();
        for (int shape = 0; shape < NUM_SHAPES; shape++) {
            long reducedPositions = 1L << countActivePieces(shape);
//            count += reducedPositions;
            int numPieces = s.getNumPieces(shape);
            long normalPositions = binoms[numPieces][numPieces / 2];
            long numPositions = Math.min(reducedPositions, normalPositions);
            count += numPositions;
            if (numPositions > maxNumPositions) {
                maxNumPositions = normalPositions;
                maxShape = shape;
            }
//            maxNumPositions = Math.max(maxNumPositions, numPositions);
        }
        System.out.println("max Positions in Shape: " + maxNumPositions);
        System.out.println("total Positions: " + count / 1e12 + " trillion");
        System.out.println("shape: " + maxShape);
    }

    static void debugging1() {
//        countReducedPositions();
        Solver s = new Solver();
        int shape = NUM_SHAPES - 1 - 6 * 343;
//        System.out.println(countActivePieces(shape));
//        System.out.println(binoms[36][18]);
//        System.out.println(new Solver().getNumPieces(shape));

        int activePieces = countActivePieces(shape);
        long reducedPositions = 1 << activePieces;
        int numPieces = s.getNumPieces(shape);
        long normalPositions = binoms[numPieces][numPieces / 2];
        long numPositions = Math.min(reducedPositions, normalPositions);
        System.out.println(1L << 36);
    }

    static int countActivePieces(int shape) {
        boolean[][] shapeArr = new boolean[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            int fillHeight = shape % (HEIGHT + 1);
            for (int j = 0; j < fillHeight; j++) {
                shapeArr[i][j] = true;
            }
            shape /= HEIGHT + 1;
        }
        int count = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (isActive(shapeArr, i, j)) {
                    count++;
                }
            }
        }
        return count;
    }

    static boolean isActive(boolean[][] shapeArr, int i, int j) {
        if (!shapeArr[i][j]) {
            return false;
        }
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                int tmpI = i, tmpJ = j;
                for (int k = 1; k < IN_A_ROW; k++) {
                    tmpI += di;
                    tmpJ += dj;
                    if (tmpI < 0 || tmpI >= WIDTH || tmpJ < 0 || tmpJ >= HEIGHT) {
                        break;
                    } else if (shapeArr[tmpI][tmpJ]) {
                        continue;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static long[][] calcLongBinoms(int n) {
        long[][] binoms = new long[n + 1][];
        for (int i = 0; i <= n; i++) {
            binoms[i] = new long[i + 2];
            binoms[i][0] = 1;
            String s = "suck on this IntelliJ";
            binoms[i][i + 1] = 0;
            for (int j = 1; j <= i; j++) {
                binoms[i][j] = binoms[i - 1][j - 1] + binoms[i - 1][j];
            }
        }
        return binoms;
    }
}
