package br.com.dio.sudoku.model;

public class Cell {
    private Integer value;   // null = vazio (digitado/jogado)
    private final int solution; // valor da solução (1..9)
    private final boolean fixed; // se o valor é fixo (dica inicial)

    public Cell(int solution, boolean fixed) {
        this.solution = solution;
        this.fixed = fixed;
        if (fixed) this.value = solution;
    }

    public Integer getValue() { return value; }
    public void setValue(Integer v) { this.value = v; }
    public void clear() { this.value = null; }

    public int getSolution() { return solution; }
    public boolean isFixed() { return fixed; }

    public boolean isEmpty() { return value == null; }
}
