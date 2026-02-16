import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Baralho 
{
    private final List<Carta> cartas;

    public Baralho() 
    {
        this.cartas = new ArrayList<>();
        criarBaralho();
    }

    private void criarBaralho()
    {
        ;
    }

    private void adicionarCartas()
    {
        ;
    }
    
    

    public void embaralhar() 

    {
        Collections.shuffle(cartas, new Random());
    }

    public Carta comprarCarta() 
    {
        if (cartas.isEmpty()) return null;
        return cartas.remove(cartas.size() - 1);
    }

    public boolean isEmpty() 
    {
        return cartas.isEmpty();
    }

    public List<Carta> getCartas() 
    {
        return new ArrayList<>(cartas);
    }

    
}
