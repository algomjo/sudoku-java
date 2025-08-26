package br.com.dio.sudoku.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DigitFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        String newStr = filter(string, fb.getDocument().getLength());
        if (newStr != null) super.insertString(fb, offset, newStr, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String newStr = filter(text, fb.getDocument().getLength() - length);
        if (newStr != null) super.replace(fb, offset, length, newStr, attrs);
    }

    private String filter(String input, int currentLen) {
        // Permite vazio ou 1 dígito 1..9
        String s = input.replaceAll("[^0-9]", "");
        if (s.isEmpty()) return ""; // limpar
        if (currentLen >= 1) return null;
        if (s.length() > 1) s = s.substring(0,1);
        char ch = s.charAt(0);
        if (ch >= '1' && ch <= '9') return String.valueOf(ch);
        return null;
    }
}
