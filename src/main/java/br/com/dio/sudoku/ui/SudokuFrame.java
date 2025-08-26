package br.com.dio.sudoku.ui;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.demo.DemoBoards;
import br.com.dio.sudoku.model.MoveResult;
import br.com.dio.sudoku.solver.SudokuSolver;
import br.com.dio.sudoku.util.DigitFilter;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SudokuFrame extends JFrame {
    private final JTextField[][] fields = new JTextField[9][9];
    private Board board;

    public SudokuFrame(Board board) {
        super("Sudoku");
        this.board = board;
        buildUi();
        syncFromBoard();
    }

    private void buildUi() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JPanel grid = new JPanel(new GridLayout(9,9));
        grid.setBorder(new MatteBorder(2,2,2,2, Color.DARK_GRAY));

        for (int r=0;r<9;r++) for (int c=0;c<9;c++) {
            JTextField tf = new JTextField();
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setFont(tf.getFont().deriveFont(Font.BOLD, 18f));
            ((AbstractDocument)tf.getDocument()).setDocumentFilter(new DigitFilter());

            // Bordas grossas nos blocos 3x3
            int top = (r % 3 == 0) ? 2 : 1;
            int left = (c % 3 == 0) ? 2 : 1;
            int bottom = (r == 8) ? 2 : ((r % 3 == 2) ? 2 : 1);
            int right = (c == 8) ? 2 : ((c % 3 == 2) ? 2 : 1);
            tf.setBorder(new MatteBorder(top,left,bottom,right, Color.GRAY));

            int rr = r, cc = c;
            tf.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) { onChange(rr,cc,tf); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { onChange(rr,cc,tf); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { onChange(rr,cc,tf); }
            });

            fields[r][c] = tf;
            grid.add(tf);
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnCheck = new JButton("Verificar");
        JButton btnHint = new JButton("Dica");
        JButton btnSolve = new JButton("Resolver");
        JButton btnReset = new JButton("Reset");
        JButton btnNew = new JButton("Novo");

        btnCheck.addActionListener(e -> {
            if (board.isSolved()) JOptionPane.showMessageDialog(this, "Resolvido corretamente!");
            else JOptionPane.showMessageDialog(this, "Ainda não. Corrija conflitos/complete.");
            highlightConflicts();
        });
        btnHint.addActionListener(e -> giveHint());
        btnSolve.addActionListener(e -> {
            if (SudokuSolver.solve(board)) {
                syncFromBoard();
                highlightConflicts();
                JOptionPane.showMessageDialog(this, "Resolvido!");
            } else {
                JOptionPane.showMessageDialog(this, "Sem solução.");
            }
        });
        btnReset.addActionListener(e -> {
            for (int r=0;r<9;r++) for (int c=0;c<9;c++) board.clear(r,c);
            syncFromBoard();
            highlightConflicts();
        });
        btnNew.addActionListener(e -> {
            this.board = DemoBoards.sample();
            syncFromBoard();
            highlightConflicts();
        });

        buttons.add(btnCheck); buttons.add(btnHint); buttons.add(btnSolve);
        buttons.add(btnReset); buttons.add(btnNew);

        add(grid, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        setSize(520, 580);
        setLocationRelativeTo(null);
    }

    private void onChange(int r, int c, JTextField tf) {
        if (board.isFixed(r,c)) {
            SwingUtilities.invokeLater(() -> tf.setText(String.valueOf(board.get(r,c).getValue())));
            return;
        }
        String t = tf.getText().trim();
        if (t.isEmpty()) { board.clear(r,c); highlightConflicts(); return; }
        int v = t.charAt(0)-'0';
        MoveResult res = board.place(r,c,v);
        if (!res.accepted()) {
            // Reverte UI
            SwingUtilities.invokeLater(() -> tf.setText(""));
            JOptionPane.showMessageDialog(this, res.message(), "Jogada inválida", JOptionPane.WARNING_MESSAGE);
        }
        highlightConflicts();
    }

    private void syncFromBoard() {
        for (int r=0;r<9;r++) for (int c=0;c<9;c++) {
            var cell = board.get(r,c);
            JTextField tf = fields[r][c];
            ((AbstractDocument)tf.getDocument()).setDocumentFilter(null); // evita loops
            tf.setText(cell.getValue()==null ? "" : String.valueOf(cell.getValue()));
            tf.setEditable(!cell.isFixed());
            tf.setBackground(cell.isFixed() ? new Color(235, 238, 245) : Color.WHITE);
            ((AbstractDocument)tf.getDocument()).setDocumentFilter(new DigitFilter());
        }
    }

    private void highlightConflicts() {
        Set<String> conf = new HashSet<>();
        for (int[] p : board.conflicts()) conf.add(p[0]+"-"+p[1]);
        for (int r=0;r<9;r++) for (int c=0;c<9;c++) {
            JTextField tf = fields[r][c];
            boolean fixed = board.isFixed(r,c);
            if (conf.contains(r+"-"+c)) {
                tf.setBackground(new Color(255, 220, 220));
            } else {
                tf.setBackground(fixed ? new Color(235,238,245) : Color.WHITE);
            }
        }
    }

    private void giveHint() {
        // Escolhe a primeira célula editável com um candidato válido (usa solution como dica)
        for (int r=0;r<9;r++) for (int c=0;c<9;c++) {
            if (!board.isFixed(r,c) && board.get(r,c).getValue()==null) {
                int sol = board.get(r,c).getSolution();
                if (board.canPlace(r,c,sol)) {
                    var res = board.place(r,c,sol);
                    if (res.accepted()) {
                        fields[r][c].setText(String.valueOf(sol));
                        highlightConflicts();
                        return;
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Sem dicas disponíveis agora.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuFrame(DemoBoards.sample()).setVisible(true));
    }
}
