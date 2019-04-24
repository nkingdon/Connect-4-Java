package main;

public class SymmetricSolver extends Solver {
    int getMirrorShape(int shape) {
        int mirror = 0;
        for (int i = 0; i < WIDTH; i++) {
            mirror *= HEIGHTp1;
            mirror += shape % HEIGHTp1;
            shape /= HEIGHTp1;
        }
        return mirror;
    }

    int getCanonicalShape(int shape) {
        return Math.min(shape, getMirrorShape(shape));
    }

    boolean isCanonicalShape(int shape) {
        return shape <= getMirrorShape(shape);
    }
}
