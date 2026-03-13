package cartas.uno;

import cartas.uno.ui.TelaUno;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainUno {
    public static void main(String[] args) {
        // Ajusta o visual para o padrão do sistema operacional (Ubuntu/Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Garante que a GUI execute na thread correta do Swing
        SwingUtilities.invokeLater(() -> {
            // 1. Instancia o motor do jogo (Framework + Regras de Uno)
            PartidaUno partida = new PartidaUno();

            // 2. Adiciona os jogadores (Exemplo com 4 jogadores)
            partida.adicionarJogador("Mary");
            partida.adicionarJogador("Gabriel");
            partida.adicionarJogador("Jogador1");
            partida.adicionarJogador("Jogador2");

            // 3. Cria a Interface Gráfica e passa a partida para ela
            // O registro do Observer já acontece dentro do construtor da TelaUno
            new TelaUno(partida);

            // 4. Inicia a partida (Baralho é criado, cartas distribuídas e observers notificados)
            partida.iniciarPartida();
        });
    }
}