package truco;

import framework.Carta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * GUI fiel ao JogoTruco original.
 * Quatro jogadores reais compartilham a mesma tela.
 * Toda a lógica do jogo é delegada a TrucoJogo.
 */
public class TrucoGUI extends JFrame {

    // ─── Componentes da interface ─────────────────────────────────────────────
    private final JButton[][] botoesCartas = new JButton[4][3];
    private final JButton[]   btnTruco     = new JButton[4];
    private final JButton[]   btnAceitar   = new JButton[4];
    private final JButton[]   btnCorrer    = new JButton[4];
    private final JButton[]   btnSeis      = new JButton[4];
    private final JButton[]   btnNove      = new JButton[4];
    private final JButton[]   btnDoze      = new JButton[4];
    private final JLabel[]    labelsMesa   = new JLabel[4];

    private JLabel lblEstado;
    private JLabel lblPontuacao;

    // ─── Lógica do jogo ───────────────────────────────────────────────────────
    private TrucoJogo jogo;

    // ─── Construtor ──────────────────────────────────────────────────────────

    public TrucoGUI() {
        setTitle("Truco - 4 Jogadores Reais");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        jogo = new TrucoJogo();

        criarPainelPontos();
        criarPainelJogo();
        atualizarInterface();

        setSize(1000, 800);
        setLocationRelativeTo(null);
    }

