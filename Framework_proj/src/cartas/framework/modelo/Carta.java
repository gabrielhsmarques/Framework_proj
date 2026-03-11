package cartas.framework.modelo;

// Representa o conceito geral de uma carta de jogo. Cada jogo dira como sua carta deve ser exibida
public abstract class Carta {

    public abstract String obterCarta();

    @Override
    public String toString() {
        return obterCarta();
    }
}

