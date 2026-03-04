package cartas.framework.motor;

import cartas.framework.modelo.*;
import java.util.ArrayList;
import java.util.List;

// Esta classe controla o fluxo principal de qualquer jogo de cartas
public abstract class Partida<TipoCarta extends Carta> {
    
    protected Baralho<TipoCarta> baralho;
    protected List<Jogador<TipoCarta>> jogadores;
    protected GerenciadorDeTurnos gerenciadorDeTurnos;
    protected ValidadorDeRegras<TipoCarta> regras;
    protected List<TipoCarta> mesa;

    // O baralho sera inicializado pelo jogo especifico
    public Partida(ValidadorDeRegras<TipoCarta> regras) {
        this.jogadores = new ArrayList<>();
        this.regras = regras;
        this.mesa = new ArrayList<>();
    }

    // Cada jogo decide qual baralho usar
    protected abstract Baralho<TipoCarta> criarBaralho();

    public void adicionarJogador(String nome) {
        jogadores.add(new Jogador<>(nome));
    }

    // Embaralhar e dar as cartas
    public void iniciarPartida() {
        if (jogadores.isEmpty()) return;

        baralho.embaralhar();
        gerenciadorDeTurnos = new GerenciadorDeTurnos(jogadores.size());

        // Distribui as cartas automaticamente usando a regra do jogo
        for (Jogador<TipoCarta> jogador : jogadores) {
            for (int i = 0; i < regras.quantidadeDeCartasIniciais(); i++) {
                jogador.receberCarta(baralho.comprarCarta());
            }
        }
    }

    public Jogador<TipoCarta> obterJogadorDaVez() {
        return jogadores.get(gerenciadorDeTurnos.obterJogadorAtual());
    }

    public TipoCarta obterCartaNoTopo() {
        if (mesa.isEmpty()) return null;
        return mesa.get(mesa.size() - 1);
    }

    // Realiza a jogada. Se a regra permitir, a carta vai para a mesa
    public boolean realizarJogada(Jogador<TipoCarta> jogador, TipoCarta carta) {
        if (regras.ehJogadaValida(carta, verMesa())) { 
            this.mesa.add(carta); 
            jogador.verMao().removerCarta(carta);
            gerenciadorDeTurnos.avancar();
            return true;
        }
        return false;
    }

    public List<TipoCarta> verMesa() {
        return this.mesa;
    }

    public void limparMesa() {
        this.mesa.clear();
    }
}
