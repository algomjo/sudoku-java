package br.com.dio.sudoku.cli;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.MoveResult;
import br.com.dio.sudoku.view.BoardRenderer;
import br.com.dio.sudoku.demo.DemoBoards;

import java.util.Scanner;

public class MainCli {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Board board = DemoBoards.sample();
        loop(board);
    }

    private static void loop(Board board) {
        while (true) {
            System.out.println(BoardRenderer.render(board));
            System.out.println("[1] Preencher  [2] Apagar  [3] Verificar  [4] Reset  [0] Sair");
            int op = readInt("Opção: ", 0, 4);
            switch (op) {
                case 1 -> fill(board);
                case 2 -> erase(board);
                case 3 -> check(board);
                case 4 -> reset(board);
                case 0 -> { System.out.println("Até mais!"); return; }
            }
        }
    }

    private static void fill(Board board) {
        int r = readInt("Linha (0..8): ", 0, 8);
        int c = readInt("Coluna (0..8): ", 0, 8);
        int v = readInt("Valor (1..9): ", 1, 9);
        MoveResult res = board.place(r, c, v);
        if (!res.accepted()) System.out.println("Falhou: " + res.message());
    }

    private static void erase(Board board) {
        int r = readInt("Linha (0..8): ", 0, 8);
        int c = readInt("Coluna (0..8): ", 0, 8);
        board.clear(r, c);
    }

    private static void check(Board board) {
        if (board.isSolved()) {
            System.out.println("Parabéns! Tabuleiro resolvido corretamente.");
        } else {
            var conflicts = board.conflicts();
            if (conflicts.isEmpty()) System.out.println("Sem conflitos, mas ainda incompleto.");
            else {
                System.out.println("Conflitos em:");
                for (int[] p : conflicts) System.out.printf("- (%d,%d)%n", p[0], p[1]);
            }
        }
    }

    private static void reset(Board board) {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                board.clear(r, c);
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException ex) {
                System.out.printf("Informe um número entre %d e %d.%n", min, max);
            }
        }
    }
}
