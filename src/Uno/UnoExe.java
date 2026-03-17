package Uno;

import javax.swing.SwingUtilities;

/**
 * Ponto de entrada da aplicação UNO.
 */
public class UnoExe {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UnoGUI().setVisible(true));
    }
}
