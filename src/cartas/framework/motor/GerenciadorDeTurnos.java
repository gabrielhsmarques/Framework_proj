package cartas.framework.motor;

public class GerenciadorDeTurnos {
    private int jogadorAtual = 0;
    private int totalJogadores;
    private int direcao = 1; // 1 para sentido horario, -1 para anti-horario

    public GerenciadorDeTurnos(int totalJogadores) {
        this.totalJogadores = totalJogadores;
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
        if (indiceJogador >= 0 && indiceJogador < totalJogadores) {
            this.jogadorAtual = indiceJogador;
        }
    }

    public int obterJogadorAtual() {
        return jogadorAtual;
    }
}
