package truco;

import javax.swing.SwingUtilities;

/**
 * Ponto de entrada da aplicação.
 * Responsabilidade única: inicializar e exibir a GUI.
 */
public class TrucoExe {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrucoGUI().setVisible(true));
    }
}
