package framework;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Baralho 
{
    protected final List<Carta> cartas;

    public Baralho() 
    {
        this.cartas = new ArrayList<>();
        criarBaralho();
    }

    protected void criarBaralho()
    {
        ;
    }

   protected void adicionarCarta(Carta carta) 
   {
        this.cartas.add(carta);
    }
    
    

    public void embaralhar() 

    {
        Collections.shuffle(cartas, new Random());
    }

  public Carta comprarCarta() {
    if (this.cartas == null || this.cartas.isEmpty()) {
        return null; 
    }
   
    return this.cartas.remove(this.cartas.size() - 1);
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
