package cartas.uno.ui;

import javax.swing.JButton;
import cartas.uno.CartaUno;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

public class BotaoCartaUno extends JButton {
    private CartaUno carta;

    public BotaoCartaUno(CartaUno carta) {
        this.carta = carta;
        this.setText(carta.getValor().toString());
        this.setPreferredSize(new Dimension(100, 140));
        this.setFont(new Font("Arial", Font.BOLD, 20));
        this.setForeground(Color.WHITE);
        
        // Lógica para converter o nome em símbolo curto
    String rotulo = switch (carta.getValor()) {
        case MAIS_DOIS -> "+2";
        case MAIS_QUATRO -> "+4";
        case PULAR -> "Ø"; // Símbolo de pular
        case INVERTER -> "⇄"; // Símbolo de inverter
        case CORINGA -> "WILD";
        default -> String.valueOf(carta.getValor().ordinal() < 10 ? carta.getValor().ordinal() : carta.getValor());
    };
    
    this.setText(rotulo);
         // ESTA LINHA REMOVE O RETÂNGULO BEGE/FOCO:
        this.setFocusPainted(false);
        
        // --- AJUSTE PARA CORES NO LINUX ---
        this.setContentAreaFilled(true);
        this.setOpaque(true);
        this.setBorderPainted(true);

        // --- Ajustes de estilização ---
        // cursor de maozinha 
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Define a cor baseada na carta
        switch (carta.getCor()) {
        case VERMELHO -> setBackground(new Color(220, 20, 60));
        case AZUL -> setBackground(new Color(30, 144, 255));
        case VERDE -> setBackground(new Color(34, 139, 34));
        case AMARELO -> setBackground(new Color(218, 165, 32)); // Um amarelo levemente mais escuro para o texto branco ler melhor
        case ESPECIAL -> setBackground(Color.BLACK);
    }
}

    public CartaUno getCarta() { return carta; }
}