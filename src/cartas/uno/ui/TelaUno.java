package cartas.uno.ui;

import cartas.framework.modelo.Carta;
import cartas.framework.modelo.Jogador;
import cartas.framework.observer.ObserverJogo;
import cartas.uno.CartaUno;
import cartas.uno.PartidaUno;
import java.awt.*;
import javax.swing.*;

public class TelaUno extends JFrame implements ObserverJogo {

    private PartidaUno partida;
    private JPanel painelMesa;
    private JPanel painelMao;
    private JLabel labelStatus;
    private JLabel labelTopoMesa;

    public TelaUno(PartidaUno partida) {
        this.partida = partida;
        this.partida.registrarObservador(this); // Registro no Framework
        configurarJanela();
        inicializarComponentes();
        this.setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Framework de Cartas - UNO (UFU)");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JPanel painelTopo = new JPanel();
        labelStatus = new JLabel("Aguardando início...", SwingConstants.CENTER);
        labelStatus.setFont(new Font("Arial", Font.BOLD, 18));
        painelTopo.add(labelStatus);
        add(painelTopo, BorderLayout.NORTH);

        painelMesa = new JPanel(new GridBagLayout());
        painelMesa.setBackground(new Color(50, 50, 50));
        labelTopoMesa = new JLabel("MESA VAZIA");
        labelTopoMesa.setForeground(Color.WHITE);
        labelTopoMesa.setFont(new Font("Arial", Font.BOLD, 22));
        painelMesa.add(labelTopoMesa);
        add(painelMesa, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelMao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JScrollPane scrollMao = new JScrollPane(painelMao);
        scrollMao.setPreferredSize(new Dimension(1000, 180));
        
        JButton btnComprar = new JButton("COMPRAR CARTA");
        btnComprar.addActionListener(e -> partida.jogadorComprarCarta());

        painelInferior.add(scrollMao, BorderLayout.CENTER);
        painelInferior.add(btnComprar, BorderLayout.EAST);
        add(painelInferior, BorderLayout.SOUTH);
    }

    @Override
    public void aoMudarVez(Jogador<?> jogadorDaVez) {
        labelStatus.setText("Vez de: " + jogadorDaVez.obterNome());
        painelMao.removeAll();

        for (Object obj : jogadorDaVez.verMao().obterCartas()) {
            CartaUno carta = (CartaUno) obj;
            BotaoCartaUno btn = new BotaoCartaUno(carta); // Usa as cores corrigidas
            
            btn.addActionListener(e -> {
                // VOLTOU A LÓGICA DE VALIDAÇÃO:
                if (partida.realizarJogada((Jogador<CartaUno>) jogadorDaVez, carta)) {
                    if (carta.getCor() == CartaUno.Cor.ESPECIAL) {
                        solicitarEscolhaDeCor(carta);
                    }
                    partida.getGerenciadorDeTurnos().avancar(); // Avança turno no framework
                    aoMudarVez(partida.obterJogadorDaVez());
                } else {
                    // MENSAGEM DE ERRO RESTAURADA:
                    JOptionPane.showMessageDialog(this, 
                        "Jogada Inválida! A carta deve ter a mesma COR ou VALOR do topo.", 
                        "Regras do Uno", JOptionPane.ERROR_MESSAGE);
                }
            });
            painelMao.add(btn);
        }
        painelMao.revalidate();
        painelMao.repaint();
    }

    @Override public void aoIniciarPartida() { labelStatus.setText("Jogo Iniciado!"); }
    
    @Override
    public void aoJogarCarta(Jogador<?> j, Carta c) {
        labelTopoMesa.setText("TOPO: " + c.obterCarta());
        if (c instanceof CartaUno cu) atualizarCorMesa(cu.getCor());
    }

    @Override public void aoLimparMesa() { labelTopoMesa.setText("MESA LIMPA"); }
    
    @Override public void aoFinalizarJogo(Jogador<?> vencedor) {
        JOptionPane.showMessageDialog(this, "UNO! Vencedor: " + vencedor.obterNome());
        System.exit(0);
    }

    private void solicitarEscolhaDeCor(CartaUno carta) {
        String[] cores = {"VERMELHO", "AZUL", "VERDE", "AMARELO"};
        int escolha = JOptionPane.showOptionDialog(this, "Escolha a nova cor:", "Coringa",
                0, JOptionPane.QUESTION_MESSAGE, null, cores, cores[0]);
        if (escolha != -1) {
            carta.setCor(CartaUno.Cor.values()[escolha]);
            atualizarCorMesa(carta.getCor());
        }
    }

    private void atualizarCorMesa(CartaUno.Cor cor) {
        switch (cor) {
            case VERMELHO -> painelMesa.setBackground(new Color(180, 0, 0));
            case AZUL -> painelMesa.setBackground(new Color(0, 0, 180));
            case VERDE -> painelMesa.setBackground(new Color(0, 120, 0));
            case AMARELO -> painelMesa.setBackground(new Color(200, 200, 0));
            default -> painelMesa.setBackground(Color.BLACK);
        }
    }
}