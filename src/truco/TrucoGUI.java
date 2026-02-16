import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrucoGUI extends JFrame
{
    // GUI Components
    private JButton[][] botoesCartas = new JButton[4][3];
    private JButton[] btnTruco = new JButton[4];
    private JButton[] btnAceitar = new JButton[4];
    private JButton[] btnCorrer = new JButton[4];
    private JButton[] btnSeis = new JButton[4];
    private JButton[] btnNove = new JButton[4];
    private JButton[] btnDoze = new JButton[4];
    private JLabel[] labelsMesa = new JLabel[4];
}