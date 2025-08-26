package br.com.dio.sudoku.view;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.Cell;

public final class BoardRenderer {
    private BoardRenderer() {}

    public static String render(Board b) {
        StringBuilder sb = new StringBuilder();
        String sepBig = "+=======+=======+=======+\n";
        String sepSmall = "+-------+-------+-------+\n";

        sb.append(sepBig);
        for (int r = 0; r < Board.SIZE; r++) {
            for (int cBlock = 0; cBlock < 3; cBlock++) {
                sb.append("|");
                for (int c = cBlock * 3; c < cBlock * 3 + 3; c++) {
                    Cell s = b.get(r, c);
                    String val = s.getValue() == null ? " " : String.valueOf(s.getValue());
                    sb.append(" ").append(val).append(" ");
                    if (c < cBlock * 3 + 2) sb.append("|");
                }
            }
            sb.append("|\n");
            if (r == 2 || r == 5) sb.append(sepBig);
            else if (r < 8) sb.append(sepSmall);
        }
        sb.append(sepBig);
        return sb.toString();
    }
}
