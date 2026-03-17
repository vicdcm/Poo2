package Uno;

import framework.Baralho;
import java.util.List;


public class BaralhoUno extends Baralho {

    
    public void reabastecerDePilha(List<CartaUno> pilhaDescarte) {
        for (CartaUno c : pilhaDescarte) {
            if (c.isCuringa()) {
                adicionarCarta(new CartaUno(Constants.CURINGA, Constants.PRETO));
            } else {
                adicionarCarta(c);
            }
        }
        embaralhar();
    }

    @Override
    protected void criarBaralho() {
        for (String cor : Constants.CORES_NORMAIS) {
           
            adicionarCarta(new CartaUno("0", cor));

            
            for (int n = 1; n <= 9; n++) {
                adicionarCarta(new CartaUno(String.valueOf(n), cor));
                adicionarCarta(new CartaUno(String.valueOf(n), cor));
            }

          
            adicionarCarta(new CartaUno(Constants.PULAR,    cor));
            adicionarCarta(new CartaUno(Constants.PULAR,    cor));
            adicionarCarta(new CartaUno(Constants.INVERTER, cor));
            adicionarCarta(new CartaUno(Constants.INVERTER, cor));
        }

      
        for (int i = 0; i < 4; i++) {
            adicionarCarta(new CartaUno(Constants.CURINGA, Constants.PRETO));
        }
    }
}
