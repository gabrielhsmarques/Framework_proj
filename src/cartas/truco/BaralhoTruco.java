package cartas.truco;

import cartas.framework.modelo.Baralho;

public class BaralhoTruco extends Baralho<CartaTruco>{
    // Criacao do baralho de truco
    @Override
    public void inicializar(){
        String[] naipes = {"Paus", "Copas", "Espadas", "Ouros"};

        String[] faces = {"4", "5", "6", "7", "Q", "J", "K", "As", "2", "3"};

        int[] forcas = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    for (int i = 0; i < 10; i++) {
        for(String naipe : naipes){
            CartaTruco carta = new CartaTruco(faces[i], naipe, forcas[i]);

            // Zap
            if (faces[i].equals("4") && naipe.equals("Paus")) {
                carta = new CartaTruco("4", "Paus", 14);
            } 
            // 7 de Copas 
            else if (faces[i].equals("7") && naipe.equals("Copas")) {
                carta = new CartaTruco("7", "Copas", 13);
            }
            // Espadilha 
            else if (faces[i].equals("As") && naipe.equals("Espadas")) {
                carta = new CartaTruco("As", "Espadas", 12);
            }
            // 7 de Ouros 
            else if (faces[i].equals("7") && naipe.equals("Ouros")) {
                carta = new CartaTruco("7", "Ouros", 11);
            }

            adicionarCarta(carta);
        }
            
        }
    }

    
}
