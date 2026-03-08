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
        this.setFont(new Font("Arial", Font.BOLD, 12));

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
            case VERMELHO -> { setBackground(new Color(255, 0, 0)); setForeground(Color.WHITE); }
            case AZUL -> { setBackground(new Color(0, 0, 255)); setForeground(Color.WHITE); }
            case VERDE -> { setBackground(new Color(0, 150, 0)); setForeground(Color.WHITE); }
            case AMARELO -> { setBackground(new Color(255, 220, 0)); setForeground(Color.BLACK); }
            case ESPECIAL -> { setBackground(Color.BLACK); setForeground(Color.WHITE); }
        }
    }

    public CartaUno getCarta() { return carta; }
}