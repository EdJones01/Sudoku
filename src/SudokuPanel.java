import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

public class SudokuPanel extends JPanel implements ActionListener, MouseListener, KeyListener, MouseMotionListener {
    private Tile[][] grid = new Tile[9][9];
    private int tileSize;
    private boolean[][] startingValues;
    private Point mouseLocation = new Point(0, 0);
    private boolean gameOver;
    private boolean showGuidelines = false;
    private boolean showHighlighting = false;
    private boolean pencilMode = false;

    public SudokuPanel() {
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        requestFocus();
        setupPuzzle(SudokuPuzzle.EASY);
    }

    private void setupPuzzle(int difficulty) {
        SudokuPuzzle sudokuPuzzle = new SudokuPuzzle(difficulty);
        int[][] intGrid = sudokuPuzzle.getGrid();
        grid = new Tile[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new Tile(intGrid[i][j]);
            }
        }

        startingValues = sudokuPuzzle.getStartingValue();
        gameOver = false;
        repaint();
    }

    private Tile getTile(int x, int y) {
        return grid[y][x];
    }

    private void setTile(int x, int y, int value) {
        if (validMove(x, y, value)) {
            grid[y][x].setValue(value);
            removePencilMarks(x, y, value);
            repaint();
        }
    }

    private void removePencilMarks(int x, int y, int value) {
        for(int i = 0; i < 9; i++) {
            grid[i][x].removePencil(value);
        }
        for(int i = 0; i < 9; i++) {
            grid[y][i].removePencil(value);
        }
    }

    private void setTilePencil(Point p, int value) {
        int x = (int) Math.floor(p.getX() / tileSize);
        int y = (int) Math.floor(p.getY() / tileSize);
        setTilePencil(x, y, value);
    }

    private void setTilePencil(int x, int y, int value) {
        if (validMove(x, y, value)) {
            grid[y][x].addPencil(value);
        }
    }

    private void setTile(Point p, int value) {
        int x = (int) Math.floor(p.getX() / tileSize);
        int y = (int) Math.floor(p.getY() / tileSize);
        setTile(x, y, value);
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        tileSize = getWidth() / 9;

        drawGrid(g2);

        if (showGuidelines && mouseWithinPanel())
            drawGuidelines(g2);

        if (showHighlighting && mouseWithinPanel())
            drawHighlighting(g2);

        if (gameOver)
            drawGameOverScreen(g2);
    }

    private void drawGrid(Graphics2D g2) {
        int x = 0;
        int y = 0;
        int thickGridSize = 4;
        int thinGridSize = 1;

        g2.drawRect(x, y, 9 * tileSize, 9 * tileSize);
        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0)
                g2.setStroke(new BasicStroke(thickGridSize));
            else
                g2.setStroke(new BasicStroke(thinGridSize));
            g2.drawLine(x, y + i * tileSize, x + 9 * tileSize, y + i * tileSize);
            g2.drawLine(x + i * tileSize, y, x + i * tileSize, y + 9 * tileSize);
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j].getValue() != 0) {
                    if (startingValues[i][j])
                        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30));
                    else
                        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 26));
                    Tools.centerString(g2, new Rectangle(x + j * tileSize, y + i *
                            tileSize, tileSize, tileSize), 0, 0, Integer.toString(grid[i][j].getValue()));
                } else {
                    Rectangle[] pencilBoxes = new Rectangle[9];

                    int rectSize = tileSize / 3;

                    int index = 0;
                    for (int row = 0; row < 3; row++) {
                        for (int col = 0; col < 3; col++) {
                            int px = (x + j * tileSize) + (col * rectSize);
                            int py = (y + i * tileSize) + (row * rectSize);
                            pencilBoxes[index] = new Rectangle(px, py, rectSize, rectSize);
                            index++;
                        }
                    }

                    for (int k = 0; k < 9; k++) {
                        if (grid[i][j].getPencil()[k]) {
                            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14));
                            Tools.centerString(g2, pencilBoxes[k], 0, 0, String.valueOf(k + 1));
                        }
                    }
                }
            }
        }
    }

    private void drawGuidelines(Graphics2D g2) {
        int selectedX = (int) Math.floor(mouseLocation.getX() / tileSize);
        int selectedY = (int) Math.floor(mouseLocation.getY() / tileSize);
        g2.setColor(new Color(255, 255, 255, 10));
        g2.fillRect(selectedX * tileSize, 0, tileSize, getHeight());
        g2.fillRect(0, selectedY * tileSize, getWidth(), tileSize);
    }

    private void drawHighlighting(Graphics2D g2) {
        int selectedX = (int) Math.floor(mouseLocation.getX() / tileSize);
        int selectedY = (int) Math.floor(mouseLocation.getY() / tileSize);
        int highlightedNumber = getTile(selectedX, selectedY).getValue();
        if (highlightedNumber != 0) {
            g2.setColor(new Color(0, 0, 255, 15));
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (grid[i][j].getValue() == highlightedNumber)
                        g2.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    private void drawGameOverScreen(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(getBackground());
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40f));
        Tools.centerString(g2, getBounds(), "You Win!");
    }

    private boolean mouseWithinPanel() {
        return mouseLocation.getX() > -1 && mouseLocation.getX() < tileSize * 9 &&
                mouseLocation.getY() > -1 && mouseLocation.getY() < tileSize * 9;
    }

    private boolean validMove(int x, int y, int value) {
        if (startingValues[y][x])
            return false;
        if (value == 0)
            return true;

        for (int i = 0; i < 9; i++) {
            if (getTile(x, i).getValue() == value || getTile(i, y).getValue() == value) {
                return false;
            }
        }

        int subGridRow = y / 3 * 3;
        int subGridColumn = x / 3 * 3;
        for (int i = subGridRow; i < subGridRow + 3; i++) {
            for (int j = subGridColumn; j < subGridColumn + 3; j++) {
                if (getTile(j, i).getValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWin() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (grid[i][j].getValue() == 0)
                    return false;
        return true;
    }

    private void showHelpMenu() {
        Tools.showPopup("""
                CONTROLS:

                P: Pencil mode
                G: Show gridlines
                H: Show number highlighting
                C: Fill in pencil numbers""");
    }

    private void fillInPencil() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(grid[j][i].getValue() == 0)
                    for(int k = 1; k < 10; k++)
                        setTilePencil(i, j, k);
            }
        }
    }

    private void save() {
        String saveFilePath = Tools.chooseSaveFilePath() + ".sud";

        StringBuilder data = new StringBuilder();
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String isStartingValue = startingValues[i][j] ? "y" : "n";
                data.append(grid[i][j].getValue()).append(isStartingValue).append(" ");
            }
            data.append("\n");
        }
        try {
            Tools.saveToFile(data.toString(), saveFilePath);
            Tools.showPopup("Game successfully saved!");
        } catch (IOException e) {
            Tools.showPopup("Unable to save to: " + saveFilePath);
        }
    }

    private void load() {
        String loadFilePath = Tools.chooseOpenFilePath("Sudoku saves", new String[] {"sud"});
        try {
            String[] data = Tools.readFromFile(loadFilePath);
            for(int i = 0; i < 9; i++) {
                String row = data[i].replaceAll(" ", "");
                for(int j = 0; j < 9; j++) {
                    int value = Integer.parseInt(String.valueOf(row.charAt(j * 2)));
                    startingValues[i][j] = (String.valueOf(row.charAt(j * 2 + 1))).equals("y");
                    grid[i][j] = new Tile(value);
                }
            }

        } catch (Exception e) {
            Tools.showPopup("Unable to load from: " + loadFilePath);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            return;

        if (e.getKeyCode() == KeyEvent.VK_P) {
            pencilMode = !pencilMode;
            if(pencilMode)
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            else
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            showGuidelines = !showGuidelines;
            repaint();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_H) {
            showHighlighting = !showHighlighting;
            repaint();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_C) {
            fillInPencil();
            repaint();
            return;
        }

        String character = String.valueOf(e.getKeyChar());
        try {
            int num = Integer.parseInt(character);
            if (pencilMode) {
                setTilePencil(mouseLocation, num);
            } else {
                setTile(mouseLocation, num);
                if (checkWin()) {
                    gameOver = true;
                }
            }
            repaint();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseLocation = new Point(e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = (int) (double) (e.getX() / tileSize);
        int y = (int) (double) (e.getY() / tileSize);
        if (!gameOver && !startingValues[y][x]) {
            new InputWorker().execute();
            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("save")) {
            save();
            return;
        }
        if (cmd.equals("load")) {
            load();
            return;
        }
        if (cmd.equals("help")) {
            showHelpMenu();
            return;
        }

        setupPuzzle(Integer.parseInt(cmd));
        repaint();
    }

    private class InputWorker extends SwingWorker<Integer, Void> {
        Point location;

        @Override
        protected Integer doInBackground() {
            location = new Point((int) mouseLocation.getX(), (int) mouseLocation.getY());
            NumberInputFrame buttonInput = new NumberInputFrame();
            return buttonInput.getUserInput();
        }

        @Override
        protected void done() {
            try {
                int input = get();
                if (pencilMode)
                    setTilePencil(location, input);
                else
                    setTile(location, input);
                repaint();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}