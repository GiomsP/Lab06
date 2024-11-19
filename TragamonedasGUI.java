import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class JuegosDeAzarGUI extends JFrame {
    private JPanel[] rodillos;
    private Timer[] timers;
    private JButton botonGirar;
    private Random random;
    private String[] simbolos = {"🔔", "🍒", "⭐", "7️⃣", "💎"};
    private int[] currentIndex;
    private boolean girando;

    public JuegosDeAzarGUI() {
        super("Juegos de Azar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        random = new Random();
        rodillos = new JPanel[3];
        timers = new Timer[3];
        currentIndex = new int[3];

        String[] opciones = {"Jugar Dados", "Jugar Tragamonedas", "Ambos Juegos"};
        int seleccion = JOptionPane.showOptionDialog(
            null,
            "Seleccione una opción:",
            "Juegos de Azar",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        if (seleccion == 0) {
            iniciarJuegoDeDados();
        } else if (seleccion == 1) {
            iniciarTragamonedas();
        } else if (seleccion == 2) {
            iniciarAmbosJuegos();
        }
    }

    private void iniciarJuegoDeDados() {
        StringBuilder resultado = new StringBuilder("\n--- Juego de Dados ---\n");
        for (int i = 1; i <= 5; i++) { // 5 tiradas
            int dado1 = random.nextInt(6) + 1; // Número entre 1 y 6
            int dado2 = random.nextInt(6) + 1;
            int suma = dado1 + dado2;
            resultado.append("Tirada ").append(i)
                .append(": Dado 1 = ").append(dado1)
                .append(", Dado 2 = ").append(dado2)
                .append(" (Suma: ").append(suma).append(")");

     
            if (suma == 7 || suma == 11) {
                resultado.append(" -> ¡Ganaste!\n");
            } else {
                resultado.append(" -> Perdiste.\n");
            }
        }
        JOptionPane.showMessageDialog(this, resultado.toString(), "Resultado del Juego de Dados", JOptionPane.INFORMATION_MESSAGE);
    }

    private void iniciarTragamonedas() {
        // Mostrar el tragamonedas en una nueva ventana
        JFrame tragamonedasFrame = new JFrame("Tragamonedas");
        tragamonedasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tragamonedasFrame.setSize(400, 300);
        tragamonedasFrame.setLocationRelativeTo(this);


        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel rodillosPanel = new JPanel(new GridLayout(1, 3, 10, 0));

   
        for (int i = 0; i < 3; i++) {
            rodillos[i] = new JPanel();
            rodillos[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            rodillos[i].setPreferredSize(new Dimension(100, 150));
            JLabel label = new JLabel(simbolos[0], SwingConstants.CENTER);
            label.setFont(new Font("Dialog", Font.PLAIN, 48));
            rodillos[i].add(label);
            rodillosPanel.add(rodillos[i]);

            final int index = i;
            timers[i] = new Timer(100, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    actualizarRodillo(index);
                }
            });
        }


        botonGirar = new JButton("GIRAR");
        botonGirar.setFont(new Font("Dialog", Font.BOLD, 20));
        botonGirar.addActionListener(e -> iniciarGiro());

        mainPanel.add(rodillosPanel, BorderLayout.CENTER);
        mainPanel.add(botonGirar, BorderLayout.SOUTH);

        tragamonedasFrame.add(mainPanel);
        tragamonedasFrame.setVisible(true);
    }

    private void iniciarGiro() {
        if (girando) return;
        girando = true;
        botonGirar.setEnabled(false);

        for (int i = 0; i < timers.length; i++) {
            final int index = i;
            Timer delayTimer = new Timer(i * 1000, e -> {
                timers[index].start();
                // Detener después de un tiempo aleatorio
                Timer stopTimer = new Timer(2000 + random.nextInt(1000), e2 -> {
                    timers[index].stop();
                    if (index == 2) {
                        verificarResultado();
                    }
                });
                stopTimer.setRepeats(false);
                stopTimer.start();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    private void actualizarRodillo(int index) {
        currentIndex[index] = (currentIndex[index] + 1) % simbolos.length;
        JLabel label = (JLabel) rodillos[index].getComponent(0);
        label.setText(simbolos[currentIndex[index]]);
    }

    private void verificarResultado() {
        girando = false;
        botonGirar.setEnabled(true);

  
        if (currentIndex[0] == currentIndex[1] && currentIndex[1] == currentIndex[2]) {
            JOptionPane.showMessageDialog(this, "¡FELICIDADES! ¡GANASTE!",
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lo siento, perdiste.",
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void iniciarAmbosJuegos() {
        iniciarJuegoDeDados();
        iniciarTragamonedas();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegosDeAzarGUI());
    }
}
