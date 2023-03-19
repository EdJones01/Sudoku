import javax.swing.*;
import java.awt.event.ActionListener;

public class SudokuMenuBar extends JMenuBar {
    private ActionListener actionListener;

    public SudokuMenuBar(ActionListener actionListener) {
        this.actionListener = actionListener;

        add(new JLabel("Difficulty:"));
        addGap(10);
        addButtonToBar("Easy", ""+SudokuPuzzle.EASY);
        addButtonToBar("Medium", ""+SudokuPuzzle.MEDIUM);
        addButtonToBar("Hard", ""+SudokuPuzzle.HARD);
        addButtonToBar("Expert", ""+SudokuPuzzle.EXPERT);
        addGap(20);
        addButtonToBar("Help", "help");
    }

    private void addButtonToBar(String name, String cmd) {
        JButton button = new JButton(name);
        button.addActionListener(actionListener);
        button.setActionCommand(cmd);
        button.setFocusable(false);
        add(button);
        addGap(5);
    }

    private void addGap(int size) {
        add(Box.createHorizontalStrut(size));
    }
}
