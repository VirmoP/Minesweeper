public abstract class GeneratorInterface {
    /**
     * Method to implement a generation of a board where 0 = no mine,
     * 9 = mine and other integers represents how many mines are neighbouring
     * @param x width of board (int[y][x])
     * @param y height of board (int[y][x])
     * @param minecount number of mines on generated board
     * @return Return a Board object with the generated board.
     */
    abstract Board generate(int x, int y, int minecount, int startx, int starty);

    /**
     *Utility function to add 1 mine to all neighbours that aren't mines
     * @param board int[][] board
     * @param x coordinate of mine (int[y][x])
     * @param y coordinate of mine (int[y][x])
     */
    public void add1ToNeighbours(int[][] board, int x, int y) {
        for (int i = -1; i < 2; i++) {
            if ((y == 0 && i == -1) || (y == board.length - 1 && i == 1))
                continue;
            for (int j = -1; j < 2; j++) {
                if ((x == 0 && j == -1) || (x == board[0].length - 1 && j == 1))
                    continue;
                if (board[y + i][x + j] != 9)
                    board[y + i][x + j] += 1;
            }
        }
    }


    /**
     * Utility, When given a board with only mines in it (only 9s and 0s)
      * @param board int[][] board with only mines in it
     */
    public void boardFromMines(int[][] board){
        for (int i = 0; i < board.length; i++) {
            // Loop through all elements of current row
            for (int j = 0; j < board[i].length; j++)
                if (board[i][j] == 9){
                    add1ToNeighbours(board, j, i);
                }
        }
    }

    /**
     * Utility, When given a board with only mines in it (only 9s and 0s)
     * @param board Board object with only mines in it
     */
    public void boardFromMines(Board board){
        for (int i = 0; i < board.intboard.length; i++) {
            System.out.println();
            // Loop through all elements of current row
            for (int j = 0; j < board.intboard[i].length; j++)
                if (board.intboard[i][j] == 9){
                    add1ToNeighbours(board.intboard, j, i);
                }
        }
    }

    public boolean isMineInNeighbours(int[][] board, int x, int y){
        for (int i = -1; i < 2; i++) {
            if ((y == 0 && i == -1) || (y == board.length - 1 && i == 1))
                continue;
            for (int j = -1; j < 2; j++) {
                if ((x == 0 && j == -1) || (x == board[0].length - 1 && j == 1))
                    continue;
                if (board[y + i][x + j] == 9)
                    return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
