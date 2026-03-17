package Uno;
import framework.Carta;

public class CartaUno extends Carta 
{
    public CartaUno(String valor, String cor) 
    {
        super(valor, cor); 
    }

    public boolean podeJogarSobre(CartaUno outra) 
    {
        if (this.getTipo().equals(ConstantsUNO.PRETO)) return true; 
        return this.getTipo().equals(outra.getTipo()) || 
               this.getValor().equals(outra.getValor());
    }

    
}
