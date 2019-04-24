package main;

public interface DartBoard {
    int[] getChildDists(int dist, int shape, int numPieces, int numChildren);
    int[] getChildShapes(int shape);
    int[] getWinSets(int shape);
    int[] genAllWinSets();
    int distFromHash(int hash, int numPieces);
    int distToHash(int dist);
    int getNumDists(int numPieces);
    int getNumPieces(int shape);
    int[] getEquivalentShapes(int shape);
    int getSize();
    int getNumShapes();
}
