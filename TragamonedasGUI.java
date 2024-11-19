import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TragamonedasGUI extends JFrame {
    private JPanel[] rodillos;
    private Timer[] timers;
    private JButton botonGirar;
    private Random random;
    private String[] simbolos = {"🔔", "🍒", "⭐", "7️⃣", "💎"};
    private int[] currentIndex;
    private boolean girando;
    
    public TragamonedasGUI() {
        super("Tragamonedas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        random = new Random();
        rodillos = new JPanel[3];
        timers = new Timer[3];
        currentIndex = new int[3];
        
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
        
        add(mainPanel);
        setVisible(true);
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
                "Victoria", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TragamonedasGUI());
    }
}