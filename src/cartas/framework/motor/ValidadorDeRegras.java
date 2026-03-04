package cartas.framework.motor;

import cartas.framework.modelo.Carta;
import cartas.framework.modelo.Jogador;
import java.util.List;

// Define o que um jogo precisa implementar para que o framework funcione
public interface ValidadorDeRegras<TipoCarta extends Carta> {
    
    // Valida se e uma jogada permitida dependendo da regra do jogo
    boolean ehJogadaValida(TipoCarta cartaParaJogar, TipoCarta cartaNaMesa);

    // Define quem ganhou a partida ou rodada
    boolean verificarVencedor(List<Jogador<TipoCarta>> jogadores);
    
    // Quantas cartas cada um comeca
    int quantidadeDeCartasIniciais();
}