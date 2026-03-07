package cartas.truco;

import cartas.framework.modelo.Carta;

public class CartaTruco extends Carta{

    private String face;
    private String naipe;
    private int forca;

    public CartaTruco(String face, String naipe, int forca){
        this.face = face;
        this.naipe = naipe;
        this.forca = forca;
    }

    @Override
    public String obterCarta(){
        return face + "de" + naipe;
    }

    public String getFace() {
        return face;
    }

    public String getNaipe() {
        return naipe;
    }

    public int getForca() {
        return forca;
    }
}
