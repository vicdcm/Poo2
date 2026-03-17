// JogoTruco.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JogoTruco extends JFrame {

    // GUI Components
    private JButton[][] botoesCartas = new JButton[4][3];
    private JButton[] btnTruco = new JButton[4];
    private JButton[] btnAceitar = new JButton[4];
    private JButton[] btnCorrer = new JButton[4];
    private JButton[] btnSeis = new JButton[4];
    private JButton[] btnNove = new JButton[4];
    private JButton[] btnDoze = new JButton[4];
    private JLabel[] labelsMesa = new JLabel[4];

    // Painel de Pontos
    private JLabel lblEstado;
    private JLabel lblPontuacao;

    // Game State
    private Jogador[] jogadores;
    private Dupla[] duplas;
    private Carta[] cartasNaMesa;
    private int jogadorAtual;
    private int estadoTruco;
    private int pontuacaoAtual;
    private boolean aguardandoRespostaTruco;
    private int jogadorQuePediuTruco;
    private int rodadaAtual;
    private int[] placarParcial = new int[2];
    private int cartasJogadasNaRodada;
    private int duplaQuePodeAumentar = -1; // -1 = ninguém; 0 ou 1 = dupla com direito de aumentar

    public JogoTruco() {
        setTitle("Truco - 4 Jogadores Reais");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarJogadoresEDuplas();
        cartasNaMesa = new Carta[4];
        jogadorAtual = 0;
        estadoTruco = Constants.TRUCO_NAO_TRUCADO;
        pontuacaoAtual = 1;
        aguardandoRespostaTruco = false;
        rodadaAtual = 0;
        cartasJogadasNaRodada = 0;
        duplaQuePodeAumentar = -1;

        criarPainelPontos();
        criarPainelJogo();
        distribuirCartas();
        atualizarInterface();

        setSize(1000, 800);
        setLocationRelativeTo(null);
    }

    private void inicializarJogadoresEDuplas() {
        jogadores = new Jogador[4];
        for (int i = 0; i < 4; i++) {
            jogadores[i] = new Jogador(i, i % 2);
        }
        duplas = new Dupla[]{new Dupla(0), new Dupla(1)};
    }

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

    private void distribuirCartas() {
        Baralho baralho = new Baralho();
        baralho.embaralhar();
        for (Jogador j : jogadores) j.getMao().getCartas().clear();
        for (int carta = 0; carta < 3; carta++) {
            for (Jogador j : jogadores) {
                Carta c = baralho.comprarCarta();
                if (c != null) j.getMao().adicionar(c);
            }
        }
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

    private void jogarCarta(int jogadorId, int indice) {
        if (jogadorId != jogadorAtual || aguardandoRespostaTruco) return;

        Mao mao = jogadores[jogadorId].getMao();
        if (indice >= mao.tamanho()) return;

        Carta carta = mao.remover(indice);
        cartasNaMesa[jogadorId] = carta;
        labelsMesa[jogadorId].setText("J" + jogadorId + ": " + carta.toString());
        labelsMesa[jogadorId].setBackground(new Color(220, 220, 255));

        cartasJogadasNaRodada++;
        jogadorAtual = (jogadorAtual + 1) % 4;

        if (cartasJogadasNaRodada == 4) {
            int vencedor = determinarVencedorRodada();
            int duplaVencedora = jogadores[vencedor].getDuplaId();
            placarParcial[duplaVencedora]++;

            if (placarParcial[0] == 2 || placarParcial[1] == 2 ||
                    (rodadaAtual == 2 && placarParcial[0] != placarParcial[1])) {

                int duplaMao = placarParcial[0] > placarParcial[1] ? 0 : 1;
                duplas[duplaMao].adicionarPontos(pontuacaoAtual);

                if (duplas[0].getPontuacao() >= Constants.PONTOS_VITORIA ||
                        duplas[1].getPontuacao() >= Constants.PONTOS_VITORIA) {
                    JOptionPane.showMessageDialog(this,
                            (duplas[0].getPontuacao() >= Constants.PONTOS_VITORIA ? "Dupla 0" : "Dupla 1") +
                                    " VENCEU O JOGO!");
                    System.exit(0);
                }

                JOptionPane.showMessageDialog(this, "Mão terminada! Dupla " + duplaMao + " ganhou.");
                reiniciarMao();
            } else {
                // ✅ LIMPAR A MESA AO FINAL DA VAZA
                for (int i = 0; i < 4; i++) {
                    labelsMesa[i].setText("Jogador " + i);
                    labelsMesa[i].setBackground(Color.WHITE);
                }
                rodadaAtual++;
                jogadorAtual = vencedor;
                cartasJogadasNaRodada = 0;
            }
        }
        atualizarInterface();
    }

    private int determinarVencedorRodada() {
        int vencedor = 0;
        int forcaMax = cartasNaMesa[0].getForca();
        for (int i = 1; i < 4; i++) {
            int forca = cartasNaMesa[i].getForca();
            if (forca > forcaMax) {
                forcaMax = forca;
                vencedor = i;
            }
        }
        return vencedor;
    }

    // === AÇÕES DE TRUCO ===

    private void pedirTruco(int jogadorId) {
        if (aguardandoRespostaTruco || estadoTruco >= Constants.TRUCO_DOZE || cartasJogadasNaRodada >= 2) {
            return;
        }
        if (estadoTruco == Constants.TRUCO_NAO_TRUCADO) {
            estadoTruco = Constants.TRUCO_TRUCADO;
            pontuacaoAtual = 3;
            jogadorQuePediuTruco = jogadorId;
            aguardandoRespostaTruco = true;
            duplaQuePodeAumentar = jogadores[jogadorId].getDuplaId(); // quem pediu primeiro
            atualizarInterface();
        }
    }

    private void aceitarTruco(int jogadorId) {
        if (!ehDuplaAdversaria(jogadorId)) return;
        aguardandoRespostaTruco = false;
        duplaQuePodeAumentar = jogadores[jogadorId].getDuplaId(); // quem aceitou pode aumentar
        atualizarInterface();
    }

    private void correrTruco(int jogadorId) {
        if (!ehDuplaAdversaria(jogadorId)) return;
        int duplaPediu = jogadores[jogadorQuePediuTruco].getDuplaId();
        int pontos;
        switch (estadoTruco) {
            case Constants.TRUCO_NAO_TRUCADO:
            case Constants.TRUCO_TRUCADO:
                pontos = 1;
                break;
            case Constants.TRUCO_SEIS:
                pontos = 3;
                break;
            case Constants.TRUCO_NOVE:
                pontos = 6;
                break;
            case Constants.TRUCO_DOZE:
                pontos = 9;
                break;
            default:
                pontos = 1;
        }
        duplas[duplaPediu].adicionarPontos(pontos);
        JOptionPane.showMessageDialog(this, "Dupla " + duplaPediu + " ganhou " + pontos + " ponto(s)!");
        reiniciarMao();
    }

    private void aumentarTruco(int jogadorId, int novoEstado, int novaPontuacao) {
        if (aguardandoRespostaTruco) return;

        int estadoEsperado = -1;
        if (novoEstado == Constants.TRUCO_SEIS) estadoEsperado = Constants.TRUCO_TRUCADO;
        else if (novoEstado == Constants.TRUCO_NOVE) estadoEsperado = Constants.TRUCO_SEIS;
        else if (novoEstado == Constants.TRUCO_DOZE) estadoEsperado = Constants.TRUCO_NOVE;
        else return;

        if (estadoTruco != estadoEsperado) return;
        if (jogadores[jogadorId].getDuplaId() != duplaQuePodeAumentar) return;

        estadoTruco = novoEstado;
        pontuacaoAtual = novaPontuacao;
        jogadorQuePediuTruco = jogadorId;
        aguardandoRespostaTruco = true;
        duplaQuePodeAumentar = 1 - jogadores[jogadorId].getDuplaId(); // passa para a outra dupla
        atualizarInterface();
    }

    private boolean ehDuplaAdversaria(int jogadorId) {
        return aguardandoRespostaTruco &&
                jogadores[jogadorId].getDuplaId() != jogadores[jogadorQuePediuTruco].getDuplaId();
    }

    private void reiniciarMao() {
        for (int i = 0; i < 4; i++) {
            cartasNaMesa[i] = null;
            labelsMesa[i].setText("Jogador " + i);
            labelsMesa[i].setBackground(Color.WHITE);
        }
        estadoTruco = Constants.TRUCO_NAO_TRUCADO;
        pontuacaoAtual = 1;
        aguardandoRespostaTruco = false;
        rodadaAtual = 0;
        cartasJogadasNaRodada = 0;
        placarParcial[0] = placarParcial[1] = 0;
        duplaQuePodeAumentar = -1;
        jogadorAtual = (jogadorQuePediuTruco + 1) % 4;
        distribuirCartas();
        atualizarInterface();
    }

    public static void main(String[] args) {
            new JogoTruco().setVisible(true);
    }
}