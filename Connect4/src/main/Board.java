package main;

public abstract class Board {
    static final int FOUR = 4;
    static final byte EMPTY = 0;
    static final byte FIRST = 1;
    static final byte SECOND = 2;
    static final byte DEAD = 3;

    final int width, height;

    Board(int width, int height) {
        this.width = width;
        this.height = height;
    }

    abstract Iterable<Board> nextBoards();
}
