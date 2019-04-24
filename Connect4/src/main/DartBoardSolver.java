//package main;
//
//public class DartBoardSolver {
//    static final byte LOSE = 0, TIE = 1, WIN = 2;
//    final DartBoard g;
//    final int SIZE;
//    final int NUM_SHAPES;
//    final byte[][][] gameValues;
//    final int[] shapeToHash;
//    final int[][] shapeFromHash;
////    final int[] canonShapes;
//    final int[] tierSizes;
//    final int[] allWinSets;
//
//    DartBoardSolver(DartBoard game) {
//        this.g = game;
//        SIZE = game.getSize();
//        NUM_SHAPES = game.getNumShapes();
//        shapeToHash = new int[NUM_SHAPES];
//        shapeFromHash = new int[SIZE + 1][];
//        gameValues = new byte[SIZE + 1][][];
//        tierSizes = new int[SIZE + 1];
//        allWinSets = game.genAllWinSets();
//    }
//
//    void solve() {
//        initializeArrays();
//        solveBottomTier();
//        for (int tier = SIZE - 1; tier >= 0; tier--) {
//            solveTier(tier);
//        }
//    }
//
//    void solveTier(int numPieces) {
//        int numShapesInTier = tierSizes[numPieces];
//        int numDists = g.getNumDists(numPieces);
//        for (int shapeHash = 0; shapeHash < numShapesInTier; shapeHash++) {
//            solveShape(numPieces, shapeHash, numDists);
//        }
//    }
//
//    void solveBottomTier() {
//        int numPieces = SIZE;
//        int numDists = g.getNumDists(numPieces);
//        boolean sizeIsEven = numPieces % 2 == 0;
//        int[] winSets = allWinSets;
//        byte[] valueArr = gameValues[numPieces][0];
//        for (int distHash = 0; distHash < numDists; distHash++) {
//            byte gameValue;
//            int dist = g.distFromHash(distHash, numPieces);
//            int winCheckDist = (sizeIsEven) ? dist : ~dist;
//            if (hasWinSet(winCheckDist, winSets)) {
//                gameValue = LOSE;
//            } else {
//                gameValue = TIE;
//            }
//            valueArr[distHash] = gameValue;
//        }
//    }
//
//    void solveShape(int numPieces, int shapeHash, int numDists) {  // doesn't handle symmetry properly yet
//        byte[] localGameVals = gameValues[numPieces][shapeHash];
//        int shape = shapeFromHash[numPieces][shapeHash];
//        int[] winSets = g.getWinSets(shape);
//        int[] childShapes = g.getChildShapes(shape);
//        int numChildren = childShapes.length;
//        byte[][] childValueArrs = new byte[numChildren][];
//        byte[][] childTier = gameValues[numPieces + 1];
//        for (int i = 0; i < numChildren; i++) {
//            childValueArrs[i] = childTier[shapeToHash[childShapes[i]]];
//        }
//        for (int distHash = 0; distHash < numDists; distHash++) {
//            byte gameValue;
//            int dist = g.distFromHash(distHash, numPieces);
//            int winCheckDist = (numPieces % 2 == 0) ? dist : ~dist;
//            if (hasWinSet(winCheckDist, winSets)) {
//                gameValue = LOSE;
//            } else {
//                int[] childDists = g.getChildDists(dist, shape, numPieces, numChildren);
//                byte minChildVal = WIN;
//                for (int i = 0; i < numChildren; i++) {
//                    byte newVal = childValueArrs[i][g.distToHash(childDists[i])];
//                    if (newVal < minChildVal) {
//                        minChildVal = newVal;
//                        if (minChildVal == LOSE) {
//                            break;
//                        }
//                    }
//                }
//                gameValue = (byte) (WIN - minChildVal);
//            }
//            localGameVals[distHash] = gameValue;
//        }
//    }
//
//    boolean hasWinSet(int dist, int[] winSets) {
//        for (int winSet: winSets) {
//            if ((dist & winSet) == winSet) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    void initializeArrays() {
//        for (int shape = 0; shape < NUM_SHAPES; shape++) {
//            int canonShape = Util.intArrayMin(g.getEquivalentShapes(shape));
//            if (shape == canonShape) {
//                int numPieces = g.getNumPieces(shape);
//                shapeToHash[shape] = tierSizes[numPieces];
//                tierSizes[numPieces]++;
//            } else {
//                shapeToHash[shape] = shapeToHash[canonShape];
//            }
//        }
//        for (int i = 0; i <= SIZE; i++) {
//            gameValues[i] = new byte[tierSizes[i]][g.getNumDists(i)];
//            shapeFromHash[i] = new int[tierSizes[i]];
//        }
//        for (int shape = NUM_SHAPES - 1; shape >= 0; shape--) {
//            shapeFromHash[g.getNumPieces(shape)][shapeToHash[shape]] = shape;
//        }
//    }
//}
