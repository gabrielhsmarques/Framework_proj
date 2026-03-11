package cartas.uno;

import cartas.framework.motor.*;
import cartas.framework.modelo.*;

public class PartidaUno extends Partida<CartaUno> {

    private boolean jaComprouNoTurno = false;

    public PartidaUno() {
        super(new RegrasUno());
    }

    @Override
    protected Baralho<CartaUno> criarBaralho() {
        return new BaralhoUno();
    }

    @Override
    public void iniciarPartida() {
        // 1. Distribui as cartas iniciais (7 para cada)
        super.iniciarPartida(); 

        // 2. Sorteia a primeira carta da mesa (Não pode ser ESPECIAL/Coringa)
        CartaUno primeiraCarta;
        do {
            primeiraCarta = baralho.comprarCarta();
            if (primeiraCarta.getCor() == CartaUno.Cor.ESPECIAL) {
                // Se for coringa, descarta ou devolve ao baralho e tenta outra
                // Aqui estamos apenas pegando a próxima
            }
        } while (primeiraCarta.getCor() == CartaUno.Cor.ESPECIAL);

        // 3. Coloca a carta válida na mesa
        mesa.add(primeiraCarta);
        
        // 4. Notifica a Interface para pintar a cor da mesa e mostrar a carta
        notificarJogada(null, primeiraCarta); 
        notificarMudancaVez(obterJogadorDaVez());
    }

    @Override
    public boolean realizarJogada(Jogador<CartaUno> jogador, CartaUno carta) {
        if (super.realizarJogada(jogador, carta)) {
            processarEfeito(carta);
            jaComprouNoTurno = false; // Reset ao jogar com sucesso
            
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
            default -> { }
        }
    }

    public void comprarParaJogadorAtual(int qtd) {
        Jogador<CartaUno> atual = obterJogadorDaVez();
        for (int i = 0; i < qtd; i++) {
            atual.receberCarta(baralho.comprarCarta());
        }
    }

    public int jogadorComprarCarta() {
        if (jaComprouNoTurno) return -1; // Bloqueio: já comprou uma vez

        Jogador<CartaUno> atual = obterJogadorDaVez();
        CartaUno comprada = baralho.comprarCarta();
        
        if (comprada != null) {
            atual.receberCarta(comprada);
            jaComprouNoTurno = true; // Marca que o limite de compra foi atingido
            notificarMudancaVez(atual); 

            // Verifica se a carta comprada serve na mesa
            if (regras.ehJogadaValida(comprada, mesa)) {
                return 1; // Pode jogar agora se quiser
            } else {
                return 0; // Não serve, a interface deve passar a vez
            }
        }
        return -2; // Baralho vazio
    }

    public boolean passarVezManualmente() {
        if (jaComprouNoTurno) {
            jaComprouNoTurno = false; // Reseta para o próximo
            gerenciadorDeTurnos.avancar();
            notificarMudancaVez(obterJogadorDaVez());
            return true;
        }
        return false; // Bloqueado: tem que comprar antes de passar
    }
}