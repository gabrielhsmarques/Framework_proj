package cartas.framework.motor;

import cartas.framework.modelo.*;
import cartas.framework.observer.ObserverJogo;
import java.util.ArrayList;
import java.util.List;

// Esta classe controla o fluxo principal de qualquer jogo de cartas
public abstract class Partida<TipoCarta extends Carta> {
    
    protected Baralho<TipoCarta> baralho;
    protected List<Jogador<TipoCarta>> jogadores = new ArrayList<>();
    public GerenciadorDeTurnos gerenciadorDeTurnos;
    protected List<Jogador<TipoCarta>> quemJogouNaMesa = new ArrayList<>();
    protected ValidadorDeRegras<TipoCarta> regras;
    protected List<TipoCarta> mesa;
    protected int indiceQuemComecaAMao = 0;

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

        baralho = criarBaralho();
        baralho.embaralhar();

        gerenciadorDeTurnos = new GerenciadorDeTurnos(jogadores.size());
        gerenciadorDeTurnos.definirVez(indiceQuemComecaAMao);

        // Distribui as cartas automaticamente usando a regra do jogo
        for (Jogador<TipoCarta> jogador : jogadores) {
            jogador.verMao().limparMao();
            for (int i = 0; i < regras.quantidadeDeCartasIniciais(); i++) {
                jogador.receberCarta(baralho.comprarCarta());
            }
        }
        limparMesa();
        notificarInicio();
        notificarMudancaVez(obterJogadorDaVez());
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
            mesa.add(carta);
            quemJogouNaMesa.add(jogador);
            jogador.verMao().removerCarta(carta);

            notificarJogada(jogador, carta);

            if (this.mesa.size() < jogadores.size()) {
                gerenciadorDeTurnos.avancar();
                notificarMudancaVez(obterJogadorDaVez());
            }
            
            return true;
        }
        return false;
    }

    public List<TipoCarta> verMesa() {
        return this.mesa;
    }

    public void limparMesa() {
        mesa.clear();
        quemJogouNaMesa.clear();
        for (ObserverJogo obs : observadores) {
            obs.aoLimparMesa();
        }
    }

    protected void notificarFinalizacao(Jogador<TipoCarta> vencedor) {
        for (ObserverJogo obs : observadores) {
            obs.aoFinalizarJogo(vencedor);
        }
    }

    // Lista de observadores
    protected List<ObserverJogo> observadores = new ArrayList<>();

    public void registrarObservador(ObserverJogo observador) {
        this.observadores.add(observador);
    }

    // Métodos auxiliares de aviso observer
    protected void notificarInicio() {
        for (ObserverJogo o : observadores) o.aoIniciarPartida();
    }

    protected void notificarJogada(Jogador<TipoCarta> j, TipoCarta c) {
        for (ObserverJogo o : observadores) o.aoJogarCarta(j, c);
    }

    protected void notificarMudancaVez(Jogador<TipoCarta> j) {
        for (ObserverJogo o : observadores) o.aoMudarVez(j);
    }
}
