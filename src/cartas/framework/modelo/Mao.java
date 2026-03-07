package cartas.framework.modelo;

import java.util.ArrayList;
import java.util.List;

// Classe que gerencia a mao do participante
public class Mao<TipoDeCarta extends Carta> {
    
    private List<TipoDeCarta> listaDeCartas = new ArrayList<>();

    public void adicionarCarta(TipoDeCarta carta) {
        if (carta != null) {
            listaDeCartas.add(carta);
        }
    }

    public boolean removerCarta(TipoDeCarta carta) {
        return listaDeCartas.remove(carta);
    }

    public void limparMao() {
        listaDeCartas.clear();
    }

    public int obterQuantidadeDeCartas() {
        return listaDeCartas.size();
    }

    public List<TipoDeCarta> obterCartas() {
        return listaDeCartas;
    }
}
