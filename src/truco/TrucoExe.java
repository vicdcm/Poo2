package truco;

import javax.swing.SwingUtilities;

public class TrucoExe {
    public static void main(String[] args) {
        // O Swing deve ser iniciado na Event Dispatch Thread por segurança
        SwingUtilities.invokeLater(() -> {
            TrucoJogo motor = new TrucoJogo(); // Cria a lógica
            TrucoGUI gui = new TrucoGUI(motor); // Passa o motor para a GUI
            gui.setVisible(true);
        });
    }
}
