package main;

public class Solver { // migrate dists to long
    static final int WIDTH = Value.WIDTH, HEIGHT = Value.HEIGHT, IN_A_ROW = 4;
    static final int SIZE = WIDTH * HEIGHT, HEIGHTp1 = HEIGHT + 1;
    static final int NUM_SHAPES = Util.pow(HEIGHT + 1, WIDTH);
//    static final byte LOSE = 0, TIE = 1, WIN = 2;
//    static final String[] valToString = new String[] {"LOSE", "TIE", "WIN"};

    final byte[][][] gameValues = new byte[SIZE + 1][][];
    final long[][] binoms = Util.calcBinoms(SIZE);
    final int[] shapeToHash = new int[NUM_SHAPES];
    final int[][] shapeFromHash = new int[SIZE + 1][];
    final long[] allWinSets = genAllWinSets();
    int numValidPositions = 0;

    void solve() {
        initializeArrays();
        solveBottomTier();
        for (int tier = SIZE - 1; tier >= 0; tier--) {
            solveTier(tier);
        }
    }

    void solveTier(int numPieces) {
        int numShapesInTier = gameValues[numPieces].length;
        long numDists = getNumDists(numPieces);
        for (int shapeHash = 0; shapeHash < numShapesInTier; shapeHash++) {
            solveShape(numPieces, shapeHash, numDists);
        }
    }

    void solveBottomTier() {
        int numPieces = SIZE;
        long numDists = getNumDists(numPieces);
        boolean sizeIsEven = numPieces % 2 == 0;
        long[] winSets = allWinSets;
        byte[] valueArr = gameValues[numPieces][0];
        for (long distHash = 0; distHash < numDists; distHash++) {
            byte gameValue;
            long dist = distFromHash(distHash, numPieces);
            long validCheckDist = sizeIsEven ? ~dist : dist;
            if (hasWinSet(validCheckDist, winSets)) {
                continue;
            }
            numValidPositions++;
            long winCheckDist = sizeIsEven ? dist : ~dist;
            if (hasWinSet(winCheckDist, winSets)) {
                gameValue = -1;
            } else {
                gameValue = 0;
            }
            valueArr[(int) distHash] = gameValue;
        }
    }

    void solveShape(int numPieces, int shapeHash, long numDists) {
        boolean sizeIsEven = numPieces % 2 == 0;
        byte[] localGameVals = gameValues[numPieces][shapeHash];
        int shape = shapeFromHash[numPieces][shapeHash];
        long[] winSets = getWinSets(shape);
        int[] childShapes = getChildShapes(shape);
        int numChildren = childShapes.length;
        byte[][] childValueArrs = new byte[numChildren][];
        byte[][] childTier = gameValues[numPieces + 1];
        for (int i = 0; i < numChildren; i++) {
            childValueArrs[i] = childTier[shapeToHash[childShapes[i]]];
        }
        for (long distHash = 0; distHash < numDists; distHash++) {
            byte gameValue;
            long dist = distFromHash(distHash, numPieces);
            long validCheckDist = sizeIsEven ? ~dist : dist;
            if (hasWinSet(validCheckDist, winSets)) {
                continue;
            }
            numValidPositions++;
            long winCheckDist = sizeIsEven ? dist : ~dist;
            if (hasWinSet(winCheckDist, winSets)) {
                gameValue = (byte) (numPieces - SIZE - 1);
            } else {
                long[] childDists = getChildDists(dist, shape, numPieces, numChildren);
                byte minChildVal = Byte.MAX_VALUE;
                for (int i = 0; i < numChildren; i++) {
                    byte newVal = childValueArrs[i][(int) distToHash(childDists[i])];
                    if (newVal < minChildVal) {
                        minChildVal = newVal;
                        if (minChildVal == numPieces - SIZE) {
                            break;
                        }
                    }
                }
                gameValue = (byte) -minChildVal;
            }
            localGameVals[(int) distHash] = gameValue;
        }
    }

    long[] getChildDists(long dist, int shape, int numPieces, int numChildren) {
        int player = numPieces % 2;
        long[] childDists = new long[numChildren];
        int newPieceIndex = 0, count = 0;
        for (int i = 0; i < WIDTH; i++) {
            int numPiecesInCol = shape % (HEIGHT + 1);
            newPieceIndex += numPiecesInCol;
            if (numPiecesInCol < HEIGHT) {
                long left = (dist >>> newPieceIndex) << (newPieceIndex + 1);
                long right = dist % (1 << newPieceIndex);
                childDists[count++] = left | (player << newPieceIndex) | right;
            }
            shape /= HEIGHT + 1;
        }
        return childDists;
    }

    int[] getChildShapes(int shape) {
        int[] children = new int[WIDTH];
        int count = 0;
        int incr = 1;
        int tmpShape = shape;
        for (int i = 0; i < WIDTH; i++) {
            int h = tmpShape % (HEIGHT + 1);
            if (h < HEIGHT) {
                children[count] = shape + incr;
                count++;
            }
            incr *= HEIGHT + 1;
            tmpShape /= HEIGHT + 1;
        }
        int[] result = new int[count];
        System.arraycopy(children, 0, result, 0, count);
        return result;
    }

