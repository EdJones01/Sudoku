import javax.swing.*;
import java.awt.event.ActionListener;

public class SudokuMenuBar extends JMenuBar {
    private ActionListener actionListener;

    public SudokuMenuBar(ActionListener actionListener) {
        this.actionListener = actionListener;

        JMenu fileMenu = new JMenu("File");

        fileMenu.add(createMenuItem("Save", "save"));
        fileMenu.add(createMenuItem("Load", "load"));
        fileMenu.add(createMenuItem("Help", "help"));

        JMenu newGameMenu = new JMenu("New Game");

        newGameMenu.add(createMenuItem("Easy", ""+SudokuPuzzle.EASY));
        newGameMenu.add(createMenuItem("Medium", ""+SudokuPuzzle.MEDIUM));
        newGameMenu.add(createMenuItem("Hard", ""+SudokuPuzzle.HARD));
        newGameMenu.add(createMenuItem("Expert", ""+SudokuPuzzle.EXPERT));

        add(fileMenu);
        add(newGameMenu);

        addGap(10);

        add(createButton("Pencil", "pencil"));
        add(createButton("Highlighting", "highlighting"));
    }

    private JButton createButton(String name, String cmd) {
        JButton button = new JButton(name);
        button.addActionListener(actionListener);
        button.setActionCommand(cmd);
        button.setFocusable(false);
        return button;
    }

    private JMenuItem createMenuItem(String name, String cmd) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener(actionListener);
        menuItem.setActionCommand(cmd);
        return menuItem;
    }

    private void addGap(int size) {
        add(Box.createHorizontalStrut(size));
    }
}
