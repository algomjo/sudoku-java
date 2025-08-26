package br.com.dio.sudoku.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    public static final int SIZE = 9;

    private final Cell[][] grid;     // [linha][coluna]
    private final int[] rowMask = new int[SIZE];
    private final int[] colMask = new int[SIZE];
    private final int[] boxMask = new int[SIZE];

    public Board(List<List<Cell>> rows) {
        if (rows.size() != SIZE) throw new IllegalArgumentException("Board inválido: tamanho != 9");
        this.grid = new Cell[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            var row = rows.get(r);
            if (row.size() != SIZE) throw new IllegalArgumentException("Linha inválida: " + r);
            for (int c = 0; c < SIZE; c++) {
                this.grid[r][c] = row.get(c);
                Integer v = this.grid[r][c].getValue();
                if (v != null) setMask(r, c, v, true);
            }
        }
    }

    public Cell get(int r, int c) { return grid[r][c]; }
    public boolean isFixed(int r, int c) { return grid[r][c].isFixed(); }

    private static int boxIndex(int r, int c) { return (r / 3) * 3 + (c / 3); }
    private static int bit(int v) {
        if (v < 1 || v > 9) throw new IllegalArgumentException("Valor fora de 1..9: " + v);
        return 1 << v;
    }

    private void setMask(int r, int c, int v, boolean add) {
        int b = boxIndex(r, c);
        int m = bit(v);
        if (add) {
            rowMask[r] |= m;
            colMask[c] |= m;
            boxMask[b] |= m;
        } else {
            rowMask[r] &= ~m;
            colMask[c] &= ~m;
            boxMask[b] &= ~m;
        }
    }

    public boolean canPlace(int r, int c, int v) {
        if (isFixed(r, c)) return false;
        if (v < 1 || v > 9) return false;
        int b = boxIndex(r, c);
        int m = bit(v);
        return (rowMask[r] & m) == 0 && (colMask[c] & m) == 0 && (boxMask[b] & m) == 0;
    }

    public MoveResult place(int r, int c, int v) {
        var cell = grid[r][c];
        if (cell.isFixed()) return MoveResult.fail("Posição fixa");
        if (v < 1 || v > 9) return MoveResult.fail("Valor deve ser 1..9");
        Integer old = cell.getValue();
        if (Objects.equals(old, v)) return MoveResult.ok();
        if (old != null) setMask(r, c, old, false);

        if (!canPlace(r, c, v)) {
            if (old != null) setMask(r, c, old, true);
            return MoveResult.fail("Conflito em linha/coluna/bloco");
        }
        cell.setValue(v);
        setMask(r, c, v, true);
        return MoveResult.ok();
    }

    public void clear(int r, int c) {
        var cell = grid[r][c];
        if (cell.isFixed()) return;
        Integer old = cell.getValue();
        if (old != null) {
            setMask(r, c, old, false);
            cell.clear();
        }
    }

    public boolean isComplete() {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (grid[r][c].getValue() == null) return false;
        return true;
    }

    public boolean isSolved() {
        return isComplete() && !hasConflicts();
    }

    public boolean hasConflicts() {
        int[] rm = new int[SIZE], cm = new int[SIZE], bm = new int[SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Integer v = grid[r][c].getValue();
                if (v == null) continue;
                int m = bit(v);
                int b = boxIndex(r, c);
                if ((rm[r] & m) != 0 || (cm[c] & m) != 0 || (bm[b] & m) != 0) return true;
                rm[r] |= m; cm[c] |= m; bm[b] |= m;
            }
        }
        return false;
    }

    public List<int[]> conflicts() {
        List<int[]> out = new ArrayList<>();
        int[] rm = new int[SIZE], cm = new int[SIZE], bm = new int[SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Integer v = grid[r][c].getValue();
                if (v == null) continue;
                int m = bit(v);
                int b = boxIndex(r, c);
                if ((rm[r] & m) != 0 || (cm[c] & m) != 0 || (bm[b] & m) != 0) {
                    out.add(new int[]{r, c});
                }
                rm[r] |= m; cm[c] |= m; bm[b] |= m;
            }
        }
        return out;
    }
}
