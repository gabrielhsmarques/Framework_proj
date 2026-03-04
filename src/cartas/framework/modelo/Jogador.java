package cartas.framework.modelo;


// Classe que define o jogador e seus atributos
public class Jogador<TipoDeCarta extends Carta> {
    
    private String nome;
    private Mao<TipoDeCarta> mao;

    public Jogador(String nome) {
        this.nome = nome;
        this.mao = new Mao<>(); //comeca com a mao vazia
    }

    public void receberCarta(TipoDeCarta carta) {
        this.mao.adicionarCarta(carta);
    }

    public String obterNome() {
        return this.nome;
    }

    public Mao<TipoDeCarta> verMao() {
        return this.mao;
    }
}