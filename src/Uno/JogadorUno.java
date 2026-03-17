package Uno;

import framework.Jogador;

/**
 * Jogador de UNO.
 * Rastreia se o jogador já declarou "UNO" quando ficou com 1 carta.
 */
public class JogadorUno extends Jogador {

    private boolean declarouUno;

    public JogadorUno(int id) {
        super(id);
        this.declarouUno = false;
    }

    public boolean isDeclarouUno() {
        return declarouUno;
    }

    public void setDeclarouUno(boolean declarouUno) {
        this.declarouUno = declarouUno;
    }
}
