package truco;

import framework.Jogador;

public class JogadorTruco extends Jogador {

    private final int duplaId;

    public JogadorTruco(int id, int duplaId) {
        super(id);
        this.duplaId = duplaId;
    }

    public int getDuplaId() {
        return duplaId;
    }
}

