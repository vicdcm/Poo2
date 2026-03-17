package Uno;

import framework.Carta;

import java.util.ArrayList;
import java.util.List;

/**
 * Lógica completa do jogo de UNO para 4 jogadores.
 *
 * Regras implementadas:
 *  - Cartas numéricas: joga se mesma cor ou mesmo número
 *  - Pular: próximo jogador perde a vez
 *  - Inverter: inverte o sentido do jogo
 *  - Curinga: jogador escolhe a nova cor ativa
 *  - UNO!: ao ficar com 1 carta, deve declarar; se acusado antes de declarar,
 *           compra 2 cartas de penalidade
 *  - Fim de jogo: primeiro a zerar a mão vence
 */
public class UnoJogo {

    private static final int NUM_JOGADORES  = 4;
    private static final int CARTAS_INICIAIS = 7;

    // ─── Estado do jogo ───────────────────────────────────────────────────────
    private final JogadorUno[] jogadores;
    private BaralhoUno         baralho;
    private final List<CartaUno> pilhaDescarte;

    private int     jogadorAtual;
    private boolean sentidoHorario;
    private String  corAtual;           // cor vigente (muda com curingas)
    private CartaUno cartaTopo;         // carta visível no topo da pilha

    // Controle de UNO
    // precisaDeclarar[i] = true quando o jogador i tem 1 carta e ainda não declarou
    private final boolean[] precisaDeclarar;

    // Aguardando escolha de cor após jogar curinga
    private boolean aguardandoEscolhaCor;

    // ─── Construtor ──────────────────────────────────────────────────────────

    public UnoJogo() {
        jogadores      = new JogadorUno[NUM_JOGADORES];
        pilhaDescarte  = new ArrayList<>();
        precisaDeclarar = new boolean[NUM_JOGADORES];

        for (int i = 0; i < NUM_JOGADORES; i++) {
            jogadores[i] = new JogadorUno(i);
        }

        iniciarPartida();
    }

    // ─── Inicialização ────────────────────────────────────────────────────────

    private void iniciarPartida() {
        baralho = new BaralhoUno();
        baralho.embaralhar();

        for (JogadorUno j : jogadores) {
            j.getMao().limpar();
            j.setDeclarouUno(false);
        }
        pilhaDescarte.clear();

        // Distribui 7 cartas para cada jogador
        for (int i = 0; i < CARTAS_INICIAIS; i++) {
            for (JogadorUno j : jogadores) {
                j.getMao().adicionar(baralho.comprarCarta());
            }
        }

        // Vira a primeira carta — recompra se for curinga
        CartaUno primeira;
        do {
            primeira = (CartaUno) baralho.comprarCarta();
        } while (primeira.isCuringa());

        pilhaDescarte.add(primeira);
        cartaTopo  = primeira;
        corAtual   = primeira.getCor();

        jogadorAtual    = 0;
        sentidoHorario  = true;
        aguardandoEscolhaCor = false;
    }

    // ─── Jogada ───────────────────────────────────────────────────────────────

    /**
     * Tenta jogar a carta no índice indicado.
     * corEscolhida só é relevante para curingas; ignorado nas demais.
     * Retorna true se a jogada foi aceita.
     */
    public boolean jogarCarta(int jogadorId, int indice, String corEscolhida) {
        if (jogadorId != jogadorAtual) return false;
        if (aguardandoEscolhaCor) return false;

        CartaUno carta = (CartaUno) jogadores[jogadorId].getMao().get(indice);
        if (carta == null) return false;
        if (!podeJogar(carta)) return false;

        jogadores[jogadorId].getMao().remover(indice);
        pilhaDescarte.add(carta);
        cartaTopo = carta;

        // Atualiza cor vigente
        if (carta.isCuringa()) {
            if (corEscolhida != null && List.of(Constants.CORES_NORMAIS).contains(corEscolhida)) {
                corAtual = corEscolhida;
            } else {
                aguardandoEscolhaCor = true; // GUI precisa pedir a cor
            }
        } else {
            corAtual = carta.getCor();
        }

        // Controle de UNO
        int cartasRestantes = jogadores[jogadorId].getMao().tamanho();
        if (cartasRestantes == 1) {
            precisaDeclarar[jogadorId] = true;
            jogadores[jogadorId].setDeclarouUno(false);
        } else {
            precisaDeclarar[jogadorId] = false;
        }

        // Aplica efeito da carta e avança o turno (se jogo não acabou)
        if (!jogoAcabou()) {
            aplicarEfeito(carta);
        }

        return true;
    }

    /** Define a cor após jogar um curinga (chamado pela GUI). */
    public boolean escolherCor(int jogadorId, String cor) {
        if (!aguardandoEscolhaCor) return false;
        if (jogadorId != jogadorAtual) return false;
        if (!List.of(Constants.CORES_NORMAIS).contains(cor)) return false;

        corAtual = cor;
        aguardandoEscolhaCor = false;
        aplicarEfeito(cartaTopo); // aplica o efeito do curinga agora
        return true;
    }

