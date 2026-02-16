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

    private void criarPainelPontos() {
        JPanel painel = new JPanel(new GridLayout(2, 1, 5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painel.setBackground(new Color(240, 240, 240));

        lblEstado = new JLabel("Iniciando jogo...", JLabel.CENTER);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 18));
        lblEstado.setOpaque(true);
        lblEstado.setBackground(Color.CYAN);
        lblEstado.setBorder(BorderFactory.createRaisedBevelBorder());

        lblPontuacao = new JLabel("Dupla 0: 0   |   Dupla 1: 0", JLabel.CENTER);
        lblPontuacao.setFont(new Font("Arial", Font.BOLD, 16));
        lblPontuacao.setOpaque(true);
        lblPontuacao.setBackground(Color.YELLOW);
        lblPontuacao.setBorder(BorderFactory.createRaisedBevelBorder());

        painel.add(lblEstado);
        painel.add(lblPontuacao);
        add(painel, BorderLayout.NORTH);
    }

    private void criarPainelJogo() {
        JPanel painelCentral = new JPanel(new BorderLayout());

        JPanel painelMesa = new JPanel(new GridLayout(2, 2, 15, 15));
        painelMesa.setBorder(BorderFactory.createTitledBorder("Mesa de Jogo"));
        for (int i = 0; i < 4; i++) {
            labelsMesa[i] = new JLabel("Jogador " + i, JLabel.CENTER);
            labelsMesa[i].setOpaque(true);
            labelsMesa[i].setBackground(Color.WHITE);
            labelsMesa[i].setBorder(BorderFactory.createEtchedBorder());
            labelsMesa[i].setFont(new Font("Arial", Font.BOLD, 16));
            painelMesa.add(labelsMesa[i]);
        }
        painelCentral.add(painelMesa, BorderLayout.CENTER);

        painelCentral.add(criarPainelJogador(0), BorderLayout.SOUTH);
        painelCentral.add(criarPainelJogador(2), BorderLayout.NORTH);
        painelCentral.add(criarPainelJogador(1), BorderLayout.EAST);
        painelCentral.add(criarPainelJogador(3), BorderLayout.WEST);

        add(painelCentral, BorderLayout.CENTER);
    }

    private JPanel criarPainelJogador(int jogadorId) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Jogador " + jogadorId));

        JPanel painelCartas = new JPanel(new FlowLayout());
        for (int i = 0; i < 3; i++) {
            JButton btn = new JButton("Carta " + (i + 1));
            btn.setPreferredSize(new Dimension(90, 100));
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            int idx = i;
            btn.addActionListener(e -> jogarCarta(jogadorId, idx));
            botoesCartas[jogadorId][i] = btn;
            painelCartas.add(btn);
        }

        JPanel painelTruco = new JPanel(new FlowLayout());
        btnTruco[jogadorId] = criarBotao("Truco", e -> pedirTruco(jogadorId));
        btnAceitar[jogadorId] = criarBotao("Aceitar", e -> aceitarTruco(jogadorId));
        btnCorrer[jogadorId] = criarBotao("Correr", e -> correrTruco(jogadorId));
        btnSeis[jogadorId] = criarBotao("Seis", e -> aumentarTruco(jogadorId, Constants.TRUCO_SEIS, 6));
        btnNove[jogadorId] = criarBotao("Nove", e -> aumentarTruco(jogadorId, Constants.TRUCO_NOVE, 9));
        btnDoze[jogadorId] = criarBotao("Doze", e -> aumentarTruco(jogadorId, Constants.TRUCO_DOZE, 12));

        painelTruco.add(btnTruco[jogadorId]);
        painelTruco.add(btnAceitar[jogadorId]);
        painelTruco.add(btnCorrer[jogadorId]);
        painelTruco.add(btnSeis[jogadorId]);
        painelTruco.add(btnNove[jogadorId]);
        painelTruco.add(btnDoze[jogadorId]);

        painel.add(painelCartas, BorderLayout.NORTH);
        painel.add(painelTruco, BorderLayout.SOUTH);
        return painel;
    }

    private JButton criarBotao(String texto, ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.PLAIN, 9));
        btn.setPreferredSize(new Dimension(60, 25));
        btn.addActionListener(acao);
        return btn;
    }

     private void atualizarInterface() {
        for (int j = 0; j < 4; j++) {
            Mao mao = jogadores[j].getMao();
            for (int i = 0; i < 3; i++) {
                if (i < mao.tamanho()) {
                    botoesCartas[j][i].setText(mao.get(i).toString());
                    botoesCartas[j][i].setEnabled(j == jogadorAtual && !aguardandoRespostaTruco);
                } else {
                    botoesCartas[j][i].setText("");
                    botoesCartas[j][i].setEnabled(false);
                }
            }
            atualizarBotoesTruco(j);
        }

        String estado = "Rodada " + (rodadaAtual + 1) + " — Vez do Jogador " + jogadorAtual;
        if (aguardandoRespostaTruco) {
            int duplaResponde = 1 - jogadores[jogadorQuePediuTruco].getDuplaId();
            estado += " — Dupla " + duplaResponde + " responde ao TRUCO!";
        }
        lblEstado.setText(estado);
        lblPontuacao.setText(String.format("Dupla 0: %d   |   Dupla 1: %d",
                duplas[0].getPontuacao(), duplas[1].getPontuacao()));
    }

    private void atualizarBotoesTruco(int jogadorId) {
        int minhaDupla = jogadores[jogadorId].getDuplaId();
        boolean possoTrucarPrimeiraVez = (estadoTruco == Constants.TRUCO_NAO_TRUCADO) &&
                !aguardandoRespostaTruco &&
                cartasJogadasNaRodada < 2;
        boolean souDuplaAdversaria = aguardandoRespostaTruco &&
                minhaDupla != jogadores[jogadorQuePediuTruco].getDuplaId();
        boolean possoAumentar = !aguardandoRespostaTruco &&
                minhaDupla == duplaQuePodeAumentar;

        btnTruco[jogadorId].setEnabled(possoTrucarPrimeiraVez);
        btnAceitar[jogadorId].setEnabled(souDuplaAdversaria);
        btnCorrer[jogadorId].setEnabled(souDuplaAdversaria);
        btnSeis[jogadorId].setEnabled(possoAumentar && estadoTruco == Constants.TRUCO_TRUCADO);
        btnNove[jogadorId].setEnabled(possoAumentar && estadoTruco == Constants.TRUCO_SEIS);
        btnDoze[jogadorId].setEnabled(possoAumentar && estadoTruco == Constants.TRUCO_NOVE);
    }



}