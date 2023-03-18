import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NumberInputFrame {
    private String userInput = null;

    private void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setFocusable(true);
        panel.requestFocus();

        panel.setPreferredSize(new Dimension(250, 250));
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();

                userInput = button.getText();
                frame.dispose();
            }
        };

        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.setFont(button.getFont().deriveFont(Font.PLAIN, 30));
            button.addActionListener(buttonListener);
            button.setFocusable(false);
            panel.add(button);
        }

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String character = "" + e.getKeyChar();
                try {
                    Integer.parseInt(character);
                    userInput = character;
                    frame.dispose();
                } catch (Exception ignored) {
                    frame.dispose();
                }
            }
        });

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    public int getUserInput() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
        while (userInput == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Integer.parseInt(userInput);
    }
}
