package main;

import java.util.Arrays;

public class Reduce {

    public static void main(String[] args) {
        testReduceShort();
    }

    static void testReduceShort() {
        int height = 5;
        short[] grid = new short[] {
                0b0110011001,
                0b1001100110,
                0b0110011001,
                0b1001100110,
                0b0110011001};
        reduceShort2(grid, height);
        printShort(grid, height);
    }

    static void updateHorizontal(short[] grid, short[] canWin) {
        int end = grid.length - Board.FOUR;
        for (int i = 0; i <= end; i++) {
            short mask = grid[i];
            mask |= grid[i + 1];
            mask |= grid[i + 2];
            mask |= grid[i + 3];
            short tmp = mask;
            tmp >>>= 1;
            mask &= tmp;
//            mask |= 0xAAAA;
            mask ^= 0xFFFF;
            canWin[i] |= mask;
            canWin[i + 1] |= mask;
            canWin[i + 2] |= mask;
            canWin[i + 3] |= mask;
        }
    }

    /* currently only works with odd widths */
    static void reduceShort(short[] grid, int height) {
        int width = grid.length, end = width - 1, half = width / 2;
        short[] canWin = new short[width];
        updateHorizontal(grid, canWin);
        short[] gridSum = Arrays.copyOf(grid, width);
        short oneMask = 0;
        oneMask |= 0xFFFF;
        oneMask <<= 2 * (height - half);
        short negOneMask = oneMask;
        negOneMask ^= 0xFFFF;
        short ones;
        ones = oneMask;
        for (int i = 0; i < half; i++) {
            short col = gridSum[i];
            col >>>= 2 * (half - i);
            col |= ones;
            gridSum[i] = col;
            ones <<= 2;
        }
        ones = negOneMask;
        for (int i = 0; i < half; i++) {
            short col = gridSum[end - i];
            col <<= 2 * (half - i);
            col |= ones;
            gridSum[i] = col;
            ones >>>= 2;
        }
        short[] canWinSum = new short[width];
        updateHorizontal(gridSum, canWinSum);
        short[] gridDiff = Arrays.copyOf(grid, width);
        ones = negOneMask;
        for (int i = 0; i < half; i++) {
            short col = gridDiff[i];
            col <<= 2 * (half - i);
            col |= ones;
            gridDiff[i] = col;
            ones >>>= 2;
        }
        ones = oneMask;
        for (int i = 0; i < half; i++) {
            short col = gridDiff[end - i];
            col >>>= 2 * (half - i);
            col |= ones;
            gridDiff[i] = col;
            ones <<= 2;
        }
        short[] canWinDiff = new short[width];
        updateHorizontal(gridSum, canWinDiff);
        for (int i = 0; i < half; i++) {
            canWin[i] |= (canWinSum[i] << (2 * (half - i))) | (canWinDiff[i] >>> (2 * (half - i)));
            canWin[end - i] |= (canWinSum[end - i] >>> (2 * (half - i))) | (canWinDiff[end - i] << (2 * (half - i)));
        }
        for (int i = 0; i < width; i++) {
            short value = grid[i];
            short tmp = value;
            tmp >>>= 1;
            value |= tmp;
            short mask = canWin[i];
            mask ^= 0xFFFF;
            mask &= value;
            mask &= 0x5555;
            tmp = mask;
            tmp <<= 1;
            mask |= tmp;
            grid[i] |= mask;
        }
    }

    static void testReduce() {
        byte[][] grid = new byte[][] {
                {1, 0, 0, 0},
                {1, 1, 1, 2},
                {2, 2, 1, 1},
                {2, 1, 2, 1}};
        reduce(grid);
        printBoard(grid);
    }

    static void printShort(short[] grid, int height) {
        int width = grid.length;
        for (int i = 0; i < width; i++) {
            short value = grid[i];
            for (int j = 0; j < height; j++) {
                System.out.print(value % 4 + " ");
                value >>>= 2;
            }
            System.out.println("");
        }
    }

