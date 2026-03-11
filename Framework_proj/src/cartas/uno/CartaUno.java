package cartas.uno;

import cartas.framework.modelo.*;

public class CartaUno extends Carta {
    public enum Cor { VERMELHO, AZUL, VERDE, AMARELO, ESPECIAL }
    public enum Valor { 
        ZERO, UM, DOIS, TRES, QUATRO, CINCO, SEIS, SETE, OITO, NOVE, 
        PULAR, INVERTER, MAIS_DOIS, CORINGA, MAIS_QUATRO 
    }

    private Cor cor;
    private final Valor valor;

    public CartaUno(Cor cor, Valor valor) {
        this.cor = cor;
        this.valor = valor;
    }

    public Cor getCor() { return cor; }
    public void setCor(Cor cor) { this.cor = cor; } // Permite mudar cor do Coringa
    public Valor getValor() { return valor; }

    @Override
    public String obterCarta() {
        return cor + " " + valor;
    }
}
