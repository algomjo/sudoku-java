package br.com.dio.sudoku.io;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.Cell;
import java.util.ArrayList;
import java.util.List;

public final class PuzzleLoader {
    private PuzzleLoader() {}

    /**
     * @param solved   81 dígitos '1'..'9' representando a solução final
     * @param givenMask 81 chars '1'=fixo, '0'=vazio inicial
     */
    public static Board fromString(String solved, String givenMask) {
        if (solved.length()!=81 || givenMask.length()!=81)
            throw new IllegalArgumentException("Strings devem ter 81 chars");

        List<List<Cell>> rows = new ArrayList<>();
        for (int r=0;r<9;r++) {
            List<Cell> row = new ArrayList<>(9);
            for (int c=0;c<9;c++) {
                int idx = r*9+c;
                int solution = solved.charAt(idx)-'0';
                boolean fixed = givenMask.charAt(idx)=='1';
                row.add(new Cell(solution, fixed));
            }
            rows.add(row);
        }
        return new Board(rows);
    }
}