    static void printBoard(byte[][] grid) {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                System.out.print(grid[x][y] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    static void reduce(byte[][] grid) {
        reduce1(grid);
    }

    static byte get(short col, int y) {
        return (byte) ((col >>> (2 * y)) % 4);
    }

    static void reduceShort2(short[] grid, int height) {
        int width = grid.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                byte value = get(grid[x], y);
                if (value == Board.EMPTY || value == Board.DEAD) {
                    continue;
                }
                int i1, i2;
                i1 = 0;
                i2 = 1;
                while (x - i1 >= 0 && (get(grid[x - i1], y) == Board.EMPTY || get(grid[x - i1], y) == value)) {
                    i1++;
                }
                while (x + i2 < width && (get(grid[x + i2], y) == Board.EMPTY || get(grid[x + i2], y) == value)) {
                    i2++;
                }
                if (i2 + i1 > Board.FOUR) {
                    continue;
                }
                i1 = 1;
                i2 = 1;
                while (y - i1 >= 0 && (get(grid[x], y - i1) == Board.EMPTY || get(grid[x], y - i1) == value)) {
                    i1++;
                }
                while (y + i2 < height && (get(grid[x], y + i2) == Board.EMPTY || get(grid[x], y + i2) == value)) {
                    i2++;
                }
                if (i2 + i1 > Board.FOUR) {
                    continue;
                }
                i1 = 1;
                i2 = 1;
                while (x - i1 >= 0 && y - i1 >= 0 && (get(grid[x - i1], y - i1) == Board.EMPTY || get(grid[x - i1], y - i1) == value)) {
                    i1++;
                }
                while (x + i2 < width && y + i2 < height && (get(grid[x + i2], y + i2) == Board.EMPTY || get(grid[x + i2], y + i2) == value)) {
                    i2++;
                }
                if (i2 + i1 > Board.FOUR) {
                    continue;
                }
                i1 = 1;
                i2 = 1;
                while (x - i1 >= 0 && y + i1 < height && (get(grid[x - i1], y + i1) == Board.EMPTY || get(grid[x - i1], y + i1) == value)) {
                    i1++;
                }
                while (x + i2 < width && y - i2 >= 0 && (get(grid[x + i2], y - i2) == Board.EMPTY || get(grid[x + i2], y - i2) == value)) {
                    i2++;
                }
                if (i2 + i1 > Board.FOUR) {
                    continue;
                }
                grid[x] |= Board.DEAD << (2 * y);
            }
        }
    }

    static void flipShort(short[] grid) {
        int width = grid.length, half = width / 2, end = width - 1;
        for (int i = 0; i < half; i++) {
            short left = grid[i], right = grid[end - i];
            if (left != right) {
                if (left < right) {
                    for (int k = 0; k < half; k++) {
                        short tmp = grid[k];
                        grid[k] = grid[end - k];
                        grid[end - k] = tmp;
                    }
                }
                return;
            }
        }
    }

    static int explore(byte[][] grid, int x, int y, int value, int dx, int dy) {
        int width = grid.length, height = grid[0].length;
        byte other;
        int i = 1;
        while (true) {
            x += dx;
            y += dy;
            if (x < 0 || x >= width || y < 0 || y >= height) {
                break;
            }
            other = grid[x][y];
            if (other != Board.EMPTY && other != value) {
                break;
            }
            i++;
        }
        return i;
    }

