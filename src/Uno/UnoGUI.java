package Uno;

import framework.Carta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * GUI do UNO para 4 jogadores reais na mesma tela.
 * Layout idêntico ao TrucoGUI: painéis nas 4 bordas, mesa central.
 */
public class UnoGUI extends JFrame {

    // ─── Componentes ─────────────────────────────────────────────────────────
    private final JButton[][] botoesCartas = new JButton[4][0];  // dinâmico
    private final JPanel[]    painelCartas = new JPanel[4];
    private final JButton[]   btnComprar   = new JButton[4];
    private final JButton[]   btnUno       = new JButton[4];
    private final JButton[][] btnAcusar = new JButton[4][4];

    private JLabel lblEstado;
    private JLabel lblCorAtual;
    private JLabel lblCartaTopo;
    private JLabel lblSentido;

    // ─── Lógica ──────────────────────────────────────────────────────────────
    private UnoJogo jogo;

    // ─── Construtor ──────────────────────────────────────────────────────────

    public UnoGUI() {
        setTitle("UNO - 4 Jogadores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        jogo = new UnoJogo();

        criarPainelStatus();
        criarPainelJogo();
        atualizarInterface();

        setSize(1100, 850);
        setLocationRelativeTo(null);
    }

    // ─── Construção ──────────────────────────────────────────────────────────

    private void criarPainelStatus() {
        JPanel painel = new JPanel(new GridLayout(2, 1, 5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painel.setBackground(new Color(240, 240, 240));

        lblEstado = new JLabel("Iniciando jogo...", JLabel.CENTER);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 18));
        lblEstado.setOpaque(true);
        lblEstado.setBackground(Color.CYAN);
        lblEstado.setBorder(BorderFactory.createRaisedBevelBorder());

        JPanel infoMesa = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        infoMesa.setOpaque(true);
        infoMesa.setBackground(Color.YELLOW);
        infoMesa.setBorder(BorderFactory.createRaisedBevelBorder());

        lblCartaTopo = new JLabel("Topo: —");
        lblCartaTopo.setFont(new Font("Arial", Font.BOLD, 16));

        lblCorAtual = new JLabel("Cor: —");
        lblCorAtual.setFont(new Font("Arial", Font.BOLD, 16));

        lblSentido = new JLabel("Sentido: ↻");
        lblSentido.setFont(new Font("Arial", Font.BOLD, 16));

        infoMesa.add(lblCartaTopo);
        infoMesa.add(lblCorAtual);
        infoMesa.add(lblSentido);

        painel.add(lblEstado);
        painel.add(infoMesa);
        add(painel, BorderLayout.NORTH);
    }

    private void criarPainelJogo() {
        JPanel painelCentral = new JPanel(new BorderLayout());

        // Mesa central — apenas decorativa, a info fica no painel de status
        JPanel mesa = new JPanel(new GridBagLayout());
        mesa.setBorder(BorderFactory.createTitledBorder("Mesa"));
        mesa.setBackground(new Color(50, 120, 60));
        JLabel lblMesa = new JLabel("▶ Compre ou jogue uma carta", JLabel.CENTER);
        lblMesa.setForeground(Color.WHITE);
        lblMesa.setFont(new Font("Arial", Font.PLAIN, 14));
        mesa.add(lblMesa);
        painelCentral.add(mesa, BorderLayout.CENTER);

        painelCentral.add(criarPainelJogador(0), BorderLayout.SOUTH);
        painelCentral.add(criarPainelJogador(2), BorderLayout.NORTH);
        painelCentral.add(criarPainelJogador(1), BorderLayout.EAST);
        painelCentral.add(criarPainelJogador(3), BorderLayout.WEST);

        add(painelCentral, BorderLayout.CENTER);
    }

    private JPanel criarPainelJogador(int id) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Jogador " + id));

        // Painel de cartas (dinâmico — recriado a cada atualização)
        painelCartas[id] = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
        JScrollPane scroll = new JScrollPane(painelCartas[id],
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(0, 120));
        scroll.setBorder(null);
        painel.add(scroll, BorderLayout.CENTER);

        // Painel de ações
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));

        btnComprar[id] = criarBotao("Comprar", e -> aoComprarCarta(id));
        btnComprar[id].setBackground(new Color(180, 220, 180));

        btnUno[id] = criarBotao("UNO!", e -> aoDeclararUno(id));
        btnUno[id].setBackground(new Color(255, 80, 80));
        btnUno[id].setForeground(Color.WHITE);
        btnUno[id].setFont(new Font("Arial", Font.BOLD, 9));

        acoes.add(btnComprar[id]);
        acoes.add(btnUno[id]);

        // Botões para acusar cada um dos outros jogadores
        for (int alvo = 0; alvo < 4; alvo++) {
            if (alvo == id) continue;
            final int a = alvo;
            btnAcusar[id][alvo] = criarBotao("Acusar J" + alvo, e -> aoAcusarUno(id, a));
            btnAcusar[id][alvo].setBackground(new Color(255, 200, 100));
            acoes.add(btnAcusar[id][alvo]);
        }

        painel.add(acoes, BorderLayout.SOUTH);
        return painel;
    }

    private JButton criarBotao(String texto, ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.PLAIN, 9));
        btn.setPreferredSize(new Dimension(75, 25));
        btn.addActionListener(acao);
        return btn;
    }

    // ─── Ações ───────────────────────────────────────────────────────────────

    private void aoJogarCarta(int jogadorId, int indice) {
        CartaUno carta = (CartaUno) jogo.getJogadores()[jogadorId].getMao().get(indice);
        if (carta == null) return;

        if (carta.isCuringa()) {
            // Pede a cor ao jogador antes de confirmar a jogada
            String cor = pedirEscolhaDeCor(jogadorId);
            if (cor == null) return; // cancelou
            jogo.jogarCarta(jogadorId, indice, cor);
        } else {
            if (!jogo.jogarCarta(jogadorId, indice, null)) {
                JOptionPane.showMessageDialog(this,
                        "Carta inválida! Jogue uma carta da mesma cor ou mesmo valor.",
                        "Jogada inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        atualizarInterface();
        if (jogo.jogoAcabou()) exibirFimDeJogo();
    }

    private void aoComprarCarta(int jogadorId) {
        CartaUno carta = jogo.comprarCarta(jogadorId);
        if (carta == null) {
            JOptionPane.showMessageDialog(this,
                    "Não é sua vez ou o baralho está vazio.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Jogador " + jogadorId + " comprou: " + carta,
                "Carta comprada", JOptionPane.INFORMATION_MESSAGE);
        atualizarInterface();
    }

    private void aoDeclararUno(int jogadorId) {
        boolean ok = jogo.declararUno(jogadorId);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Jogador " + jogadorId + " disse UNO!",
                    "UNO!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "UNO inválido! Você precisa ter exatamente 1 carta.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        atualizarInterface();
    }

    private void aoAcusarUno(int acusadorId, int acusadoId) {
        boolean ok = jogo.acusarUno(acusadorId, acusadoId);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Jogador " + acusadoId + " esqueceu de dizer UNO e comprou "
                    + Constants.PENALIDADE_UNO + " cartas!",
                    "Acusação válida!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Acusação inválida. Jogador " + acusadoId + " não precisa declarar UNO.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        atualizarInterface();
    }

    private String pedirEscolhaDeCor(int jogadorId) {
        String[] cores = Constants.CORES_NORMAIS;
        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Jogador " + jogadorId + ", escolha a nova cor:",
                "Curinga — escolha a cor",
                JOptionPane.QUESTION_MESSAGE,
                null, cores, cores[0]);
        return escolha;
    }

    // ─── Atualização da interface ─────────────────────────────────────────────

    private void atualizarInterface() {
        int atual     = jogo.getJogadorAtual();
        boolean aguardando = jogo.isAguardandoEscolhaCor();
        JogadorUno[] jogadores = jogo.getJogadores();

        // Recria os botões de carta para cada jogador
        for (int j = 0; j < 4; j++) {
            painelCartas[j].removeAll();
            List<Carta> cartas = jogadores[j].getMao().getCartas();
            final int jj = j;

            for (int i = 0; i < cartas.size(); i++) {
                CartaUno carta = (CartaUno) cartas.get(i);
                final int idx = i;
                JButton btn = new JButton("<html><center>" +
                        carta.toString().replace(" ", "<br>") + "</center></html>");
                btn.setPreferredSize(new Dimension(72, 90));
                btn.setFont(new Font("Arial", Font.BOLD, 10));
                btn.setBackground(corDaCarta(carta));
                btn.setForeground(Color.WHITE);

                boolean minhaVez = (jj == atual) && !aguardando;
                boolean podeJogar = minhaVez && jogo.podeJogar(carta);
                btn.setEnabled(podeJogar);
                btn.addActionListener(e -> aoJogarCarta(jj, idx));
                painelCartas[j].add(btn);
            }
            painelCartas[j].revalidate();
            painelCartas[j].repaint();

            // Botões de ação
            btnComprar[j].setEnabled(j == atual && !aguardando);
            btnUno[j].setEnabled(jogadores[j].getMao().tamanho() == 1
                    && !jogadores[j].isDeclarouUno());

            for (int alvo = 0; alvo < 4; alvo++) {
                if (alvo == j || btnAcusar[j][alvo] == null) continue;
                btnAcusar[j][alvo].setEnabled(jogo.isPrecisaDeclarar(alvo));
            }
        }

        // Labels de status
        CartaUno topo = jogo.getCartaTopo();
        lblCartaTopo.setText("Topo: " + topo);
        lblCorAtual.setText("Cor: " + jogo.getCorAtual());
        lblSentido.setText("Sentido: " + (jogo.isSentidoHorario() ? "↻ Horário" : "↺ Anti-horário"));

        String estado = "Vez do Jogador " + atual
                + "  |  Cartas na mão: " + jogadores[atual].getMao().tamanho();
        if (aguardando) estado += "  —  Escolha a cor do curinga!";
        lblEstado.setText(estado);

        // Cor de fundo do label de cor
        lblCorAtual.setBackground(corDoLabel(jogo.getCorAtual()));
        lblCorAtual.setOpaque(true);
    }

    // ─── Auxiliares ──────────────────────────────────────────────────────────

    private Color corDaCarta(CartaUno carta) {
        return switch (carta.getCor()) {
            case Constants.VERMELHO -> new Color(200, 50, 50);
            case Constants.AZUL     -> new Color(50, 100, 200);
            case Constants.VERDE    -> new Color(40, 150, 60);
            case Constants.AMARELO  -> new Color(200, 170, 0);
            default                 -> new Color(60, 60, 60);   // Preto/curinga
        };
    }

    private Color corDoLabel(String cor) {
        return switch (cor) {
            case Constants.VERMELHO -> new Color(255, 150, 150);
            case Constants.AZUL     -> new Color(150, 180, 255);
            case Constants.VERDE    -> new Color(150, 230, 150);
            case Constants.AMARELO  -> new Color(255, 240, 100);
            default                 -> Color.LIGHT_GRAY;
        };
    }

    void exibirFimDeJogo() {
        int v = jogo.getVencedor();
        JOptionPane.showMessageDialog(this,
                "Jogador " + v + " venceu o jogo!",
                "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja jogar novamente?", "Nova Partida",
                JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            jogo = new UnoJogo();
            atualizarInterface();
        } else {
            System.exit(0);
        }
    }
}