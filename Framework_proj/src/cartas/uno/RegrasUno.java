package cartas.uno;

import cartas.framework.motor.*;
import cartas.framework.modelo.*;
import java.util.List;

public class RegrasUno implements ValidadorDeRegras<CartaUno> {

    @Override
    public boolean ehJogadaValida(CartaUno carta, List<CartaUno> mesa) {
        if (mesa.isEmpty()) return true;
        CartaUno topo = mesa.get(mesa.size() - 1);

        // Coringa sempre pode ser jogado
        if (carta.getCor() == CartaUno.Cor.ESPECIAL) return true;

        // Mesma cor ou mesmo valor/símbolo
        return carta.getCor() == topo.getCor() || carta.getValor() == topo.getValor();
    }

    @Override
    public boolean verificarVencedor(List<Jogador<CartaUno>> jogadores) {
        for (Jogador<CartaUno> j : jogadores) {
            if (j.verMao().obterQuantidadeDeCartas() == 0) return true;
        }
        return false;
    }

    @Override
    public int quantidadeDeCartasIniciais() { return 7; }
}