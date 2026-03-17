package Uno;

import framework.Baralho;
import java.util.List;

/**
 * Baralho de UNO com 96 cartas:
 *  - 4 cores × (1× zero + 2× um–nove) = 76 cartas numéricas
 *  - 4 cores × 2× Pular                =  8 cartas
 *  - 4 cores × 2× Inverter             =  8 cartas
 *  - 4× Curinga                        =  4 cartas
 *  Total: 96 cartas
 */
public class BaralhoUno extends Baralho {

    /**
     * Reabastece o baralho com as cartas da pilha de descarte (exceto o topo).
     * Chamado por UnoJogo quando o baralho esgota durante a partida.
     */
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
            // Um zero por cor
            adicionarCarta(new CartaUno("0", cor));

            // Dois de cada 1–9 por cor
            for (int n = 1; n <= 9; n++) {
                adicionarCarta(new CartaUno(String.valueOf(n), cor));
                adicionarCarta(new CartaUno(String.valueOf(n), cor));
            }

            // Duas de cada ação por cor
            adicionarCarta(new CartaUno(Constants.PULAR,    cor));
            adicionarCarta(new CartaUno(Constants.PULAR,    cor));
            adicionarCarta(new CartaUno(Constants.INVERTER, cor));
            adicionarCarta(new CartaUno(Constants.INVERTER, cor));
        }

        // Quatro curingas (cor preta, sem cor fixa)
        for (int i = 0; i < 4; i++) {
            adicionarCarta(new CartaUno(Constants.CURINGA, Constants.PRETO));
        }
    }
}
