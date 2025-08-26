package br.com.dio.sudoku.demo;

import br.com.dio.sudoku.io.PuzzleLoader;
import br.com.dio.sudoku.model.Board;

/** Fornece um puzzle de exemplo (solução + máscara). */
public final class DemoBoards {
    private DemoBoards() {}

    // Solução conhecida (válida)
    private static final String SOLVED =
            "534678912"+
                    "672195348"+
                    "198342567"+
                    "859761423"+
                    "426853791"+
                    "713924856"+
                    "961537284"+
                    "287419635"+
                    "345286179";

    // Máscara: 1 = fixo (dado inicial), 0 = vazio a preencher
    private static final String MASK =
            "111000000"+
                    "000111000"+
                    "000000111"+
                    "111000000"+
                    "000111000"+
                    "000000111"+
                    "111000000"+
                    "000111000"+
                    "000000111";

    public static Board sample() {
        return PuzzleLoader.fromString(SOLVED, MASK);
    }
}
