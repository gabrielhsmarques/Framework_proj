package cartas.framework.observer;

import cartas.framework.modelo.Carta;
import cartas.framework.modelo.Jogador;

// Interface que a Interface Grafica deve implementar para ser avisada sobre os acontecimentos do jogo
public interface ObserverJogo {
    
    // Avisa que a partida começou 
    void aoIniciarPartida();

    // Avisa que um jogador realizou uma jogada valida
    void aoJogarCarta(Jogador<?> jogador, Carta carta);

    // Avisa que o turno mudou e agora e a vez de outro jogador
    void aoMudarVez(Jogador<?> jogador);

    // Avisa que a mesa foi limpa
    void aoLimparMesa();

    // Avisa que o jogo terminou e temos um vencedor
    void aoFinalizarJogo(Jogador<?> vencedor);
}