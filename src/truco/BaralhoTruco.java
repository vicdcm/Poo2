package truco;
import java.util.Arrays;
import java.util.List;

import framework.Baralho;
import framework.Carta;

public class BaralhoTruco extends Baralho 
{

    @Override
    protected void criarBaralho() 
    {

        adicionarCartasRanqueadas(
                Arrays.asList(Constants.AS, Constants.DOIS, Constants.TRES),
                Arrays.asList(Constants.COPAS, Constants.OUROS, Constants.ESPADAS, Constants.PAUS)
        );

        adicionarCarta(new CartaTruco(Constants.QUATRO, Constants.ESPADAS));
        adicionarCarta(new CartaTruco(Constants.QUATRO, Constants.PAUS));

        adicionarCartasRanqueadas(
                Arrays.asList(Constants.CINCO, Constants.SEIS),
                Arrays.asList(Constants.COPAS, Constants.OUROS, Constants.ESPADAS, Constants.PAUS)
        );

        adicionarCarta(new CartaTruco(Constants.SETE, Constants.COPAS));
        adicionarCarta(new CartaTruco(Constants.SETE, Constants.OUROS));

        adicionarCartasRanqueadas(
                Arrays.asList(Constants.REI, Constants.DAMA, Constants.VALETE),
                Arrays.asList(Constants.COPAS, Constants.OUROS, Constants.ESPADAS, Constants.PAUS)
        );
    }

    private void adicionarCartasRanqueadas(List<String> valores, List<String> naipes) {

        for (String valor : valores) {
            for (String naipe : naipes) {
                adicionarCarta(new CartaTruco(valor, naipe));
            }
        }
    }

}


