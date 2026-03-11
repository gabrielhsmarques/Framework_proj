package cartas.truco.ui;

import cartas.framework.modelo.Carta;
import cartas.framework.modelo.Jogador;
import cartas.truco.CartaTruco;
import cartas.truco.ObserverTruco;
import cartas.truco.PartidaTruco;
import java.awt.*;
import javax.swing.*;

public class TelaTruco extends JFrame implements ObserverTruco {

    private PartidaTruco partida;
    private JPanel painelMesa;
    private JPanel painelMao;
    private JLabel labelPlacar;
    private JLabel labelInfo;

    public TelaTruco(PartidaTruco partida) {
        this.partida = partida;
        // Registro obrigatorio no framework para receber as notificacoes
        this.partida.registrarObservador(this);

        configurarJanela();
        inicializarComponentes();
        
        this.setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Framework de Cartas - Truco");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // Area superior: placar e valor da mao
        JPanel painelTopo = new JPanel(new GridLayout(2, 1));
        labelPlacar = new JLabel("Equipe A: 0 | Equipe B: 0", SwingConstants.CENTER);
        labelPlacar.setFont(new Font("Arial", Font.BOLD, 18));
        labelInfo = new JLabel("Valor da Mão: 1", SwingConstants.CENTER);
        painelTopo.add(labelPlacar);
        painelTopo.add(labelInfo);
        add(painelTopo, BorderLayout.NORTH);

        // Area central: mesa
        painelMesa = new JPanel(new FlowLayout());
        painelMesa.setBorder(BorderFactory.createTitledBorder("Mesa da Rodada"));
        painelMesa.setBackground(new Color(34, 139, 34));
        add(painelMesa, BorderLayout.CENTER);

        // Area inferior: mao do jogador e botao de truco
        JPanel painelAcoes = new JPanel(new BorderLayout());
        painelMao = new JPanel(new FlowLayout());
        
        JButton btnTruco = new JButton("PEDIR TRUCO!");
        btnTruco.addActionListener(e -> partida.aumentarValorDaAposta());
        
        painelAcoes.add(painelMao, BorderLayout.CENTER);
        painelAcoes.add(btnTruco, BorderLayout.EAST);
        add(painelAcoes, BorderLayout.SOUTH);
    }

    // METODOS DO OBSERVER

    @Override
    public void aoIniciarPartida() {
        painelMesa.removeAll(); // Limpa cartas da mesa da mao anterior
        painelMao.removeAll();  // Limpa a mao visual para receber a nova
        labelPlacar.setText("Equipe A: " + partida.getPontosEquipeA() + " | Equipe B: " + partida.getPontosEquipeB());
        labelInfo.setText("Partida Iniciada! Valor da Mão: " + partida.getValorDaMao());
        // Forca o Swing a redesenhar do zero
        revalidate();
        repaint();
    }

    @Override
    public void aoMudarVez(Jogador<?> jogadorDaVez) {
        painelMao.removeAll();
        setTitle("Vez de: " + jogadorDaVez.obterNome());

        for (Object c : jogadorDaVez.verMao().obterCartas()) {
            CartaTruco carta = (CartaTruco) c;
            BotaoCarta btn = new BotaoCarta(carta);
            
            btn.addActionListener(e -> {
                Jogador<CartaTruco> jogadorAtual = (Jogador<CartaTruco>) partida.obterJogadorDaVez();
                boolean sucesso = partida.realizarJogada(jogadorAtual, carta);
    
                if (sucesso) {
                    if (partida.verMesa().size() == 4) {
                    // Desativa os botoes imediatamente para evitar 2 clique
                        painelMao.removeAll();
                        painelMao.repaint();
            
                        Timer timer = new Timer(1500, evt -> {
                            partida.finalizarRodada();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            });
            painelMao.add(btn);
        }
        revalidate();
        repaint();
    }

    @Override
    public void aoJogarCarta(Jogador<?> jogador, Carta carta) {
        JLabel labelCarta = new JLabel(carta.obterCarta());
        labelCarta.setOpaque(true);
        labelCarta.setBackground(Color.WHITE);
        labelCarta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        labelCarta.setPreferredSize(new Dimension(80, 100));
        labelCarta.setHorizontalAlignment(SwingConstants.CENTER);
        
        painelMesa.add(labelCarta);
        revalidate();
        repaint();
    }

    @Override
    public void aoPedirAposta(Jogador<CartaTruco> autor, int novoValor) {
        // Identifica quem deve responder
        int indiceAutor = partida.getjogadores().indexOf(autor);
        int equipeAdversaria = (indiceAutor % 2 == 0) ? 2 : 1;

        // Abre uma caixa de dialogo
        String[] opcoes = {"Aceitar", "Correr"};
        int escolha = JOptionPane.showOptionDialog(
            this, 
            autor.obterNome() + " pediu " + novoValor + "! O que a Equipe " + equipeAdversaria + " faz?",
            "PEDIDO DE APOSTA",
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, opcoes, opcoes[0]
        );

        // Envia a resposta para o motor do jogo
        boolean aceitou = (escolha == 0); // Aceitar
        partida.responderAposta(equipeAdversaria, aceitou);
    }

    @Override
    public void aoPedirDecisaoMaoDeOnze(int equipe, int valor) {    
        String[] opcoes = {"Aceitar (Vale 3)", "Fugir (Dá 1 ponto ao adversário)"};
    
        int escolha = JOptionPane.showOptionDialog(
            this,
            "MÃO DE ONZE PARA EQUIPE " + (equipe == 1 ? "A" : "B") + "!\n" +
            "Desejam jogar esta mão valendo " + valor + " pontos?",
            "Decisão Crítica",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null, opcoes, opcoes[0]
    );

    if (escolha == 1) { // Fugir
        // Se a equipe de 11 pontos fugir, o adversario ganha 1 ponto
        partida.equipeCorreu(equipe);
    }
    // Se aceitar, o jogo apenas continua normalmente valendo 3
}
    
    @Override
    public void aoResponderAposta(Jogador<CartaTruco> jogador, String resposta) {

        JOptionPane.showMessageDialog(this, jogador.obterNome() + " respondeu: " + resposta);
    
        labelInfo.setText(jogador.obterNome() + " " + resposta);
    }

    @Override
    public void aoLimparMesa() {
        painelMesa.removeAll(); // Remove os JLabels antigos da tela
        labelPlacar.setText("Equipe A: " + partida.getPontosEquipeA() + 
                       " | Equipe B: " + partida.getPontosEquipeB());
    
        // Forca o Swing a perceber a mudança
        painelMesa.revalidate();
        painelMesa.repaint();
    }

    @Override
    public void aoFinalizarJogo(Jogador<?> vencedor) {
        String equipe = (partida.getjogadores().indexOf(vencedor) % 2 == 0) ? "EQUIPE A" : "EQUIPE B";
    
        JOptionPane.showMessageDialog(this, 
            "FIM DE JOGO!\nO vencedor é: " + vencedor.obterNome() + 
            "\nVitória da " + equipe + "!", 
            "🏆 CAMPEÃO", 
            JOptionPane.INFORMATION_MESSAGE);
    
        // Fechar o jogo 
        System.exit(0); 
    }
}
