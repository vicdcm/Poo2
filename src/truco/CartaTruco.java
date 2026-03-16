public class CartaTruco extends Carta {

    public CartaTruco(String valor, String tipo) {
        super(valor, tipo);
    }

    public int getForca() {

    
        if (getValor().equals(Constants.QUATRO) && getTipo().equals(Constants.ESPADAS)) return 28; // Zape
        if (getValor().equals(Constants.SETE) && getTipo().equals(Constants.COPAS)) return 27;
        if (getValor().equals(Constants.QUATRO) && getTipo().equals(Constants.PAUS)) return 26;
        if (getValor().equals(Constants.SETE) && getTipo().equals(Constants.OUROS)) return 25;

        
        return switch (getValor()) {
            case Constants.TRES -> 24;
            case Constants.DOIS -> 23;
            case Constants.AS -> 22;
            case Constants.QUATRO -> 21;
            case Constants.CINCO -> 20;
            case Constants.SEIS -> 19;
            case Constants.SETE -> 18;
            case Constants.REI -> 17;
            case Constants.DAMA -> 16;
            case Constants.VALETE -> 15;
            default -> 0;
        };
    }
}
