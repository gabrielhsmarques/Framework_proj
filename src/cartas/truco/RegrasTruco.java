package cartas.truco;

import cartas.framework.modelo.Jogador;
import cartas.framework.motor.ValidadorDeRegras;
import cartas.truco.CartaTruco;
import java.util.List;

public class RegrasTruco implements ValidadorDeRegras<CartaTruco> {

    @Override
    public boolean ehJogadaValida(CartaTruco cartaParaJogar, List<CartaTruco> cartasNaMesa) {
        // Qualquer carta pode ser jogada a qualquer momento
        return true; 
    }

    @Override
    public int quantidadeDeCartasIniciais() {
        return 3; // Comeca com 3 cartas
    }

    @Override
    public boolean verificarVencedor(List<Jogador<CartaTruco>> jogadores) {
        // Aqui voce verificaria se alguém atingiu 12 pontos.
        // Por enquanto, vamos deixar simples para você testar.
        return false; 
    }
    
    // Qual das cartas na mesa ganhou a rodada
    public int quemGanhouARodada(List<CartaTruco> cartasNaMesa) {
        int indiceVencedor = 0;
        int maiorForca = -1;
        boolean amarradoValido = false;

        for (int i = 0; i < cartasNaMesa.size(); i++) {
            int forcaAtual = cartasNaMesa.get(i).getForca();
            
            if (forcaAtual > maiorForca) {
                maiorForca = forcaAtual;
                indiceVencedor = i;
                amarradoValido = false;
            }else if (forcaAtual == maiorForca){
                int equipeVencedorAtual = indiceVencedor % 2;
                int equipeQueAmarrou = i % 2;

                if (equipeVencedorAtual != equipeQueAmarrou){
                    amarradoValido = true;
                }
            } 
        }
        return amarradoValido ? -1 : indiceVencedor;
    }
}
