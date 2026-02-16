


import java.util.Arrays;
import java.util.List;


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

    
}
