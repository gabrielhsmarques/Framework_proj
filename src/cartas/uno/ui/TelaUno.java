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
        painelMesa.setBackground(new Color(45, 45, 45));

        labelTopoMesa = new JLabel("MESA VAZIA");
        labelTopoMesa.setForeground(Color.WHITE);
        labelTopoMesa.setFont(new Font("Arial", Font.BOLD, 22));
        painelMesa.add(labelTopoMesa);
        add(painelMesa, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelMao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JScrollPane scrollMao = new JScrollPane(painelMao);
        scrollMao.setPreferredSize(new Dimension(1000, 180));
        
        // PAINEL DE BOTÕES DE AÇÃO (Comprar e Passar)
        JPanel painelBotoesAcao = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JButton btnComprar = new JButton("COMPRAR CARTA");
        btnComprar.addActionListener(e -> acaoComprar());

        JButton btnPassar = new JButton("PASSAR VEZ");
    btnPassar.addActionListener(e -> {
    if (!partida.passarVezManualmente()) {
        JOptionPane.showMessageDialog(this, 
            "Você só pode passar a vez depois de comprar uma carta!", 
            "Regra do Uno", 
            JOptionPane.WARNING_MESSAGE);
    }
    });
        painelBotoesAcao.add(btnComprar);
        painelBotoesAcao.add(btnPassar);

        painelInferior.add(scrollMao, BorderLayout.CENTER);
        painelInferior.add(painelBotoesAcao, BorderLayout.EAST);
        add(painelInferior, BorderLayout.SOUTH);
    }

    // Método para tratar a lógica de compra com mensagens na interface
    private void acaoComprar() {
        int resultado = partida.jogadorComprarCarta();
        
        if (resultado == -1) {
            JOptionPane.showMessageDialog(this, "Você já comprou uma carta neste turno!", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else if (resultado == 0) {
            JOptionPane.showMessageDialog(this, "A carta comprada não serve. Passando a vez...", "Uno", JOptionPane.INFORMATION_MESSAGE);
            // O turno já é avançado pela lógica da PartidaUno
        } else if (resultado == 1) {
            JOptionPane.showMessageDialog(this, "Você comprou uma carta que pode ser jogada!", "Boa!", JOptionPane.INFORMATION_MESSAGE);
        } else if (resultado == -2) {
            JOptionPane.showMessageDialog(this, "O baralho está vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void aoMudarVez(Jogador<?> jogadorDaVez) {
        labelStatus.setText("Vez de: " + jogadorDaVez.obterNome());
        painelMao.removeAll();

        for (Object obj : jogadorDaVez.verMao().obterCartas()) {
            CartaUno carta = (CartaUno) obj;
            BotaoCartaUno btn = new BotaoCartaUno(carta); 
            
            btn.addActionListener(e -> {
                if (partida.realizarJogada((Jogador<CartaUno>) jogadorDaVez, carta)) {
                    if (carta.getCor() == CartaUno.Cor.ESPECIAL) {
                        solicitarEscolhaDeCor(carta);
                    }
                    partida.gerenciadorDeTurnos.avancar(); 
                    aoMudarVez(partida.obterJogadorDaVez());
                } else {
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

    @Override 
    public void aoIniciarPartida() {
         labelStatus.setText("Jogo Iniciado!"); 
         // Garante que a mesa comece neutra ao iniciar
        painelMesa.setBackground(new Color(45, 45, 45));}
    
    

    @Override 
    public void aoLimparMesa() { labelTopoMesa.setText("MESA LIMPA"); }
    
    @Override public void aoFinalizarJogo(Jogador<?> vencedor) {
        JOptionPane.showMessageDialog(this, "UNO! Vencedor: " + vencedor.obterNome());
        System.exit(0);
    }

    @Override
    public void aoJogarCarta(Jogador<?> j, Carta c) { // Verifique se o nome é EXATAMENTE esse
        labelTopoMesa.setText("TOPO: " + c.obterCarta());
        if (c instanceof CartaUno cu) atualizarCorMesa(cu.getCor());
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
        // Se a mesa estiver limpa ou for especial sem cor definida, volta ao neutro
        default -> painelMesa.setBackground(new Color(45, 45, 45));
    }
}
}