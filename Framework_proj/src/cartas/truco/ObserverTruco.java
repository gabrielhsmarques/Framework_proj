package cartas.truco;

import cartas.framework.modelo.Jogador;
import cartas.framework.observer.ObserverJogo;

// Interface que herda tudo do framework e adiciona os eventos do truco
public interface ObserverTruco extends ObserverJogo {
    
    // Avisa que alguém pediu Truco/Seis/Nove/Doze
    void aoPedirAposta(Jogador<CartaTruco> jogador, int novoValor);

    // Avisa se o adversario aceitou, correu ou aumentou
    void aoResponderAposta(Jogador<CartaTruco> jogador, String resposta);

    // Avisa que a equipe em mao de onze deve decidir se vai jogar a mao ou nao
    void aoPedirDecisaoMaoDeOnze(int equipe, int pontos);
}
