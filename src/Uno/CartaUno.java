package Uno;

import framework.Carta;


public class CartaUno extends Carta {

    public CartaUno(String valor, String cor) {
        super(valor, cor);
    }


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
