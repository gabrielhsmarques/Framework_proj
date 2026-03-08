package cartas.uno;

import cartas.framework.motor.*;
import cartas.framework.modelo.*;

public class PartidaUno extends Partida<CartaUno> {

    public PartidaUno() {
        super(new RegrasUno());
    }

    @Override
    protected Baralho<CartaUno> criarBaralho() {
        return new BaralhoUno();
    }

    @Override
    public boolean realizarJogada(Jogador<CartaUno> jogador, CartaUno carta) {
        if (super.realizarJogada(jogador, carta)) {
            processarEfeito(carta);
            
            if (regras.verificarVencedor(jogadores)) {
                notificarFinalizacao(jogador);
            }
            return true;
        }
        return false;
    }

private void processarEfeito(CartaUno carta) {
    switch (carta.getValor()) {
        case PULAR -> gerenciadorDeTurnos.pularVez();
        case INVERTER -> gerenciadorDeTurnos.mudarDirecao();
        case MAIS_DOIS -> {
            gerenciadorDeTurnos.avancar();
            comprarParaJogadorAtual(2);
        }
        case MAIS_QUATRO -> {
            gerenciadorDeTurnos.avancar();
            comprarParaJogadorAtual(4);
        }
        default -> { 
            // Nenhuma ação para ZERO, UM, DOIS, etc.
        }
    }
}

    public void comprarParaJogadorAtual(int qtd) {
        Jogador<CartaUno> atual = obterJogadorDaVez();
        for (int i = 0; i < qtd; i++) {
            atual.receberCarta(baralho.comprarCarta());
        }
    }

    public void jogadorComprarCarta() {
    Jogador<CartaUno> atual = obterJogadorDaVez();
    CartaUno comprada = baralho.comprarCarta();
    
    if (comprada != null) {
        atual.receberCarta(comprada);
        // Notifica a GUI que a mão mudou (você pode usar o observer para isso)
        notificarMudancaVez(atual); 
    }
}
}
