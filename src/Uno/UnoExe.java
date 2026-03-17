package Uno;

import javax.swing.SwingUtilities;


public class UnoExe {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UnoGUI().setVisible(true));
    }
}
