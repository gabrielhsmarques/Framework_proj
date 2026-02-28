package cartas.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * O Baralho é uma coleção de cartas. 
 * Usamos <TipoDeCarta> para que o baralho aceite qualquer classe que herde de Carta.
 */
public class Baralho<TipoDeCarta extends Carta> {

    // Lista que armazena as cartas
    private List<TipoDeCarta> listaDeCartas = new ArrayList<>();

    // Adiciona uma carta nova ao monte
    public void adicionarCarta(TipoDeCarta carta) {
        this.listaDeCartas.add(carta);
    }

    // Mistura as cartas
    public void embaralhar() {
        Collections.shuffle(this.listaDeCartas);
    }

    // Remove e retorna a primeira carta da lista (o topo do baralho)
    public TipoDeCarta comprarCarta() {
        if (this.listaDeCartas.isEmpty()) {
            return null; // Caso o baralho esteja vazio
        }
        return this.listaDeCartas.remove(0);
    }

    // Retorna quantas cartas ainda existem no baralho
    public int quantidadeDeCartas() {
        return this.listaDeCartas.size();
    }
}
    