    private void aplicarEfeito(CartaUno carta) {
        switch (carta.getValor()) {
            case Constants.PULAR -> {
                avancarJogador(); // pula o próximo
                avancarJogador();
            }
            case Constants.INVERTER -> {
                sentidoHorario = !sentidoHorario;
                avancarJogador();
            }
            default -> avancarJogador(); // numérica e curinga: apenas avança
        }
    }

    // ─── Comprar carta ────────────────────────────────────────────────────────

    /**
     * Jogador compra uma carta. Só pode comprar na sua vez e se não puder jogar
     * nenhuma carta da mão (ou por escolha).
     * Retorna a carta comprada, ou null se inválido.
     */
    public CartaUno comprarCarta(int jogadorId) {
        if (jogadorId != jogadorAtual) return null;
        if (aguardandoEscolhaCor) return null;

        reabastecerBaralhoSeNecessario();
        if (baralho.isEmpty()) return null;

        CartaUno carta = (CartaUno) baralho.comprarCarta();
        jogadores[jogadorId].getMao().adicionar(carta);

        // Comprar cancela qualquer pendência de UNO
        precisaDeclarar[jogadorId] = false;

        avancarJogador();
        return carta;
    }

    private void reabastecerBaralhoSeNecessario() {
        if (!baralho.isEmpty()) return;
        CartaUno topo = pilhaDescarte.remove(pilhaDescarte.size() - 1);
        baralho.reabastecerDePilha(pilhaDescarte);
        pilhaDescarte.clear();
        pilhaDescarte.add(topo);
    }

    // ─── UNO ─────────────────────────────────────────────────────────────────

    /**
     * Jogador declara "UNO!" após ficar com 1 carta.
     * Retorna true se a declaração foi válida (jogador tem 1 carta e ainda não declarou).
     */
    public boolean declararUno(int jogadorId) {
        if (jogadores[jogadorId].getMao().tamanho() != 1) return false;
        if (jogadores[jogadorId].isDeclarouUno()) return false;

        jogadores[jogadorId].setDeclarouUno(true);
        precisaDeclarar[jogadorId] = false;
        return true;
    }

    /**
     * Um jogador acusa outro de ter esquecido de dizer UNO.
     * Se válido, o acusado compra 2 cartas de penalidade.
     * Retorna true se a acusação foi válida.
     */
    public boolean acusarUno(int acusadorId, int acusadoId) {
        if (acusadorId == acusadoId) return false;
        if (!precisaDeclarar[acusadoId]) return false;

        // Aplica penalidade
        for (int i = 0; i < Constants.PENALIDADE_UNO; i++) {
            reabastecerBaralhoSeNecessario();
            if (!baralho.isEmpty()) {
                jogadores[acusadoId].getMao().adicionar(baralho.comprarCarta());
            }
        }
        precisaDeclarar[acusadoId] = false;
        return true;
    }

    // ─── Validação ────────────────────────────────────────────────────────────

    /**
     * Uma carta pode ser jogada se:
     *  - For curinga (sempre jogável)
     *  - Tiver a mesma cor vigente
     *  - Tiver o mesmo valor que a carta do topo
     */
    public boolean podeJogar(CartaUno carta) {
        if (carta.isCuringa()) return true;
        if (carta.getCor().equals(corAtual)) return true;
        if (carta.getValor().equals(cartaTopo.getValor())) return true;
        return false;
    }

    /** Verifica se o jogador atual tem ao menos uma carta jogável. */
    public boolean jogadorTemJogadaValida(int jogadorId) {
        for (Carta c : jogadores[jogadorId].getMao().getCartas()) {
            if (podeJogar((CartaUno) c)) return true;
        }
        return false;
    }

    // ─── Fim de jogo ─────────────────────────────────────────────────────────

    public boolean jogoAcabou() {
        for (JogadorUno j : jogadores) {
            if (j.getMao().estaVazia()) return true;
        }
        return false;
    }

    public int getVencedor() {
        for (JogadorUno j : jogadores) {
            if (j.getMao().estaVazia()) return j.getId();
        }
        return -1;
    }

    // ─── Auxiliares ──────────────────────────────────────────────────────────

    private void avancarJogador() {
        int passo = sentidoHorario ? 1 : NUM_JOGADORES - 1;
        jogadorAtual = (jogadorAtual + passo) % NUM_JOGADORES;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public JogadorUno[] getJogadores()           { return jogadores.clone(); }
    public int          getJogadorAtual()        { return jogadorAtual; }
    public CartaUno     getCartaTopo()           { return cartaTopo; }
    public String       getCorAtual()            { return corAtual; }
    public boolean      isSentidoHorario()       { return sentidoHorario; }
    public boolean      isAguardandoEscolhaCor() { return aguardandoEscolhaCor; }
    public boolean      isPrecisaDeclarar(int id){ return precisaDeclarar[id]; }
}