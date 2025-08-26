package br.com.dio.sudoku.solver;

import br.com.dio.sudoku.model.Board;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SudokuSolver {
    private SudokuSolver() {}

    public static boolean solve(Board board) {
        List<int[]> empties = new ArrayList<>();
        for (int r = 0; r < Board.SIZE; r++)
            for (int c = 0; c < Board.SIZE; c++)
                if (board.get(r,c).getValue() == null && !board.get(r,c).isFixed())
                    empties.add(new int[]{r,c});

        // MRV: célula com menos candidatos primeiro
        empties.sort(Comparator.comparingInt(pos -> candidates(board, pos[0], pos[1]).size()));
        return backtrack(board, empties, 0);
    }

    private static boolean backtrack(Board board, List<int[]> empties, int idx) {
        if (idx == empties.size()) return true;
        int r = empties.get(idx)[0], c = empties.get(idx)[1];

        for (int v : candidates(board, r, c)) {
            var res = board.place(r, c, v);
            if (res.accepted()) {
                if (backtrack(board, empties, idx + 1)) return true;
                board.clear(r, c);
            }
        }
        return false;
    }

    private static List<Integer> candidates(Board board, int r, int c) {
        List<Integer> out = new ArrayList<>(9);
        for (int v = 1; v <= 9; v++) if (board.canPlace(r, c, v)) out.add(v);
        return out;
    }
}
