package Uno;

import framework.Carta;

/**
 * Carta de UNO.
 * valor = "0"–"9", "Pular", "Inverter", "Curinga"
 * tipo  = cor da carta (Vermelho, Azul, Verde, Amarelo, Preto)
 *
 * Cartas curinga têm tipo "Preto" e não possuem cor fixa;
 * a cor ativa é definida pelo jogador ao jogá-las e fica em UnoJogo.
 */
public class CartaUno extends Carta {

    public CartaUno(String valor, String cor) {
        super(valor, cor);
    }

    /** Cor da carta (Preto para curingas). */
    public String getCor() {
        return getTipo();
    }

    public boolean isNumerica() {
        return getValor().matches("[0-9]");
    }

    public boolean isAcao() {
        return getValor().equals(Constants.PULAR) ||
               getValor().equals(Constants.INVERTER);
    }

    public boolean isCuringa() {
        return getValor().equals(Constants.CURINGA);
    }

    @Override
    public String toString() {
        if (isCuringa()) return "🃏 Curinga";
        String icone = switch (getCor()) {
            case Constants.VERMELHO -> "🔴";
            case Constants.AZUL     -> "🔵";
            case Constants.VERDE    -> "🟢";
            case Constants.AMARELO  -> "🟡";
            default -> "⬛";
        };
        return icone + " " + getValor();
    }
}