    // ─── Construção da interface ──────────────────────────────────────────────

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
            final int indice = i;
            btn.addActionListener(e -> aoJogarCarta(jogadorId, indice));
            botoesCartas[jogadorId][i] = btn;
            painelCartas.add(btn);
        }

        JPanel painelTruco = new JPanel(new FlowLayout());
        btnTruco[jogadorId]   = criarBotao("Truco",   e -> aoPedirTruco(jogadorId));
        btnAceitar[jogadorId] = criarBotao("Aceitar", e -> aoAceitarTruco(jogadorId));
        btnCorrer[jogadorId]  = criarBotao("Correr",  e -> aoCorrerTruco(jogadorId));
        btnSeis[jogadorId]    = criarBotao("Seis",    e -> aoAumentarTruco(jogadorId));
        btnNove[jogadorId]    = criarBotao("Nove",    e -> aoAumentarTruco(jogadorId));
        btnDoze[jogadorId]    = criarBotao("Doze",    e -> aoAumentarTruco(jogadorId));

        painelTruco.add(btnTruco[jogadorId]);
        painelTruco.add(btnAceitar[jogadorId]);
        painelTruco.add(btnCorrer[jogadorId]);
        painelTruco.add(btnSeis[jogadorId]);
        painelTruco.add(btnNove[jogadorId]);
        painelTruco.add(btnDoze[jogadorId]);

        painel.add(painelCartas, BorderLayout.NORTH);
        painel.add(painelTruco,  BorderLayout.SOUTH);
        return painel;
    }

    private JButton criarBotao(String texto, ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.PLAIN, 9));
        btn.setPreferredSize(new Dimension(60, 25));
        btn.addActionListener(acao);
        return btn;
    }

    // ─── Ações dos jogadores ──────────────────────────────────────────────────

    private void aoJogarCarta(int jogadorId, int indice) {
        // Captura a carta antes de removê-la via TrucoJogo
        Carta carta = jogo.getJogadores()[jogadorId].getMao().get(indice);
        if (carta == null) return;

        int rodadaAntes = jogo.getRodadaAtual();
        boolean jogou   = jogo.jogarCarta(jogadorId, indice);
        if (!jogou) return;

        // Mostra a carta jogada na mesa
        labelsMesa[jogadorId].setText("J" + jogadorId + ": " + carta);
        labelsMesa[jogadorId].setBackground(new Color(220, 220, 255));

        int rodadaDepois = jogo.getRodadaAtual();

        if (jogo.jogoAcabou()) {
            atualizarInterface();
            exibirFimDeJogo();
            return;
        }

        // Rodada concluída e nova mão iniciada (rodada voltou a 0 e cartas redistribuídas)
        if (rodadaDepois == 0 && rodadaAntes != 0) {
            JOptionPane.showMessageDialog(this, montarMensagemFimMao());
            limparMesa();
        }
        // Rodada concluída mas mão continua (rodada avançou)
        else if (rodadaDepois > rodadaAntes) {
            limparMesa();
        }

        atualizarInterface();
    }

    private void aoPedirTruco(int jogadorId) {
        jogo.pedirTruco(jogadorId);
        atualizarInterface();
    }

    private void aoAceitarTruco(int jogadorId) {
        jogo.aceitarTruco(jogadorId);
        atualizarInterface();
    }

    private void aoAumentarTruco(int jogadorId) {
        jogo.aumentarTruco(jogadorId);
        atualizarInterface();
    }

    // ─── Atualização da interface ─────────────────────────────────────────────

    private void atualizarInterface() {
        JogadorTruco[] jogadores = jogo.getJogadores();
        int  jogadorAtual = jogo.getJogadorAtual();
        boolean aguardando = jogo.isAguardandoResposta();

        for (int j = 0; j < 4; j++) {
            framework.Mao mao = jogadores[j].getMao();
            for (int i = 0; i < 3; i++) {
                if (i < mao.tamanho()) {
                    botoesCartas[j][i].setText(mao.get(i).toString());
                    botoesCartas[j][i].setEnabled(j == jogadorAtual && !aguardando);
                } else {
                    botoesCartas[j][i].setText("");
                    botoesCartas[j][i].setEnabled(false);
                }
            }
            atualizarBotoesTruco(j);
        }

        String estado = "Rodada " + (jogo.getRodadaAtual() + 1)
                + " — Vez do Jogador " + jogadorAtual;
        if (aguardando) {
            int duplaResponde = 1 - jogadores[jogo.getJogadorQuePediuTruco()].getDuplaId();
            estado += " — Dupla " + duplaResponde + " responde ao TRUCO!";
        }
        lblEstado.setText(estado);

        Dupla[] duplas = jogo.getDuplas();
        lblPontuacao.setText(String.format("Dupla 0: %d   |   Dupla 1: %d",
                duplas[0].getPontuacao(), duplas[1].getPontuacao()));
    }

    private void atualizarBotoesTruco(int jogadorId) {
        int  minhaDupla = jogo.getJogadores()[jogadorId].getDuplaId();
        boolean aguardando = jogo.isAguardandoResposta();
        int  estado     = jogo.getEstadoTruco();
        int  pediu      = jogo.getJogadorQuePediuTruco();

        boolean possoTrucarPrimeiraVez =
                estado == Constants.TRUCO_NAO_TRUCADO &&
                !aguardando &&
                jogo.getCartasJogadasNaRodada() < 2;

        boolean souAdversario =
                aguardando &&
                minhaDupla != jogo.getJogadores()[pediu].getDuplaId();

        boolean possoAumentar =
                !aguardando &&
                estado != Constants.TRUCO_NAO_TRUCADO &&
                estado != Constants.TRUCO_DOZE &&
                minhaDupla == jogo.getDuplaQuePodeAumentar();

        btnTruco[jogadorId].setEnabled(possoTrucarPrimeiraVez);
        btnAceitar[jogadorId].setEnabled(souAdversario);
        btnCorrer[jogadorId].setEnabled(souAdversario);
        btnSeis[jogadorId].setEnabled(possoAumentar && estado == Constants.TRUCO_TRUCADO);
        btnNove[jogadorId].setEnabled(possoAumentar && estado == Constants.TRUCO_SEIS);
        btnDoze[jogadorId].setEnabled(possoAumentar && estado == Constants.TRUCO_NOVE);
    }

    // ─── Auxiliares de apresentação ──────────────────────────────────────────

    private void limparMesa() {
        for (int i = 0; i < 4; i++) {
            labelsMesa[i].setText("Jogador " + i);
            labelsMesa[i].setBackground(Color.WHITE);
        }
    }

    private String montarMensagemFimMao() {
        Dupla[] duplas = jogo.getDuplas();
        return String.format("Mão encerrada!%nDupla 0: %d pts  |  Dupla 1: %d pts",
                duplas[0].getPontuacao(), duplas[1].getPontuacao());
    }

    void exibirFimDeJogo() {
        int venc = jogo.getDuplaVencedora();
        JOptionPane.showMessageDialog(this, "Dupla " + venc + " VENCEU O JOGO!");

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja jogar novamente?", "Fim de Jogo",
                JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            jogo = new TrucoJogo();
            limparMesa();
            atualizarInterface();
        } else {
            System.exit(0);
        }
    }

    private void aoCorrerTruco(int jogadorId) {
        // pontosAoCorre() consultado ANTES de correrTruco() alterar o estado
        int duplaPediu = jogo.getJogadores()[jogo.getJogadorQuePediuTruco()].getDuplaId();
        int pontos     = jogo.getPontosAoCorre();

        jogo.correrTruco(jogadorId);

        JOptionPane.showMessageDialog(this,
                "Dupla " + duplaPediu + " ganhou " + pontos + " ponto(s)!");
        limparMesa();
        atualizarInterface();
        if (jogo.jogoAcabou()) exibirFimDeJogo();
    }
}