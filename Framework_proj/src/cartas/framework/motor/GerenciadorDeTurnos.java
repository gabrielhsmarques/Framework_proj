package cartas.framework.motor;

public class GerenciadorDeTurnos {
    private int jogadorAtual;
    private int totalJogadores;
    private int direcao = 1; // 1 para sentido horario, -1 para anti-horario

    public GerenciadorDeTurnos(int totalJogadores) {
        this.totalJogadores = totalJogadores;
        this.jogadorAtual = 0;
    }

    // Passa a vez para o proximo jogador da sequencia
    public void avancar() {
        jogadorAtual = (jogadorAtual + direcao + totalJogadores) % totalJogadores;
    }

    // Pula o próximo jogador da vez
    public void pularVez() {
        avancar();
        avancar(); 
    }

    // Inverte a ordem da mesa
    public void mudarDirecao() {
        this.direcao *= -1;
    }

    // Define exatamente quem deve jogar agora 
    public void definirVez(int indiceJogador) {
        jogadorAtual = indiceJogador % totalJogadores;
    }

    public int obterJogadorAtual() {
        return jogadorAtual;
    }

}
