package cartas.truco;

import cartas.framework.observer.ObserverJogo;
import cartas.framework.modelo.Jogador;
import cartas.truco.CartaTruco;

/**
 * Esta interface herda tudo do framework e adiciona os eventos do Truco.
 */
public interface ObserverTruco extends ObserverJogo {
    
    // Avisa que alguém pediu Truco/Seis/Nove...
    void aoPedirAposta(Jogador<CartaTruco> jogador, int novoValor);

    // Avisa se o adversário aceitou, correu ou aumentou
    void aoResponderAposta(Jogador<CartaTruco> jogador, String resposta);
}
