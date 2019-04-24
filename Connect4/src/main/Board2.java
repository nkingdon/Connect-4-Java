package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board2 extends Board {
    final short[] grid;
    final short player;

    Board2(int width, int height) {
        super(width, height);
        grid = new short[width];
        player = FIRST;
    }

    Board2(short[] grid, int height, short player) {
        super(grid.length, height);
        this.grid = grid;
        this.player = player;
//        Reduce.reduceShort2(grid, height);
//        Reduce.flipShort(grid);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(grid);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Board2)) {
            return false;
        }
        Board2 b = (Board2) o;
        return Arrays.equals(this.grid, b.grid);
    }

    @Override
    Iterable<Board> nextBoards() {
        List<Board> result = new LinkedList<>();
        for (int i = 0; i < width; i++) {
            short col = grid[i];
            for (int j = 0; j < height; j++) {
                if (col == 0) {
                    short[] copy = Arrays.copyOf(grid, width);
                    copy[i] |= player << (2 * j);
                    result.add(new Board2(copy, height, (short) (player ^ 3)));
                    break;
                }
                col >>>= 2;
            }
        }
        return result;
    }
}
