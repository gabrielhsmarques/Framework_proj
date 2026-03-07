package cartas.truco;

import cartas.framework.modelo.Baralho;
import cartas.framework.modelo.Jogador;
import cartas.framework.modelo.Pontuacao;
import cartas.framework.motor.Partida;
import cartas.framework.observer.ObserverJogo;
import java.util.List;
import javax.swing.JOptionPane;

public class PartidaTruco extends Partida<CartaTruco> {

    private int valorDaMao = 1; // Comeca em 1, pode ir para 3, 6, 9, 12
    private int rodadaAtual = 0;
    RegrasTruco regrasTruco = (RegrasTruco) regras;
    
    // Controle das 3 rodadas de uma mao
    private int[] vitoriasDeRodada; // Armazena quem ganhou a 1, 2 e 3 rodada

    public PartidaTruco() {
        // Passando as regras do truco para o construtor do Framework
        super(new RegrasTruco());
        vitoriasDeRodada = new int[3];
    }

    // Dizendo ao Framework qual baralho usar
    @Override
    protected Baralho<CartaTruco> criarBaralho() {
        return new BaralhoTruco();
    }

    @Override
    public void iniciarPartida() {
        super.iniciarPartida(); // Chama a distribuição de cartas do framework

    // Verificamos se alguém está na Mão de Onze
        int pA = getPontosEquipeA();
        int pB = getPontosEquipeB();

        if (pA == 11 && pB == 11) {
        // Mão de Ferro: Valor volta a ser 1 e ninguém vê as cartas (regra oficial)
            valorDaMao = 1;
        } else if (pA == 11) {
            // Mão de Onze: A mão já começa valendo 3!
            valorDaMao = 3;
            notificarDecisaoMaoDeOnze(1);
        
        }else if (pB == 11) {
            // Mão de Onze: A mão já começa valendo 3!
            valorDaMao = 3;
            notificarDecisaoMaoDeOnze(2);
        
        } else {
        valorDaMao = 1; // Mão normal
        }
    }

    public void prepararEIniciar(){

        configurarPontuacaoEquipes();

        iniciarPartida();
    }

    // Configuracao para dois jogadores da mesma equipe pontuarem juntos
    public void configurarPontuacaoEquipes() {
        Pontuacao equipeA = new Pontuacao();
        Pontuacao equipeB = new Pontuacao();

        // Jogadores 0 e 2 compartilham o objeto equipeA
        jogadores.get(0).setPontuacao(equipeA);
        jogadores.get(2).setPontuacao(equipeA);

        // Jogadores 1 e 3 compartilham o objeto equipeB
        jogadores.get(1).setPontuacao(equipeB);
        jogadores.get(3).setPontuacao(equipeB);
}

    public void responderAposta(int equipeQueResponde, boolean aceitou) {
        int indiceRepresentante = (equipeQueResponde == 1) ? 0 : 1;
        Jogador<CartaTruco> quemRespondeu = jogadores.get(indiceRepresentante);
        if (aceitou) {
            // Se aceitou, apenas notifica a tela para seguir o jogo
            notificarRespostaAposta(quemRespondeu, "ACEITOU!");
        } else {
            // Se correu, a mao acaba
            notificarRespostaAposta(quemRespondeu, "CORREU!");
            equipeCorreu(equipeQueResponde); 
        }
    }

    public void notificarRespostaAposta(Jogador<CartaTruco> autorDaResposta, String resposta) {
        for (ObserverJogo obs : observadores) {
            if (obs instanceof ObserverTruco observerTruco) {
                observerTruco.aoResponderAposta(autorDaResposta, resposta);
            }
        }
    }
    private void notificarDecisaoMaoDeOnze(int equipe) {
        for (ObserverJogo obs : observadores) {
            if (obs instanceof ObserverTruco observerTruco) {
                observerTruco.aoPedirDecisaoMaoDeOnze(equipe, 3);
            }
        }
    }

    public void notificarAposta(Jogador<CartaTruco> autor, int valor) {
        // Buscando na lista de observadores do framework
        for (ObserverJogo obs : observadores) {
            // Verificando se o observer e um observer do truco
            if (obs instanceof ObserverTruco observerTruco) {
                observerTruco.aoPedirAposta(autor, valor);
            }
        }
    }

    // Metodo para aumentar a aposta
    public void aumentarValorDaAposta() {
        Jogador<CartaTruco> autor = obterJogadorDaVez();
        int equipeAutor = (jogadores.indexOf(autor) % 2 == 0) ? 1 : 2;
        int pontosEquipe = (equipeAutor == 1) ? getPontosEquipeA() : getPontosEquipeB();

        if (pontosEquipe == 11) {
            JOptionPane.showMessageDialog(null, "ERRO FATAL: Pediu Truco na Mão de Onze! Você PERDEU O JOGO.");
        
            // Da a vitória direta para o adversario
            int equipeVencedora = (equipeAutor == 1) ? 2 : 1;
            finalizarJogoDireto(equipeVencedora);
            return;
        }

        if (valorDaMao == 1) valorDaMao = 3;
        else if (valorDaMao < 12) valorDaMao += 3;
        notificarAposta(autor, valorDaMao);
    }

