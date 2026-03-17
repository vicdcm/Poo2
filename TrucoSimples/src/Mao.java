// Mao.java
import java.util.*;

public class Mao {
    private final List<Carta> cartas;

    public Mao() {
        this.cartas = new ArrayList<>();
    }

    public void adicionar(Carta carta) {
        cartas.add(carta);
    }

    public Carta remover(int indice) {
        if (indice < 0 || indice >= cartas.size()) return null;
        return cartas.remove(indice);
    }

    public Carta get(int indice) {
        if (indice < 0 || indice >= cartas.size()) return null;
        return cartas.get(indice);
    }

    public int tamanho() {
        return cartas.size();
    }

    public boolean estaVazia() {
        return cartas.isEmpty();
    }

    public List<Carta> getCartas() {
        return new ArrayList<>(cartas);
    }
}