import framework.Carta;
import framework.Jogador;
import framework.Mao;

import java.util.Arrays;
import java.util.List;


public class truco 
{
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


    
}
