// Carta.java
import java.util.Objects;

public class Carta {
    private final String valor;
    private final String naipe;

    public Carta(String valor, String naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }

    public String getValor() { return valor; }
    public String getNaipe() { return naipe; }

    @Override
    public String toString() {
        String simbolo;
        switch (naipe) {
            case Constants.COPAS: simbolo = Constants.SIMBOLO_COPAS; break;
            case Constants.OUROS: simbolo = Constants.SIMBOLO_OUROS; break;
            case Constants.ESPADAS: simbolo = Constants.SIMBOLO_ESPADAS; break;
            case Constants.PAUS: simbolo = Constants.SIMBOLO_PAUS; break;
            default: simbolo = "";
        }
        return valor + simbolo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carta carta = (Carta) o;
        return Objects.equals(valor, carta.valor) && Objects.equals(naipe, carta.naipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor, naipe);
    }

    // Retorna o valor numérico para comparação (quanto maior, mais forte)
    public int getForca() {
        // Manilhas fixas (força 25 a 28)
        if (valor.equals(Constants.QUATRO) && naipe.equals(Constants.ESPADAS)) return 28; // Zape
        if (valor.equals(Constants.SETE) && naipe.equals(Constants.COPAS)) return 27;   // 7♥
        if (valor.equals(Constants.QUATRO) && naipe.equals(Constants.PAUS)) return 26;  // Espadilha
        if (valor.equals(Constants.SETE) && naipe.equals(Constants.OUROS)) return 25;   // 7♦

        // Outras cartas
        return switch (valor) {
            case Constants.TRES -> 24;
            case Constants.DOIS -> 23;
            case Constants.AS -> 22;
            case Constants.QUATRO -> 21;
            case Constants.CINCO -> 20;
            case Constants.SEIS -> 19;
            case Constants.SETE -> 18; // 7 que não é manilha
            case Constants.REI -> 17;
            case Constants.DAMA -> 16;
            case Constants.VALETE -> 15;
            default -> 0;
        };
    }
}