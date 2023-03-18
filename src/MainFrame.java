import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SudokuPanel panel = new SudokuPanel();
        setJMenuBar(new SudokuMenuBar(panel));
        panel.setPreferredSize(new Dimension(600, 600));
        setResizable(false);
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }
}