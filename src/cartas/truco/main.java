package cartas.truco;

import cartas.truco.ui.TelaTruco;

public class main {
    public static void main(String[] args) {
        PartidaTruco jogo = new PartidaTruco();
        
        jogo.adicionarJogador("Jogador 1 (A)");
        jogo.adicionarJogador("Jogador 2 (B)");
        jogo.adicionarJogador("Jogador 3 (A)");
        jogo.adicionarJogador("Jogador 4 (B)");

        new TelaTruco(jogo);
        
        jogo.prepararEIniciar();
    }
    
}
