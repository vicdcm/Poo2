package truco;

import javax.swing.SwingUtilities;


public class TrucoExe {

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new TrucoGUI().setVisible(true));
    }
}
