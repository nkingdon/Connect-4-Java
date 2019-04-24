package main;

import java.util.HashSet;

public class Counting {
    static HashSet<Board> seen = new HashSet<>();

    public static void main(String[] args) {
        System.out.println(count(4, 6));
    }

    static int count(int width, int height) {
        long start = System.nanoTime();
        accumulate(new Board2(width, height));
        long end = System.nanoTime();
        System.out.println((end - start) / 1e9);
        return seen.size();
    }

    static void accumulate(Board board) {
        if (seen.contains(board)) {
            return;
        } else {
            for (Board next: board.nextBoards()) {
                accumulate(next);
            }
            seen.add(board);
        }
    }
}
