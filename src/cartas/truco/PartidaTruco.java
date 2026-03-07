package cartas.truco;

import cartas.framework.motor.Partida;
import cartas.framework.observer.ObserverJogo;
import cartas.framework.modelo.Baralho;
import cartas.framework.modelo.Jogador;
import cartas.truco.CartaTruco;
import cartas.truco.BaralhoTruco;
import cartas.truco.RegrasTruco;

public class PartidaTruco extends Partida<CartaTruco> {

    private int valorDaMao = 1; // Começa em 1, pode ir para 3, 6, 9, 12
    private int pontosEquipeA = 0;
    private int pontosEquipeB = 0;
    
    // Controle das 3 rodadas (vazas) de uma mão
    private int[] vitoriasDeRodada; // Armazena quem ganhou a 1ª, 2ª e 3ª (1 para A, 2 para B)
    private int rodadaAtual;

    public PartidaTruco() {
        // Passando as regras do Truco para o construtor do Framework
        super(new RegrasTruco());
        vitoriasDeRodada = new int[3];
        rodadaAtual = 0;
    }

    // Dizendo ao Framework qual baralho usar
    @Override
    protected Baralho<CartaTruco> criarBaralho() {
        return new BaralhoTruco();
    }

    private void notificarAposta(Jogador<CartaTruco> autor, int valor) {
        // Buscamos na lista de observadores do pai (framework)
        for (ObserverJogo obs : this.observadores) {
            // Verificamos se o observador é um Observador de Truco
            if (obs instanceof ObserverTruco) {
                ((ObserverTruco) obs).aoPedirAposta(autor, valor);
            }
        }
    }

    // Metodo para aumentar a aposta
    public void aumentarValorDaAposta() {
        if (valorDaMao == 1) valorDaMao = 3;
        else if (valorDaMao < 12) valorDaMao += 3;
        // Avisa a interface gráfica para mostrar o balão de "TRUCO!"
        notificarAposta(obterJogadorDaVez(), valorDaMao);
    }

    public void equipeCorreu(int equipeQueCorreu) {
        int pontosParaVencedor;

        // Se o valor na mesa era 3 (Truco), quem ganha leva 1 ponto.
        if (valorDaMao == 3) {
            pontosParaVencedor = 1;
        } 
        // Se era 6, 9 ou 12, quem ganha leva o valor anterior (valor - 3).
        else {
            pontosParaVencedor = valorDaMao - 3;
        }

        // Se a equipe A correu, a B vence. Se a B correu, a A vence.
        int equipeVencedora = (equipeQueCorreu == 1) ? 2 : 1;
        
        // Ajustamos o valor da mão para o valor da "fuga" e encerramos a mão
        this.valorDaMao = pontosParaVencedor;
        finalizarMao(equipeVencedora);
    }

    /**
     * No Truco, após 3 cartas na mesa (ou 4 se for 2x2), 
     * precisamos processar quem ganhou a vaza.
     */
    public void finalizarRodada() {
        RegrasTruco regrasTruco = (RegrasTruco) regras;
        int resultado = regrasTruco.quemGanhouARodada(verMesa());

        if(resultado == -1){
            vitoriasDeRodada[rodadaAtual] = 0;
        }
        else{
            // Descobre de qual equipe é o vencedor
            int equipeVencedora = (resultado % 2 == 0) ? 1 : 2;
            vitoriasDeRodada[rodadaAtual] = equipeVencedora;

            gerenciadorDeTurnos.definirVez(resultado); // O vencedor da rodada começa a proxima
        }
        
        rodadaAtual++;
        limparMesa();

        verificarFimDaMao();
    }

    private void verificarFimDaMao() {
        int vA = 0, vB = 0;
        for (int v : vitoriasDeRodada) {
            if (v == 1) vA++;
            if (v == 2) vB++;
        }

        // Regra básica: quem fez 2 rodadas ganha a mão
        if (vA == 2 || (vA == 1 && vitoriasDeRodada[0] == 1 && vitoriasDeRodada[1] == 0)) {
            finalizarMao(1);
        } else if (vB == 2 || (vB == 1 && vitoriasDeRodada[0] == 2 && vitoriasDeRodada[1] == 0)) {
            finalizarMao(2);
        } else if (rodadaAtual == 3) {
            // Se chegou na 3ª rodada e ninguém fez 2, checa quem ganhou a última
            finalizarMao(vitoriasDeRodada[2]);
        }
    }


    private void finalizarMao(int equipeVencedora) {
        if (equipeVencedora == 1) pontosEquipeA += valorDaMao;
        else if (equipeVencedora == 2) pontosEquipeB += valorDaMao;

        // Reseta para a próxima mão
        rodadaAtual = 0;
        vitoriasDeRodada = new int[3];
        valorDaMao = 1;
        
        // Avisa o Framework para dar cartas novas
        iniciarPartida();
    }

    // Getters para a pontuacao (o Swing vai usar isso)
    public int getPontosEquipeA() { return pontosEquipeA; }
    public int getPontosEquipeB() { return pontosEquipeB; }
    public int getValorDaMao() { return valorDaMao; }
}
