package cartas.truco.ui;

import javax.swing.JButton;
import cartas.truco.CartaTruco;
import java.awt.Color;
import java.awt.Font;

public class BotaoCarta extends JButton {
    private CartaTruco carta;

    public BotaoCarta(CartaTruco carta) {
        this.carta = carta;
        this.setText(carta.obterCarta());
        this.setFont(new Font("Arial", Font.BOLD, 12));
        this.setBackground(Color.WHITE);
        this.setFocusPainted(false);
    }

    public CartaTruco getCarta() {
        return carta;
    }
}