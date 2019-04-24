package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board1 extends Board {

    private final byte[][] grid;
    final byte player;
    final int turn;

    Board1(int width, int height) {
        super(width, height);
        grid = new byte[width][height];
        player = FIRST;
        turn = 0;
    }

    Board1(byte[][] grid, byte player, int turn) {
        super(grid.length, grid[0].length);
        this.grid = grid;
        this.player = player;
        this.turn = turn;
        Reduce.reduce(grid);
        Reduce.flip(grid);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                hashCode *= 3;
                hashCode += grid[x][y];
            }
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Board1)) {
            return false;
        }
        Board1 b = (Board1) o;
        if (this.turn != b.turn) {
            return false;
        }
        return Arrays.deepEquals(this.grid, b.grid);
    }

    byte[][] getGridCopy() {
        byte[][] copy = new byte[width][];
        for (int i = 0; i < width; i++) {
            copy[i] = Arrays.copyOf(grid[i], height);
        }
        return copy;
    }

    static byte otherPlayer(byte player) {
        switch (player) {
            case FIRST: return SECOND;
            case SECOND: return FIRST;
            default: throw new IllegalArgumentException("Player must be first or second.");
        }
    }

    @Override
    Iterable<Board> nextBoards() {
        List<Board> result = new LinkedList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] == EMPTY) {
                    byte[][] next = getGridCopy();
                    next[i][j] = player;
                    result.add(new Board1(next, otherPlayer(player), turn + 1));
                    break;
                }
            }
        }
        return result;
    }
}
