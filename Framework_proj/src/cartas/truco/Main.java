package cartas.truco;

import cartas.truco.ui.TelaTruco;

// 1. Classe com letra Maiúscula (Padrão Java)
public class Main { 
    public static void main(String[] args) {
        PartidaTruco jogo = new PartidaTruco();
        
        jogo.adicionarJogador("Jogador 1 (A)");
        jogo.adicionarJogador("Jogador 2 (B)");
        jogo.adicionarJogador("Jogador 3 (A)");
        jogo.adicionarJogador("Jogador 4 (B)");

        // 2. Apenas instanciamos a tela. Ela já se registra no jogo.
        new TelaTruco(jogo);
        
        jogo.prepararEIniciar();
    }
}