package main;

public class TestSolver extends Solver {

    public static void main(String[] args) {
        long start = System.nanoTime();
        TestSolver s = new TestSolver();
        s.solve();
        long end = System.nanoTime();
        System.out.println((end - start) / 1e9);
//        s.findInitVal();
//        s.findFirstWin();
//        s.findChildValsExplore(1, 1, 0);
//        s.findFirstPrimitive();
//        System.out.println(s.getValue(5, 881, 6));
//        s.findChildValsExplore(5, 881, 6);
        System.out.println(s.getValue(3, 631, 2));
    }

    void findFirstPrimitive() {
        System.out.println();
        System.out.println("First Primitive:");
        for (int i = 0; i < gameValues.length; i++) {
            for (int j = 0; j < gameValues[i].length; j++) {
                int[] winSets = getWinSets(shapeFromHash[i][j]);
                for (int k = 0; k < gameValues[i][j].length; k++) {
                    int dist = distFromHash(k, i);
                    if (hasWinSet((i % 2 == 0) ? dist : ~dist, winSets)) {
                        printPos(i, j, k);
                        return;
                    }
                }
            }
        }
    }

    void findChildValsExplore(int numPieces, int shape, int dist) {
        System.out.println();
        int[] childDists = getChildDists(dist, shape, numPieces, WIDTH);
        int[] childShapes = getChildShapes(shape);
        for (int i = 0; i < childShapes.length; i++) {
            byte childVal = gameValues[numPieces + 1][shapeToHash[childShapes[i]]][distToHash(childDists[i])];
            System.out.println(childVal);
            if (childVal < 0) {
                System.out.println();
                printPos(numPieces + 1, childShapes[i], childDists[i]);
                findChildValsExplore(numPieces + 1, childShapes[i], childDists[i]);
                break;
            }
        }
    }

    void findChildVals(int numPieces, int shape, int dist) {
        System.out.println();
        int[] childDists = getChildDists(dist, shape, numPieces, WIDTH);
        int[] childShapes = getChildShapes(shape);
        for (int i = 0; i < childShapes.length; i++) {
            byte childVal = gameValues[numPieces + 1][shapeToHash[childShapes[i]]][distToHash(childDists[i])];
            System.out.println(childVal);
        }
    }

    void findFirstWin() {
        System.out.println();
        System.out.println("First Win:");
        for (int i = 0; i < gameValues.length; i++) {
            for (int j = 0; j < gameValues[i].length; j++) {
                for (int k = 0; k < gameValues[i][j].length; k++) {
                    if (gameValues[i][j][k] > 0) {
                        printPos(i, shapeToHash[j], distToHash(k));
                        return;
                    }
                }
            }
        }
    }

    byte getValue(int numPieces, int shape, int dist) {
        return gameValues[numPieces][shapeToHash[shape]][distToHash(dist)];
    }

    void findInitVal() {
        System.out.println();
        System.out.println("Initial Game Value:");
        System.out.println(gameValues[0][0][0]);
    }

    void printPos(int i, int j, int k) {  // should make this not take in hashes (ok, changed it)
        System.out.println("num pieces: " + i);
        System.out.println("shape: " + j);
        System.out.println("dist: " + k);
    }

//    String gameValToString(int gameVal) {
//
//    }
}
