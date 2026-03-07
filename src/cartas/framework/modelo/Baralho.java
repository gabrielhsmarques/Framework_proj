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
        this.listaDeCartas.add(carta);
    }

    public void embaralhar() {
        Collections.shuffle(this.listaDeCartas);
    }

    public TipoDeCarta comprarCarta() {
        if (this.listaDeCartas.isEmpty()) return null;
        return this.listaDeCartas.remove(0);
    }
}
    