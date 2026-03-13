package cartas.framework.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// Classe abstrata que define o comportamento de qualquer baralho
public abstract class Baralho<TipoDeCarta extends Carta> {

    protected List<TipoDeCarta> listaDeCartas = new ArrayList<>();

    // Metodo que o jogo vai implementar ao criar o baralho
    public Baralho() {
        inicializar();
    }

    public abstract void inicializar();

    public void adicionarCarta(TipoDeCarta carta) {
        listaDeCartas.add(carta);
    }

    public void embaralhar() {
        Collections.shuffle(listaDeCartas);
    }

    public TipoDeCarta comprarCarta() {
        if (listaDeCartas.isEmpty()) return null;
        return listaDeCartas.remove(0);
    }
}
    