    boolean hasWinSet(long dist, long[] winsets) {
        for (long winSet: winsets) {
            if ((dist & winSet) == winSet) {
                return true;
            }
        }
        return false;
    }

    long[] getWinSets(int shape) {
        long shapeBitString = getShapeBitString(shape);
        long[] validBitStrings = new long[allWinSets.length];
        int numValid = 0;
        for (long winSet: allWinSets) {
            if ((shapeBitString & winSet) == winSet) {
                validBitStrings[numValid++] = winSet;
            }
        }
        long[] result = new long[numValid];
        for (int i = 0; i < numValid; i++) {
            result[i] = compressWinSet(validBitStrings[i], shape);
        }
        return result;
    }

    long compressWinSet(long winSet, int shape) {
        int offset = 0;
        for (int i = 0; i < WIDTH; i++) {
            int numPiecesInCol = shape % (HEIGHT + 1);
            int shift = offset + HEIGHT;
            offset += numPiecesInCol;
            long left = (winSet >>> shift) << offset;
            long right = winSet % (1 << offset);
            winSet = left | right;
            shape /= HEIGHT + 1;
        }
        return winSet;
    }

    long getShapeBitString(int shape) {
        long bitString = 0;
        long singleBit = 1;
        while (shape > 0) {
            int numPiecesInCol = shape % (HEIGHT + 1);
            for (int j = 0; j < numPiecesInCol; j++) {
                bitString |= singleBit << j;
            }
            singleBit <<= HEIGHT;
            shape /= HEIGHT + 1;
        }
        return bitString;
    }

    long[] genAllWinSets() {
        int vertGap = HEIGHT - IN_A_ROW + 1, horiGap = WIDTH - IN_A_ROW + 1;
        int numVertWinSets = vertGap * WIDTH;
        int numHoriWinSets = horiGap * HEIGHT;
        int numDiagWinSets = vertGap * horiGap;
        int numWinSets = numVertWinSets + numHoriWinSets + 2 * numDiagWinSets;
        long[] winSets = new long[numWinSets];
        int index = 0;
        addWinSets(1, 0, vertGap, WIDTH, index, winSets);
        index += numVertWinSets;
        addWinSets(HEIGHT, 0, HEIGHT, horiGap, index, winSets);
        index += numHoriWinSets;
        addWinSets(HEIGHT + 1, 0, vertGap, horiGap, index, winSets);
        index += numDiagWinSets;
        addWinSets(HEIGHT - 1, IN_A_ROW - 1, vertGap, horiGap, index, winSets);
        return winSets;
    }

    void addWinSets(int stride, int offset, int numVertShifts, int numHoriShifts, int index, long[] winSets) {
        long canonWinSet = 0;
        for (int i = 0; i < IN_A_ROW; i++) {
            canonWinSet <<= stride;
            canonWinSet |= 1;
        }
        canonWinSet <<= offset;
        for (int i = 0; i < numHoriShifts; i++) {
            for (int j = 0; j < numVertShifts; j++) {
                winSets[index++] = canonWinSet << (i * HEIGHT + j);
            }
        }
    }

    long distFromHash(long hash, int numPieces) {
        long dist = 0;
        int numOnes = numPieces / 2;
        for (int index = numPieces - 1; index >= 0; index--) {
            dist <<= 1;
            long binom = binoms[index][numOnes];
            if (binom <= hash) {
                dist |= 1;
                numOnes--;
                hash -= binom;
            }
        }
        return dist;
    }

    long distToHash(long dist) {
        long hash = 0;
        int numOnes = 0;
        int index = 0;
        while (dist != 0) {
            if ((dist & 1) == 1) {
                numOnes++;
                hash += binoms[index][numOnes];
            }
            dist >>>= 1;
            index++;
        }
        return hash;
    }

    void initializeArrays() {
        int[] arrSizes = new int[SIZE + 1];
        for (int shape = 0; shape < NUM_SHAPES; shape++) {
            int numPieces = getNumPieces(shape);
            shapeToHash[shape] = arrSizes[numPieces];
            arrSizes[numPieces]++;
        }
        for (int i = 0; i <= SIZE; i++) {
            gameValues[i] = new byte[arrSizes[i]][(int) getNumDists(i)];
            shapeFromHash[i] = new int[arrSizes[i]];
        }
        for (int shape = 0; shape < NUM_SHAPES; shape++) {
            shapeFromHash[getNumPieces(shape)][shapeToHash[shape]] = shape;
        }
    }

    long getNumDists(int numPieces) {
        return binoms[numPieces][numPieces / 2];
    }

    int getNumPieces(int shape) {
        int num = 0;
        while (shape > 0) {
            num += shape % (HEIGHT + 1);
            shape /= (HEIGHT + 1);
        }
        return num;
    }
}
