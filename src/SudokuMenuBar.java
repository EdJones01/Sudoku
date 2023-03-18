import javax.swing.*;
import java.awt.event.ActionListener;

public class SudokuMenuBar extends JMenuBar {
    public SudokuMenuBar(ActionListener actionListener) {
        add(new JLabel("Difficulty:"));
        addGap(10);
        addDifficultyButton("Easy", SudokuPuzzle.EASY, actionListener);
        addDifficultyButton("Medium", SudokuPuzzle.MEDIUM, actionListener);
        addDifficultyButton("Hard", SudokuPuzzle.HARD, actionListener);
        addDifficultyButton("Expert", SudokuPuzzle.EXPERT, actionListener);
    }

    private void addDifficultyButton(String name, int cmd, ActionListener actionListener) {
        JButton button = new JButton(name);
        button.addActionListener(actionListener);
        button.setActionCommand(""+cmd);
        button.setFocusable(false);
        add(button);
        addGap(5);
    }

    private void addGap(int size) {
        add(Box.createHorizontalStrut(size));
    }
}
