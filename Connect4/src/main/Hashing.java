package main;

import org.junit.Test;
import static org.junit.Assert.*;

public class Hashing {

    static int size = Value.WIDTH * Value.HEIGHT;
    static final int ONES = size / 2;
    static final int ZEROS = size - ONES + 1;
    static int[][] binom = new int[ZEROS][ONES];

//    public static void main(String[] args) {
//        calcBinoms();
//    }

    @Test
    public void testCompressDist() {
        calcBinoms();
        assertEquals(13, compressDist(new boolean[] {true, false, false, true, false, true, false}));
    }

    public static int compressDist(boolean[] dist) {
        int result = 0;
        int zeros = 0, ones = 0;
        for (boolean b: dist) {
            if (b) {
                ones++;
                result += binom[zeros][ones];
            } else {
                zeros++;
            }
        }
        return result;
    }

    static void calcBinoms() {
        for (int i = 1; i < ZEROS; i++) {
            binom[i][0] = 1;
        }
        for (int i = 0; i < ONES; i++) {
            binom[0][i] = 0;
        }
        for (int i = 1; i < ZEROS; i++) {
            for (int j = 1; j < ONES; j++) {
                binom[i][j] = binom[i][j - 1] + binom[i - 1][j];
            }
        }
    }
}
