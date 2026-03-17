package Uno;

import javax.swing.*;
import java.awt.*;

public class JogoUno extends JFrame {
    private UnoJogo engine;
    private JPanel painelMao;
    private JButton btnCompra;
    private JLabel lblCartaTopo;

    public JogoUno() {
        engine = new UnoJogo(4);
        configurarJanela();
        atualizarInterface();
    }

    private void configurarJanela() {
        setLayout(new BorderLayout());
        
        // Centro: Mesa com a carta atual
        JPanel mesa = new JPanel();
        lblCartaTopo = new JLabel();
        lblCartaTopo.setFont(new Font("Arial", Font.BOLD, 40));
        btnCompra = new JButton("COMPRAR CARTA");
        btnCompra.addActionListener(e -> {
            engine.comprar(engine.getJogadorAtual());
            atualizarInterface();
        });
        
        mesa.add(new JLabel("Mesa:"));
        mesa.add(lblCartaTopo);
        mesa.add(btnCompra);
        add(mesa, BorderLayout.CENTER);

        // Sul: Mao do jogador humano (Jogador 0)
        painelMao = new JPanel();
        add(painelMao, BorderLayout.SOUTH);
        
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void atualizarInterface() {
        // Atualiza Carta do Topo
        CartaUno topo = engine.getCartaTopo();
        lblCartaTopo.setText(topo.getValor() + " " + engine.getCorAtual());
        lblCartaTopo.setForeground(traduzirCor(engine.getCorAtual()));

        // Atualiza Botões da Mão
        painelMao.removeAll();
        framework.Mao maoHumano = engine.getJogador(0).getMao();
        
        for (int i = 0; i < maoHumano.tamanho(); i++) {
            final int index = i;
            CartaUno c = (CartaUno) maoHumano.getCartas().get(i);
            JButton btn = new JButton(c.getValor());
            btn.setForeground(traduzirCor(c.getTipo()));
            
            // Apenas habilita se for a vez do humano e a carta for válida
            btn.setEnabled(engine.getJogadorAtual() == 0);
            
            btn.addActionListener(e -> {
                if (engine.jogar(0, index)) {
                    atualizarInterface();
                    // Se for bot, aqui dispararíamos o timer para as jogadas automáticas
                }
            });
            painelMao.add(btn);
        }
        
        revalidate();
        repaint();
    }

    private Color traduzirCor(String corNome) {
        return switch (corNome) {
            case ConstantsUNO.VERMELHO -> Color.RED;
            case ConstantsUNO.VERDE -> Color.GREEN;
            case ConstantsUNO.AZUL -> Color.BLUE;
            case ConstantsUNO.AMARELO -> Color.ORANGE;
            default -> Color.BLACK;
        };
    }

    public static void main(String[] args) {
        new JogoUno().setVisible(true);
    }
}
