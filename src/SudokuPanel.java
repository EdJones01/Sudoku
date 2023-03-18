import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

public class SudokuPanel extends JPanel implements ActionListener, MouseListener, KeyListener, MouseMotionListener {
    private int[][] grid = new int[9][9];
    private int tileSize;
    private boolean[][] startingValues;
    private Point mouseLocation;
    private boolean gameOver;

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
        grid = sudokuPuzzle.getGrid();
        startingValues = sudokuPuzzle.getStartingValue();
        gameOver = false;
        repaint();
    }

    private int getTile(int x, int y) {
        return grid[y][x];
    }

    private void setTile(int x, int y, int value) {
        if (validMove(x, y, value))
            grid[y][x] = value;
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

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] != 0) {
                    if (!startingValues[row][col])
                        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
                    else
                        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30f));
                    Tools.centerString(g2, new Rectangle(x + col * tileSize, y + row *
                            tileSize, tileSize, tileSize), 0, 0, Integer.toString(grid[row][col]));
                }
            }
        }

        if (gameOver) {
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(getBackground());
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40f));
            Tools.centerString(g2, getBounds(), "You Win!");
        }
    }

    private boolean validMove(int x, int y, int value) {
        if (value == 0) {
            return true;
        }

        for (int i = 0; i < 9; i++) {
            if (getTile(x, i) == value || getTile(i, y) == value) {
                return false;
            }
        }

        int subGridRow = y / 3 * 3;
        int subGridColumn = x / 3 * 3;
        for (int i = subGridRow; i < subGridRow + 3; i++) {
            for (int j = subGridColumn; j < subGridColumn + 3; j++) {
                if (getTile(j, i) == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWin() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (grid[i][j] == 0)
                    return false;
        return true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            return;

        String character = "" + e.getKeyChar();
        try {
            int num = Integer.parseInt(character);
            setTile(mouseLocation, num);
            if (checkWin()) {
                gameOver = true;
            }
            repaint();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseLocation = new Point(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        new InputWorker().execute();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setupPuzzle(Integer.parseInt(e.getActionCommand()));
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