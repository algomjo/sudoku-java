package br.com.dio.sudoku;

import br.com.dio.sudoku.demo.DemoBoards;
import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.solver.SudokuSolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void solveKnownPuzzle() {
        Board b = DemoBoards.sample();
        assertFalse(b.isSolved());
        assertTrue(SudokuSolver.solve(b));
        assertTrue(b.isSolved());
    }
}
