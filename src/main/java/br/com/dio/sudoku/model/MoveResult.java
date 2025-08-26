package br.com.dio.sudoku.model;

public final class MoveResult {
    private final boolean accepted;
    private final String message;

    private MoveResult(boolean accepted, String message) {
        this.accepted = accepted;
        this.message = message;
    }

    public static MoveResult ok() { return new MoveResult(true, "OK"); }
    public static MoveResult fail(String msg) { return new MoveResult(false, msg); }

    public boolean accepted() { return accepted; }
    public String message() { return message; }
}
