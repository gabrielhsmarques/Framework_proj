package cartas.framework.modelo;

public class Pontuacao {
    private int valor = 0;

    public void adicionar(int pontos){
        valor += pontos;
    }

    public int getValor(){
        return valor;
    }
    
}