    static void reduce4(byte[][] grid) {
        int width = grid.length, height = grid[0].length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                byte value = grid[x][y];
                if (value == Board.EMPTY || value == Board.DEAD) {
                    continue;
                }
                boolean dead = true;
                for (int[] step: new int[][] {{1, 0}, {0, 1}, {1, 1}, {1, -1}}) {
                    if (explore(grid, x, y, value, step[0], step[1]) +
                            explore(grid, x, y, value, -step[0], -step[1]) > Board.FOUR) {
                        dead = false;
                        break;
                    }
                }
                if (dead) {
                    grid[x][y] = Board.DEAD;
                }
            }
        }
    }

    static void reduce1(byte[][] grid) {
        int width = grid.length, height = grid[0].length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                byte value = grid[x][y];
                if (value == Board.EMPTY || value == Board.DEAD) {
                    continue;
                }
                int i1, i2;
                i1 = 1;
                i2 = 1;
                while (x - i1 >= 0 && (grid[x - i1][y] == Board.EMPTY || grid[x - i1][y] == value)) {
                    i1++;
                }
                while (x + i2 < width && (grid[x + i2][y] == Board.EMPTY || grid[x + i2][y] == value)) {
                    i2++;
                }
                if (i2 + i1 > Board.FOUR) {
                    continue;
                }
                i1 = 1;
                i2 = 1;
                while (y - i1 >= 0 && (grid[x][y - i1] == Board.EMPTY || grid[x][y - i1] == value)) {
                    i1++;
                }
                while (y + i2 < height && (grid[x][y + i2] == Board.EMPTY || grid[x][y + i2] == value)) {
                    i2++;
                }
                if (i2 + i1 > Board.FOUR) {
                    continue;
                }
                i1 = 1;
                i2 = 1;
                while (x - i1 >= 0 && y - i1 >= 0 && (grid[x - i1][y - i1] == Board.EMPTY || grid[x - i1][y - i1] == value)) {
                    i1++;
                }
                while (x + i2 < width && y + i2 < height && (grid[x + i2][y + i2] == Board.EMPTY || grid[x + i2][y + i2] == value)) {
                    i2++;
                }
                if (i2 + i1 > Board.FOUR) {
                    continue;
                }
                i1 = 1;
                i2 = 1;
                while (x - i1 >= 0 && y + i1 < height && (grid[x - i1][y + i1] == Board.EMPTY || grid[x - i1][y + i1] == value)) {
                    i1++;
                }
                while (x + i2 < width && y - i2 >= 0 && (grid[x + i2][y - i2] == Board.EMPTY || grid[x + i2][y - i2] == value)) {
                    i2++;
                }
                if (i2 + i1 > Board.FOUR) {
                    continue;
                }
                grid[x][y] = Board.DEAD;
            }
        }
    }

    static void flip(byte[][] grid) {
        int width = grid.length, end = width - 1, half = width / 2, height = grid[0].length;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < half; i++) {
                if (grid[i][j] != grid[end - i][j]) {
                    if (grid[i][j] < grid[end - i][j]) {
                        byte[] tmp;
                        for (int k = 0; k < half; k++) {
                            tmp = grid[k];
                            grid[k] = grid[end - k];
                            grid[end - k] = tmp;
                        }
                    }
                    return;
                }
            }
        }
    }

    static boolean canWin(byte a, byte b, byte c, byte d) {
        int x = Board1.FIRST, y = Board1.SECOND, z = Board1.DEAD;
        return !((a == z || b == z || c == z || d == z) ||
                (a == x || b == x || c == x || d == x) && (a == y || b == y || c == y || d == y));
    }

    static void update(byte[][] grid, boolean[][] canWin, int x0, int x1, int x2, int x3, int y0, int y1, int y2, int y3) {
        if (canWin(grid[x0][y0], grid[x1][y1], grid[x2][y2], grid[x3][y3])) {
            canWin[x0][y0] = true;
            canWin[x1][y1] = true;
            canWin[x2][y2] = true;
            canWin[x3][y3] = true;
        }
    }

    static void reduce3(byte[][] grid) {
        int width = grid.length, height = grid[0].length;
        boolean[][] canWin = new boolean[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = Board.FOUR - 1; x < width; x++) {
                update(grid, canWin, x, x - 1, x - 2, x - 3, y, y, y, y);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = Board.FOUR - 1; y < height; y++) {
                update(grid, canWin, x, x, x, x, y, y - 1, y - 2, y - 3);
            }
        }
        for (int sum = Board.FOUR - 1; sum < width + height - Board.FOUR; sum++) {
            for (int x = Math.max(0, sum - height + 1) + Board.FOUR - 1; x <= Math.min(width - 1, sum); x++) {
                int y = sum - x;
                update(grid, canWin, x, x - 1, x - 2, x - 3, y, y + 1, y + 2, y + 3);
            }
        }
        for (int diff = Board.FOUR - height; diff <= width - Board.FOUR; diff++) {
            for (int x = Math.max(0, diff) + Board.FOUR - 1; x < Math.min(width, height + diff); x++) {
                int y = x - diff;
                update(grid, canWin, x, x - 1, x - 2, x - 3, y, y - 1, y - 2, y - 3);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!canWin[x][y] && grid[x][y] != Board.EMPTY) {
                    grid[x][y] = Board.DEAD;
                }
            }
        }
    }

    static void reduce2(byte[][] grid) {
        boolean[][] canWin = new boolean[grid.length][grid[0].length];
        for (int y = 0; y < grid[0].length; y++) {
            int[] prev = new int[] {0, -1, -1};
            for (int x = 0; x < grid.length; x++) {
                int value = grid[x][y];
//                if (value == Board1.DEAD) {
//                    prev[1] = x;
//                    prev[2] = x;
//                }
                if (value != Board1.EMPTY) {
                    int last = prev[value];
                    if (value == Board1.DEAD) {
//                        last
                    }
                    prev[value] = x;
                    if (x - last > Board1.FOUR) {
                        for (int i = last + 1; i < x; i++) {
                            canWin[i][y] = true;
                        }
                        break;
                    }
                }
            }
            int minPrev = Math.min(prev[1], prev[2]);
            if (grid.length - minPrev > Board1.FOUR) {
                for (int i = minPrev + 1; i < grid.length; i++) {
                    canWin[i][y] = true;
                }
            }
        }
        for (int x = 0; x < grid.length; x++) {
            int[] prev = new int[] {0, -1, -1};
            for (int y = 0; y < grid[x].length; y++) {
                int value = grid[x][y];
                if (value == Board1.DEAD) {
                    prev[1] = y;
                    prev[2] = y;
                }
                if (value == Board1.FIRST || value == Board1.SECOND) {
                    int last = prev[value];
                    prev[value] = y;
                    if (y - last > Board1.FOUR) {
                        for (int i = last + 1; i < y; i++) {
                            canWin[x][i] = true;
                        }
                        break;
                    }
                }
            }
            int minPrev = Math.min(prev[1], prev[2]);
            if (grid.length - minPrev > Board1.FOUR) {
                for (int i = minPrev + 1; i < grid[x].length; i++) {
                    canWin[x][i] = true;
                }
            }
        }
        for (int sum = Board1.FOUR; sum < grid.length + grid[0].length - Board1.FOUR; sum++) {
            int x = Math.max(0, sum - grid[0].length + 1);
            int[] prevX = new int[] {0, x - 1, x - 1};
            for (; x < Math.min(grid.length, sum); x++) {
                int y = sum - x;
                int value = grid[x][y];
                if (value == Board1.DEAD) {
                    prevX[1] = x;
                    prevX[2] = x;
                }
                if (value == Board1.FIRST || value == Board1.SECOND) {
                    int gap = x - prevX[value];
                    prevX[value] = x;
                    if (gap > Board1.FOUR) {
                        for (int i = 1; i < gap; i++) {
                            canWin[x - i][y + i] = true;
                        }
                        break;
                    }
                    prevX[value] = x;
                }
            }
            int gap = x - Math.min(prevX[1], prevX[2]);
            if (gap > Board1.FOUR) {
                int y = sum - x;
                for (int i = 1; i < gap; i++) {
                    canWin[x - i][y + i] = true;
                }
            }
        }
        for (int diff = Board1.FOUR - grid[0].length; diff <= grid.length - Board1.FOUR; diff++) {
            int x = Math.max(0, diff);
            int[] prevX = new int[] {0, x - 1, x - 1};
            for (; x < Math.min(grid.length, grid[0].length - 1 + diff); x++) {
                int y = x - diff;
                int value = grid[x][y];
                if (value == Board1.DEAD) {
                    prevX[1] = x;
                    prevX[2] = x;
                }
                if (value == Board1.FIRST || value == Board1.SECOND) {
                    int gap = x - prevX[value];
                    prevX[value] = x;
                    if (gap > Board1.FOUR) {
                        for (int i = 1; i < gap; i++) {
                            canWin[x - i][y - i] = true;
                        }
                        break;
                    }
                }
            }
            int gap = x - Math.min(prevX[1], prevX[2]);
            if (gap > Board1.FOUR) {
                int y = x - diff;
                for (int i = 1; i < gap; i++) {
                    canWin[x - i][y - i] = true;
                }
            }
        }
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (!canWin[x][y] && grid[x][y] != Board1.EMPTY) {
                    grid[x][y] = Board1.DEAD;
                }
            }
        }
    }
}
