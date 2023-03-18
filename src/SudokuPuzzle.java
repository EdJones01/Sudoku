import java.util.*;

public class SudokuPuzzle {
    public static final int EASY = 40;
    public static final int MEDIUM = 50;
    public static final int HARD = 55;
    public static final int EXPERT = 60;

    private final Random random = new Random();
    private final int[][] grid = new int[][]{
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {4, 5, 6, 7, 8, 9, 1, 2, 3},
            {7, 8, 9, 1, 2, 3, 4, 5, 6},

            {2, 3, 1, 5, 6, 4, 8, 9, 7},
            {5, 6, 4, 8, 9, 7, 2, 3, 1},
            {8, 9, 7, 2, 3, 1, 5, 6, 4},

            {3, 1, 2, 6, 4, 5, 9, 7, 8},
            {6, 4, 5, 9, 7, 8, 3, 1, 2},
            {9, 7, 8, 3, 1, 2, 6, 4, 5}
    };
    private final boolean[][] startingValue = new boolean[9][9];

    public SudokuPuzzle(int difficulty) {
        generate(difficulty);
    }

    private void generate(int diffuculty) {
        shuffleNumbers();
        shuffleRows();
        shuffleCols();
        shuffle3X3Rows();
        shuffle3X3Cols();

        removeValues(diffuculty);
    }

    public int[][] getGrid() {
        return grid;
    }

    public boolean[][] getStartingValue() {
        return startingValue;
    }

    private void removeValues(int amount) {
        int count = 0;
        while (count != amount) {
            int x = random.nextInt(9);
            int y = random.nextInt(9);
            if (grid[x][y] != 0) {
                grid[x][y] = 0;
                startingValue[x][y] = true;
                count++;
            }
        }
    }

    private boolean isEmpty() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (grid[i][j] == 0)
                    return false;
        return true;
    }

    private void shuffleNumbers() {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = nums.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                int num = grid[x][y];
                if (num != 0) {
                    grid[x][y] = nums[num - 1];
                }
            }
        }
    }


    private void swapNumbers(int num1, int num2) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (grid[x][y] == num1) {
                    grid[x][y] = num2;
                } else if (grid[x][y] == num2) {
                    grid[x][y] = num1;
                }
            }
        }
    }

    private void shuffleRows() {
        int blockNumber;

        for (int i = 0; i < 9; i++) {
            int ranNum = random.nextInt(3);
            blockNumber = i / 3;
            swapRows(i, blockNumber * 3 + ranNum);
        }
    }

    private void swapRows(int row1, int row2) {
        int[] row = grid[row1];
        grid[row1] = grid[row2];
        grid[row2] = row;
    }

    private void shuffleCols() {
        int blockNumber;
        for (int i = 0; i < 9; i++) {
            int ranNum = random.nextInt(3);
            blockNumber = i / 3;
            swapCols(i, blockNumber * 3 + ranNum);
        }
    }

    private void swapCols(int column1, int column2) {
        int value;
        for (int i = 0; i < 9; i++) {
            value = grid[i][column1];
            grid[i][column1] = grid[i][column2];
            grid[i][column2] = value;
        }
    }

    private void shuffle3X3Rows() {
        for (int i = 0; i < 3; i++) {
            int num = random.nextInt(3);
            swap3X3Rows(i, num);
        }
    }

    private void swap3X3Rows(int row1, int row2) {
        for (int i = 0; i < 3; i++) {
            swapRows(row1 * 3 + i, row2 * 3 + i);
        }
    }

    private void shuffle3X3Cols() {
        for (int i = 0; i < 3; i++) {
            int num = random.nextInt(3);
            swap3X3Cols(i, num);
        }
    }

    private void swap3X3Cols(int column1, int column2) {
        for (int i = 0; i < 3; i++) {
            swapCols(column1 * 3 + i, column2 * 3 + i);
        }
    }
}