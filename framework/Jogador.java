
// Jogador.java
public class Jogador 
{
    private final int id;
    private final Mao mao;

    public Jogador(int id) 
    {
        this.id = id;
        this.mao = new Mao();
    }

    public int getId() { return id; }
    public Mao getMao() { return mao; }
}
