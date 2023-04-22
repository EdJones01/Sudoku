import javax.swing.*;
import java.awt.event.ActionListener;

public class SudokuMenuBar extends JMenuBar {
    private ActionListener actionListener;

    public SudokuMenuBar(ActionListener actionListener) {
        this.actionListener = actionListener;

        JMenu fileMenu = new JMenu("File");

        fileMenu.add(createButton("Save", "save"));
        fileMenu.add(createButton("Load", "load"));
        fileMenu.add(createButton("Help", "help"));

        JMenu newGameMenu = new JMenu("New Game");

        newGameMenu.add(createButton("Easy", ""+SudokuPuzzle.EASY));
        newGameMenu.add(createButton("Medium", ""+SudokuPuzzle.MEDIUM));
        newGameMenu.add(createButton("Hard", ""+SudokuPuzzle.HARD));
        newGameMenu.add(createButton("Expert", ""+SudokuPuzzle.EXPERT));

        add(fileMenu);
        add(newGameMenu);
    }

    private JButton createButton(String name, String cmd) {
        JButton button = new JButton(name);
        button.addActionListener(actionListener);
        button.setActionCommand(cmd);
        button.setFocusable(false);
        return button;
    }
}
