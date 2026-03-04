package cartas.framework.modelo;

import java.util.ArrayList;
import java.util.List;

// Classe que gerencia a mao do participante
public class Mao<TipoDeCarta extends Carta> {
    
    private List<TipoDeCarta> listaDeCartas = new ArrayList<>();

    public void adicionarCarta(TipoDeCarta carta) {
        if (carta != null) {
            this.listaDeCartas.add(carta);
        }
    }

    public boolean removerCarta(TipoDeCarta carta) {
        return this.listaDeCartas.remove(carta);
    }

    public void limparMao() {
        this.listaDeCartas.clear();
    }

    public int obterQuantidadeDeCartas() {
        return this.listaDeCartas.size();
    }

    public List<TipoDeCarta> obterCartas() {
        return this.listaDeCartas;
    }
}