    // Metodo auxiliar para encerrar o jogo
    private void finalizarJogoDireto(int equipeVencedora) {
        if (equipeVencedora == 1) {
            notificarFinalizacao(jogadores.get(0));
        } else {
            notificarFinalizacao(jogadores.get(1));
        }
    }

    public void equipeCorreu(int equipeQueCorreu) {
        int pontosParaVencedor;
        int pontosDaEquipeQueCorreu = (equipeQueCorreu == 1) ? getPontosEquipeA() : getPontosEquipeB();

        if (pontosDaEquipeQueCorreu == 11 && valorDaMao == 3) {
            pontosParaVencedor = 1;
        }

        // Se o valor na mesa era 3, quem ganha leva 1 ponto
        else if (valorDaMao == 3) {
            pontosParaVencedor = 1;
        } 
        // Se era 6, 9 ou 12, quem ganha leva o valor anterior
        else {
            pontosParaVencedor = valorDaMao - 3;
        }

        // Se a equipe A correu, a B vence. Se a B correu, a A vence
        int equipeVencedora = (equipeQueCorreu == 1) ? 2 : 1;
        
        // Ajustando o valor da mao para o valor 1
        valorDaMao = pontosParaVencedor;
        finalizarMao(equipeVencedora);
    }

    // Define quem ganhou a rodada
    public void finalizarRodada() {
        int resultado = regrasTruco.quemGanhouARodada(verMesa());

        if(resultado != -1){
            Jogador<CartaTruco> jogadorVencedor = quemJogouNaMesa.get(resultado);
        
            // Encontra a posicao do ganhador na lista de jogadores
            int indiceRealVencedor = -1;
            for (int i = 0; i < jogadores.size(); i++) {
                if (jogadores.get(i) == jogadorVencedor) {
                    indiceRealVencedor = i;
                    break;
                }
            }

            // definindo a vez desse jogador
            if (indiceRealVencedor != -1) {
                gerenciadorDeTurnos.definirVez(indiceRealVencedor);
            
                // Atribui vitoria a equipe 0 e 2 = Equipe 1 | 1 e 3 = Equipe 2
                int equipeVencedora = (indiceRealVencedor % 2 == 0) ? 1 : 2;
                vitoriasDeRodada[rodadaAtual] = equipeVencedora;
            }
        } else {
            vitoriasDeRodada[rodadaAtual] = 0; // Empate
        }

        rodadaAtual++;
        limparMesa(); // Limpa a lista quemJogouNaMesa do framework
        verificarFimDaMao();

        // Notifica a interface grafica
        if (rodadaAtual > 0 && rodadaAtual < 3) {
            notificarMudancaVez(obterJogadorDaVez());
        }
    }
    // Define quem ganhou a mao
    private void verificarFimDaMao() {
        int vA = 0, vB = 0;
        for (int i = 0; i < rodadaAtual; i++) {
            if (vitoriasDeRodada[i] == 1) vA++;
            else if (vitoriasDeRodada[i] == 2) vB++;
        // Se vitoriasDeRodada[i] for 0, amarrou a rodada
        }

        // CONDICOES DE VITORIA DA MAO
    
        // Alguem ganhou 2 rodadas
        if (vA == 2) {
            finalizarMao(1);
        } 
        else if (vB == 2) {
            finalizarMao(2);
        }
        // Empatou a primeira rodada? Quem ganhar a segunda leva a mao
        else if (vitoriasDeRodada[0] == 0 && rodadaAtual == 2) {
            if (vitoriasDeRodada[1] != 0) {
                finalizarMao(vitoriasDeRodada[1]);
            }
        }
        // Chegou na terceira rodada? Quem ganhar a terceira leva
        else if (rodadaAtual == 3) {
            // Se a terceira tambem empatar, ganha quem ganhou a primeira
            if (vitoriasDeRodada[2] != 0) {
                finalizarMao(vitoriasDeRodada[2]);
            } else {
                finalizarMao(vitoriasDeRodada[0]); 
            }
        }
    }

    // Atribui pontos a equipe vencedora e comeca outra mao
    private void finalizarMao(int equipeVencedora) {
        
        if (equipeVencedora == 1){ // Equipe A  
            jogadores.get(0).adicionarPontos(valorDaMao);
        }else{ // equipe B
            jogadores.get(1).adicionarPontos(valorDaMao);
        }

        if(regras.verificarVencedor(jogadores)){
            notificarFinalizacao(jogadores.get(equipeVencedora == 1 ? 0 : 1));
        }

        indiceQuemComecaAMao = (indiceQuemComecaAMao + 1) % jogadores.size();
        // Reseta para a proxima mao
        rodadaAtual = 0;
        vitoriasDeRodada = new int[3];
        valorDaMao = 1;

        iniciarPartida();       
        // Avisa o Framework para dar cartas nova
    }

    // Getters para a pontuacao no swing
    public int getPontosEquipeA() { return jogadores.get(0).obterPontos(); }
    public int getPontosEquipeB() { return jogadores.get(1).obterPontos(); }
    public int getValorDaMao() { return valorDaMao; }

    public List<Jogador<CartaTruco>> getjogadores() {
        return jogadores;}
}
