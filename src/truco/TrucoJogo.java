
package truco;
 
import framework.Carta;
import java.util.Arrays;
 
public class TrucoJogo {
 
    private final JogadorTruco[] jogadores;
    private final Dupla[] duplas;
    private Carta[] cartasNaMesa;
 
    private int jogadorAtual;
    private int jogadorInicial;           // NOVO: controla rotação entre mãos
    private int jogadorQuePediuTruco;
 
    private int estadoTruco;
    private int pontuacaoAtual;
 
    private boolean aguardandoRespostaTruco;
 
    private int rodadaAtual;
    private int cartasJogadasNaRodada;
    private int vencedorPrimeiraRodada;   // NOVO: desempate de mão
 
    private int duplaQuePodeAumentar;
    private int[] placarParcial;
 
    public TrucoJogo() {
        jogadores   = new JogadorTruco[4];
        duplas      = new Dupla[2];
        cartasNaMesa = new Carta[4];
        placarParcial = new int[2];
        inicializarJogo();
    }
 
    // ─── Inicialização ───────────────────────────────────────────────────────
 
    private void inicializarJogo() {
        for (int i = 0; i < 4; i++) {
            jogadores[i] = new JogadorTruco(i, i % 2);
        }
        duplas[0] = new Dupla(0);
        duplas[1] = new Dupla(1);
 
        jogadorAtual  = 0;
        jogadorInicial = 0;
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
 
    // ─── Jogo de cartas ──────────────────────────────────────────────────────
 
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
 
    // CORRIGIDO: cast seguro via instanceof + empate de rodada tratado
    private void avaliarRodada() {
        int vencedor   = -1;
        int maiorForca = -1;
        boolean empatou = false;
 
        for (int i = 0; i < 4; i++) {
            int forca = getForcaCarta(cartasNaMesa[i]);
            if (forca > maiorForca) {
                maiorForca = forca;
                vencedor   = i;
                empatou    = false;
            } else if (forca == maiorForca) {
                empatou = true;
            }
        }
 
        if (!empatou && vencedor != -1) {
            int duplaVencedora = jogadores[vencedor].getDuplaId();
            placarParcial[duplaVencedora]++;
 
            // NOVO: guarda quem venceu a 1ª rodada para desempate de mão
            if (rodadaAtual == 0) {
                vencedorPrimeiraRodada = duplaVencedora;
            }
            jogadorAtual = vencedor;
        }
        // Se empatou a rodada, mantém o jogador que iniciou a rodada
 
        cartasJogadasNaRodada = 0;
        rodadaAtual++;
        Arrays.fill(cartasNaMesa, null);
 
        verificarFimDaMao();
    }
 
    // Extrai força sem cast inseguro
    private int getForcaCarta(Carta carta) {
        if (carta instanceof CartaTruco ct) return ct.getForca();
        return 0;
    }
 
    // CORRIGIDO: empate de mão → vence quem ganhou a 1ª rodada
    private void verificarFimDaMao() {
        boolean fimDaMao = placarParcial[0] == 2 || placarParcial[1] == 2 || rodadaAtual == 3;
        if (!fimDaMao) return;
 
        int duplaVencedora;
        if (placarParcial[0] > placarParcial[1]) {
            duplaVencedora = 0;
        } else if (placarParcial[1] > placarParcial[0]) {
            duplaVencedora = 1;
        } else {
            // Empate de rodadas → vence quem ganhou a 1ª
            duplaVencedora = vencedorPrimeiraRodada >= 0 ? vencedorPrimeiraRodada : 0;
        }
 
        duplas[duplaVencedora].adicionarPontos(pontuacaoAtual);
 
        // NOVO: rotação de quem inicia a próxima mão
        jogadorInicial = (jogadorInicial + 1) % 4;
        jogadorAtual   = jogadorInicial;
 
        reiniciarMao();
    }
 
    // ─── Fim de jogo ─────────────────────────────────────────────────────────
 
    // NOVO
    public boolean jogoAcabou() {
        return duplas[0].venceu() || duplas[1].venceu();
    }
 
    // NOVO
    public int getDuplaVencedora() {
        if (duplas[0].venceu()) return 0;
        if (duplas[1].venceu()) return 1;
        return -1;
    }
 
    // ─── Truco ───────────────────────────────────────────────────────────────
 
    // CORRIGIDO: só permite pedir quando não trucado; duplaQuePodeAumentar = adversária
    public boolean pedirTruco(int jogadorId) {
        if (estadoTruco != Constants.TRUCO_NAO_TRUCADO) return false;
        if (aguardandoRespostaTruco) return false;
        if (cartasJogadasNaRodada >= 2) return false;
 
        estadoTruco  = Constants.TRUCO_TRUCADO;
        pontuacaoAtual = 3;
        jogadorQuePediuTruco = jogadorId;
        aguardandoRespostaTruco = true;
        duplaQuePodeAumentar = 1 - jogadores[jogadorId].getDuplaId(); // CORRIGIDO
        return true;
    }
 
    public boolean aceitarTruco(int jogadorId) {
        if (!ehDuplaAdversaria(jogadorId)) return false;
        aguardandoRespostaTruco = false;
        // Após aceitar, a dupla que aceitou pode aumentar
        duplaQuePodeAumentar = jogadores[jogadorId].getDuplaId();
        return true;
    }
 
    public void correrTruco(int jogadorId) {
        if (!ehDuplaAdversaria(jogadorId)) return;
 
        int duplaPediu = jogadores[jogadorQuePediuTruco].getDuplaId();
        // Quem pediu ganha os pontos do nível anterior ao current
        int pontos = (estadoTruco == Constants.TRUCO_TRUCADO) ? 1 : (pontuacaoAtual - 3);
        duplas[duplaPediu].adicionarPontos(pontos);
 
        jogadorInicial = (jogadorInicial + 1) % 4;
        jogadorAtual   = jogadorInicial;
        reiniciarMao();
    }
 
    // CORRIGIDO: permite aumentar como resposta (aguardandoRespostaTruco == true)
    public boolean aumentarTruco(int jogadorId) {
        if (jogadores[jogadorId].getDuplaId() != duplaQuePodeAumentar) return false;
        if (estadoTruco == Constants.TRUCO_DOZE) return false;
 
        switch (estadoTruco) {
            case Constants.TRUCO_TRUCADO -> { estadoTruco = Constants.TRUCO_SEIS;  pontuacaoAtual = 6;  }
            case Constants.TRUCO_SEIS    -> { estadoTruco = Constants.TRUCO_NOVE;  pontuacaoAtual = 9;  }
            case Constants.TRUCO_NOVE    -> { estadoTruco = Constants.TRUCO_DOZE;  pontuacaoAtual = 12; }
            default -> { return false; }
        }
 
        jogadorQuePediuTruco    = jogadorId;
        aguardandoRespostaTruco = true;
        duplaQuePodeAumentar    = 1 - jogadores[jogadorId].getDuplaId();
        return true;
    }
 
    private boolean ehDuplaAdversaria(int jogadorId) {
        return aguardandoRespostaTruco &&
               jogadores[jogadorId].getDuplaId() != jogadores[jogadorQuePediuTruco].getDuplaId();
    }
 
    public void reiniciarMao() {
        Arrays.fill(cartasNaMesa, null);
        estadoTruco             = Constants.TRUCO_NAO_TRUCADO;
        pontuacaoAtual          = 1;
        aguardandoRespostaTruco = false;
        rodadaAtual             = 0;
        cartasJogadasNaRodada   = 0;
        placarParcial[0]        = 0;
        placarParcial[1]        = 0;
        duplaQuePodeAumentar    = -1;
        vencedorPrimeiraRodada  = -1;
 
        distribuirCartas();
    }
 
    // ─── Getters (com cópias defensivas) ─────────────────────────────────────
 
    public JogadorTruco[] getJogadores()        { return jogadores.clone(); }
    public Carta[]        getCartasNaMesa()      { return cartasNaMesa.clone(); }
    public int            getJogadorAtual()      { return jogadorAtual; }
    public int            getEstadoTruco()       { return estadoTruco; }
    public int            getPontuacaoAtual()    { return pontuacaoAtual; }
    public boolean        isAguardandoResposta() { return aguardandoRespostaTruco; }
    public Dupla[]        getDuplas()            { return duplas.clone(); }
    public int            getJogadorQuePediuTruco() { return jogadorQuePediuTruco; }
    public int            getRodadaAtual()       { return rodadaAtual; }
    public int            getCartasJogadasNaRodada() { return cartasJogadasNaRodada; }
    public int            getDuplaQuePodeAumentar()  { return duplaQuePodeAumentar; }
 
    public int getPontosAoCorre() {
        return switch (estadoTruco) {
            case Constants.TRUCO_TRUCADO -> 1;
            case Constants.TRUCO_SEIS    -> 3;
            case Constants.TRUCO_NOVE    -> 6;
            case Constants.TRUCO_DOZE    -> 9;
            default -> 1;
        };
    }
}