import javax.swing.*;
import java.awt.*;

public class Dioda extends JComponent {
    private boolean isOn;

    public Dioda() {
        isOn = false;
    }

    public void paint(Graphics g) {

        if (isOn) {
            g.setColor(Color.GREEN);
            g.fillOval(10, 10, 30, 30);
        } else {
            g.setColor(Color.GRAY);
            g.fillOval(10, 10, 30, 30);
        }

        g.setColor(Color.BLACK);
        g.drawOval(5, 5, 40, 40);
    }

    public void diodeOn() {
        isOn = true;
        repaint();
    }

    public void diodeOff() {
        isOn = false;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dioda");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dioda dioda = new Dioda();

        frame.add(dioda);
        frame.setSize(100, 100);
        frame.setVisible(true);
    }
}