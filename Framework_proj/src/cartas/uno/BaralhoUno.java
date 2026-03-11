package cartas.uno;

import cartas.framework.modelo.*;

public class BaralhoUno extends Baralho<CartaUno> {

    @Override
    public void inicializar() {
        for (CartaUno.Cor cor : CartaUno.Cor.values()) {
            if (cor == CartaUno.Cor.ESPECIAL) continue;

            // Um "0" por cor
            adicionarCarta(new CartaUno(cor, CartaUno.Valor.ZERO));

            // Dois de cada (1-9, Pular, Inverter, +2)
            for (int i = 0; i < 2; i++) {
                for (int v = 1; v <= 9; v++) 
                    adicionarCarta(new CartaUno(cor, CartaUno.Valor.values()[v]));
                
                adicionarCarta(new CartaUno(cor, CartaUno.Valor.PULAR));
                adicionarCarta(new CartaUno(cor, CartaUno.Valor.INVERTER));
                adicionarCarta(new CartaUno(cor, CartaUno.Valor.MAIS_DOIS));
            }
        }

        // 4 Coringas e 4 Mais Quatro
        for (int i = 0; i < 4; i++) {
            adicionarCarta(new CartaUno(CartaUno.Cor.ESPECIAL, CartaUno.Valor.CORINGA));
            adicionarCarta(new CartaUno(CartaUno.Cor.ESPECIAL, CartaUno.Valor.MAIS_QUATRO));
        }
    }
}