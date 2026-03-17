package Uno;

import framework.*;
import java.util.ArrayList;

public class UnoJogo {
    private ArrayList<Jogador> jogadores;
    private BaralhoUno baralho;
    private CartaUno cartaTopo;
    private String corAtual; // Importante para Coringas
    
    private int jogadorAtual = 0;
    private int sentido = 1; // 1 = horário, -1 = anti-horário
    private boolean precisaComprar = false; // Para quando o jogador não tem carta

    public UnoJogo(int numJogadores) {
        jogadores = new ArrayList<>();
        for (int i = 0; i < numJogadores; i++) jogadores.add(new Jogador(i));
        
        baralho = new BaralhoUno();
        baralho.embaralhar();
        
        // Cada um começa com 7
        for (Jogador j : jogadores) {
            for (int i = 0; i < 7; i++) j.getMao().adicionar(baralho.comprarCarta());
        }
        
        // Abre a primeira carta
        cartaTopo = (CartaUno) baralho.comprarCarta();
        corAtual = cartaTopo.getTipo(); // A cor inicial é a da carta
    }

    public boolean jogar(int jogadorId, int indice) {
        if (jogadorId != jogadorAtual) return false;

        CartaUno escolhida = (CartaUno) jogadores.get(jogadorId).getMao().getCartas().get(indice);

        // Regra de validação: Cor igual, Valor igual ou Coringa
        if (escolhida.getTipo().equals(ConstantsUNO.PRETO) || 
            escolhida.getTipo().equals(corAtual) || 
            escolhida.getValor().equals(cartaTopo.getValor())) {
            
            jogadores.get(jogadorId).getMao().remover(indice);
            cartaTopo = escolhida;
            corAtual = escolhida.getTipo();
            
            processarEfeito(escolhida);
            return true;
        }
        return false;
    }

    private void processarEfeito(CartaUno carta) {
        String valor = carta.getValor();
        
        if (valor.equals(ConstantsUNO.INVERTER)) {
            sentido *= -1;
            if (jogadores.size() == 2) passarVez(); // No X1, inverter pula a vez
        } else if (valor.equals(ConstantsUNO.PULHAR)) {
            passarVez();
        }
        // O próximo jogador será definido ao final da ação
        passarVez();
    }

    public void passarVez() {
        jogadorAtual = (jogadorAtual + sentido + jogadores.size()) % jogadores.size();
    }
    
    public void comprar(int jogadorId) {
        if (jogadorId == jogadorAtual) {
            jogadores.get(jogadorId).getMao().adicionar(baralho.comprarCarta());
            passarVez();
        }
    }

    // Getters para a GUI
    public CartaUno getCartaTopo() { return cartaTopo; }
    public String getCorAtual() { return corAtual; }
    public int getJogadorAtual() { return jogadorAtual; }
    public Jogador getJogador(int id) { return jogadores.get(id); }
}