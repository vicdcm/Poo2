import truco.TrucoJogo;


import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;


public class Truco 
{
    private final List<Carta> cartas = null;
    private void criarBaralhoTruco() {
        // Ás to 3 in all suits
        adicionarCartasRanqueadas(Arrays.asList(Constants.AS, Constants.DOIS, Constants.TRES),
                Arrays.asList(Constants.COPAS, Constants.OUROS, Constants.ESPADAS, Constants.PAUS));

        // 4 only in Espadas and Paus (manilhas: Zape and Espadilha)
        cartas.add(new Carta(Constants.QUATRO, Constants.ESPADAS));
        cartas.add(new Carta(Constants.QUATRO, Constants.PAUS));

        // 5 and 6 in all suits
       adicionarCartasRanqueadas(Arrays.asList(Constants.CINCO, Constants.SEIS),
                Arrays.asList(Constants.COPAS, Constants.OUROS, Constants.ESPADAS, Constants.PAUS));

        // 7 only in Copas and Ouros (manilhas)
        cartas.add(new Carta(Constants.SETE, Constants.COPAS));
        cartas.add(new Carta(Constants.SETE, Constants.OUROS));

        // Faces in all suits
        adicionarCartasRanqueadas(Arrays.asList(Constants.REI, Constants.DAMA, Constants.VALETE),
                Arrays.asList(Constants.COPAS, Constants.OUROS, Constants.ESPADAS, Constants.PAUS));
    }

    private void adicionarCartasRanqueadas(List<String> valores, List<String> naipes) {
        for (String valor : valores) {
            for (String naipe : naipes) {
                cartas.add(new Carta(valor, naipe));
            }
        }
    }

     private void inicializarJogadoresEDuplas(Jogador jogadores[], Dupla duplas[]) { 
        jogadores = new Jogador[4];
        for (int i = 0; i < 4; i++) {
            jogadores[i] = new Jogador(i, i % 2);
        }
        duplas = new Dupla[]{new Dupla(0), new Dupla(1)};
    }

     private void distribuirCartas(
     Jogador[] jogadores,
     Dupla[] duplas,
     Carta[] cartasNaMesa,
     int jogadorAtual,
     int estadoTruco,
     int pontuacaoAtual,
     boolean aguardandoRespostaTruco,
     int jogadorQuePediuTruco,
     int rodadaAtual

     ) {
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

     private void pedirTruco(
     Jogador[] jogadores,
     Dupla[] duplas,
     Carta[] cartasNaMesa,
     int jogadorAtual,
     int estadoTruco,
     int pontuacaoAtual,
     boolean aguardandoRespostaTruco,
     int jogadorQuePediuTruco,
     int rodadaAtual,
     int[] placarParcial,
     int cartasJogadasNaRodada,
     int duplaQuePodeAumentar,
     int jogadorId
    ) {

     ) {
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

    private void aceitarTruco(
        int jogadorId,
        boolean aguardandoRespostaTruco,
        int duplaQuePodeAumentar,
        Jogador jogadores[]

    ) {
        if (!ehDuplaAdversaria(jogadorId)) return;
        aguardandoRespostaTruco = false;
        duplaQuePodeAumentar = jogadores[jogadorId].getDuplaId(); // quem aceitou pode aumentar
        atualizarInterface();
    }

    private void correrTruco(
          Jogador[] jogadores,
     Dupla[] duplas,
     Carta[] cartasNaMesa,
     int jogadorAtual,
     int estadoTruco,
     int pontuacaoAtual,
     boolean aguardandoRespostaTruco,
     int jogadorQuePediuTruco,
     int rodadaAtual,
     int jogadorId
    ) {
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

    private void aumentarTruco(
        int jogadorId, 
        int novoEstado, 
        int novaPontuacao,
        Jogador[] jogadores,
        Dupla[] duplas,
        Carta[] cartasNaMesa,
        int jogadorAtual,
        int estadoTruco,
        int pontuacaoAtual,
        boolean aguardandoRespostaTruco,
        int jogadorQuePediuTruco,
        int rodadaAtual,
        int duplaQuePodeAumentar
    ) {
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

    private boolean ehDuplaAdversaria(int jogadorId, Jogador[] jogadores, int jogadorQuePediuTruco, boolean aguardandoRespostaTruco) {
        return aguardandoRespostaTruco &&
                jogadores[jogadorId].getDuplaId() != jogadores[jogadorQuePediuTruco].getDuplaId();
    }

    private void reiniciarMao(Carta[] cartasNaMesa, JLabel[] labelsMesa, int estadoTruco, int pontuacaoAtual, boolean aguardandoRespostaTruco, int rodadaAtual, int[] placarParcial, int cartasJogadasNaRodada, int duplaQuePodeAumentar, int jogadorAtual, int jogadorQuePediuTruco) {
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


    
}
