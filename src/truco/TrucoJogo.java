package truco;

import framework.Carta;
import framework.Jogador;
import java.util.Arrays;

public class TrucoJogo {

    private JogadorTruco[] jogadores;
    private Dupla[] duplas;
    private Carta[] cartasNaMesa;

    private int jogadorAtual;
    private int jogadorQuePediuTruco;

    private int estadoTruco;
    private int pontuacaoAtual;

    private boolean aguardandoRespostaTruco;

    private int rodadaAtual;
    private int cartasJogadasNaRodada;

    private int duplaQuePodeAumentar;
    private int[] placarParcial;

    public TrucoJogo() {
        jogadores = new JogadorTruco[4];
        duplas = new Dupla[2];
        cartasNaMesa = new Carta[4];
        placarParcial = new int[2];

        inicializarJogo();
    }

    private void inicializarJogo() {
        for (int i = 0; i < 4; i++) {
            jogadores[i] = new JogadorTruco(i, i % 2);
        }

        duplas[0] = new Dupla(0);
        duplas[1] = new Dupla(1);

        jogadorAtual = 0;
        reiniciarMao();
    }

    public void distribuirCartas() {
        BaralhoTruco baralho = new BaralhoTruco();
        baralho.embaralhar();

        
        for (JogadorTruco j : jogadores) {
            j.getMao().limpar(); 
        }

        for (int i = 0; i < 3; i++) {
            for (JogadorTruco j : jogadores) {
                j.getMao().adicionar(baralho.comprarCarta());
            }
        }
    }

    public boolean jogarCarta(int jogadorId, int indiceCarta) {
        if (jogadorId != jogadorAtual || aguardandoRespostaTruco) return false;

        Carta carta = jogadores[jogadorId].getMao().remover(indiceCarta);
        if (carta == null) return false;

        cartasNaMesa[jogadorId] = carta;
        cartasJogadasNaRodada++;

        proximoJogador();

        if (cartasJogadasNaRodada == 4) {
            avaliarRodada();
        }

        return true;
    }

    private void proximoJogador() {
        jogadorAtual = (jogadorAtual + 1) % 4;
    }

    private void avaliarRodada() {
        int vencedor = 0;
        int maiorForca = -1;

        for (int i = 0; i < 4; i++) {

            CartaTruco carta = (CartaTruco) cartasNaMesa[i];
            if (carta.getForca() > maiorForca) {
                maiorForca = carta.getForca();
                vencedor = i;
            }
        }

        int dupla = jogadores[vencedor].getDuplaId();
        placarParcial[dupla]++;

        cartasJogadasNaRodada = 0;
        rodadaAtual++;

        for (int i = 0; i < 4; i++) {
            cartasNaMesa[i] = null;
        }

        jogadorAtual = vencedor;
        verificarFimDaMao();
    }

    private void verificarFimDaMao() {
        if (placarParcial[0] == 2 || placarParcial[1] == 2 || rodadaAtual == 3) {
            int duplaVencedora = (placarParcial[0] > placarParcial[1]) ? 0 : 1;
            duplas[duplaVencedora].adicionarPontos(pontuacaoAtual);
            reiniciarMao();
        }
    }



    public boolean pedirTruco(int jogadorId) {
        if (aguardandoRespostaTruco || estadoTruco >= Constants.TRUCO_DOZE || cartasJogadasNaRodada >= 2) 
            return false;

        estadoTruco = Constants.TRUCO_TRUCADO;
        pontuacaoAtual = 3;
        jogadorQuePediuTruco = jogadorId;
        aguardandoRespostaTruco = true;
        duplaQuePodeAumentar = jogadores[jogadorId].getDuplaId();
        return true;
    }

    public boolean aceitarTruco(int jogadorId) {
        if (!ehDuplaAdversaria(jogadorId)) return false;
        aguardandoRespostaTruco = false;
        duplaQuePodeAumentar = jogadores[jogadorId].getDuplaId();
        return true;
    }

    public void correrTruco(int jogadorId) {
        if (!ehDuplaAdversaria(jogadorId)) return;

        int duplaPediu = jogadores[jogadorQuePediuTruco].getDuplaId();
        int pontos = (estadoTruco == Constants.TRUCO_TRUCADO) ? 1 : (pontuacaoAtual / 2);

        duplas[duplaPediu].adicionarPontos(pontos);
        reiniciarMao();
    }

    public boolean aumentarTruco(int jogadorId) {
        if (aguardandoRespostaTruco || jogadores[jogadorId].getDuplaId() != duplaQuePodeAumentar) 
            return false;

        if (estadoTruco == Constants.TRUCO_TRUCADO) {
            estadoTruco = Constants.TRUCO_SEIS;
            pontuacaoAtual = 6;
        } else if (estadoTruco == Constants.TRUCO_SEIS) {
            estadoTruco = Constants.TRUCO_NOVE;
            pontuacaoAtual = 9;
        } else if (estadoTruco == Constants.TRUCO_NOVE) {
            estadoTruco = Constants.TRUCO_DOZE;
            pontuacaoAtual = 12;
        } else {
            return false;
        }

        jogadorQuePediuTruco = jogadorId;
        aguardandoRespostaTruco = true;
        duplaQuePodeAumentar = 1 - jogadores[jogadorId].getDuplaId();
        return true;
    }

    private boolean ehDuplaAdversaria(int jogadorId) {
        return aguardandoRespostaTruco &&
               jogadores[jogadorId].getDuplaId() != jogadores[jogadorQuePediuTruco].getDuplaId();
    }

    public void reiniciarMao() {
        Arrays.fill(cartasNaMesa, null);
        estadoTruco = Constants.TRUCO_NAO_TRUCADO;
        pontuacaoAtual = 1;
        aguardandoRespostaTruco = false;
        rodadaAtual = 0;
        cartasJogadasNaRodada = 0;
        placarParcial[0] = 0;
        placarParcial[1] = 0;
        duplaQuePodeAumentar = -1;
        
        distribuirCartas();
    }

    public JogadorTruco[] getJogadores() { return jogadores; }
    public Carta[] getCartasNaMesa() { return cartasNaMesa; }
    public int getJogadorAtual() { return jogadorAtual; }
    public int getEstadoTruco() { return estadoTruco; }
    public int getPontuacaoAtual() { return pontuacaoAtual; }
    public boolean isAguardandoResposta() { return aguardandoRespostaTruco; }
    public Dupla[] getDuplas() { return duplas; }
}

