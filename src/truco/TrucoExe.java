public class TrucoExe {
    private Jogador[] jogadores;
    private Dupla[] duplas;
    private Carta[] cartasNaMesa;
    private int jogadorAtual;
    private int estadoTruco;
    private int pontuacaoAtual;
    private boolean aguardandoRespostaTruco;
    private int jogadorQuePediuTruco;
    private int rodadaAtual;
    private int[] placarParcial = new int[2];
    private int cartasJogadasNaRodada;
    private int duplaQuePodeAumentar = -1; // -1 = ninguém; 0 ou 1 = dupla com direito de aumentar
}
