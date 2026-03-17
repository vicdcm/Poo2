package Uno;

import framework.Baralho;
import java.util.Arrays;
import Uno.ConstantsUNO;

public class BaralhoUno extends Baralho {
    @Override
    protected void criarBaralho() {
        String[] cores = {ConstantsUNO.VERMELHO, ConstantsUNO.AMARELO, ConstantsUNO.VERDE, ConstantsUNO.AZUL};
        
        for (String cor : cores) {
            // Um "0" de cada cor
            adicionarCarta(new CartaUno("0", cor));
            // Dois de cada (1-9, Pular, Inverter, +2)
            for (int i = 0; i < 2; i++) {
                for (int n = 1; n <= 9; n++) adicionarCarta(new CartaUno(String.valueOf(n), cor));
                adicionarCarta(new CartaUno(ConstantsUNO.PULHAR, cor));
                adicionarCarta(new CartaUno(ConstantsUNO.INVERTER, cor));
                adicionarCarta(new CartaUno(ConstantsUNO.MAIS_DOIS, cor));
            }
        }
        // Coringas
        for (int i = 0; i < 4; i++) {
            adicionarCarta(new CartaUno(ConstantsUNO.CORINGA, ConstantsUNO.PRETO));
            adicionarCarta(new CartaUno(ConstantsUNO.MAIS_QUATRO, ConstantsUNO.PRETO));
        }
    }
}
