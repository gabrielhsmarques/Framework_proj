package cartas.framework.modelo;


// Classe que define o jogador e seus atributos
public class Jogador<TipoDeCarta extends Carta> {
    
    private String nome;
    private Mao<TipoDeCarta> mao;
    private Pontuacao pontuacao;

    public Jogador(String nome) {
        this.nome = nome;
        this.mao = new Mao<>(); //comeca com a mao vazia
    }

    public void setPontuacao(Pontuacao p){
        pontuacao = p;
    }

    public void adicionarPontos(int pontos){
        pontuacao.adicionar(pontos);
    }

    public int obterPontos(){
        return pontuacao.getValor();
    }

    public void receberCarta(TipoDeCarta carta) {
        mao.adicionarCarta(carta);
    }

    public String obterNome() {
        return nome;
    }

    public Mao<TipoDeCarta> verMao() {
        return mao;
    }
}