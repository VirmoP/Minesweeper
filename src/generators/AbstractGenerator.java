package generators;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import utils.*;

public abstract class AbstractGenerator {
    /**
     * Method to implement a generation of a board where 0 = no mine,
     * 9 = mine and other integers represents how many mines are neighbouring
     * @param x width of board (int[y][x])
     * @param y height of board (int[y][x])
     * @param minecount number of mines on generated board
     * @return Return a utils.Board object with the generated board.
     */
    public abstract Board generate(int x, int y, int minecount, int startx, int starty);

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
     * Utility that picks a mine from border of known and unknown tiles and swaps it with an unknows safe tile.
     * @param board utils.Board object where to move mines
     * @return changed utils.Board if possible
     */
    public Board moveMines(Board board){
        Set<Tile> unrevealedSafe = new HashSet<>();
        Set<Tile> unrevealedMines = new HashSet<>();
        Set<Tile> minesWithRevealedNeighbours = new HashSet<>();

        for (Tile[] row: board.board){
            for (Tile tile: row){
                if (!tile.isRevealed()){
                    if (!tile.isFlagged() && tile.isMine())
                        unrevealedMines.add(tile);
                    else if (!tile.isMine()) {
                        unrevealedSafe.add(tile);
                    }
                }
            }
        }

        for (Tile tile : unrevealedMines){
            for (Tile neighbour : tile.getNeighbours()){
                if (neighbour.isRevealed()) {
                    minesWithRevealedNeighbours.add(tile);
                    break;
                }
            }
        }

        if (minesWithRevealedNeighbours.isEmpty())
            return board;

        Tile chosenMine = chooseRandomTile(minesWithRevealedNeighbours);
        Tile chosenSafe = chooseRandomTile(unrevealedSafe);

        int[][] intboard = board.intboard;

        intboard[chosenMine.getY()][chosenMine.getX()] = 0;
        intboard[chosenSafe.getY()][chosenSafe.getX()] = 9;

        for (int k = 0; k < intboard.length; k++) {
            for (int l = 0; l < intboard[k].length; l++) {
                if (intboard[k][l] != 9)
                    intboard[k][l] = 0;
            }
        }

        boardFromMines(intboard);

        return new Board(intboard);

    }

    /**
     * Method that picks a safe from border of known and unknown tiles, swaps with mine from unrevealed tiles.
     * @param board given utils.Board
     * @return changed utils.Board if possible
     */
    public Board moveSafe(Board board){
        Set<Tile> unrevealedSafe = new HashSet<>();
        Set<Tile> unrevealedMines = new HashSet<>();
        Set<Tile> safesWithRevealedNeighbours = new HashSet<>();

        for (Tile[] row: board.board){
            for (Tile tile: row){
                if (!tile.isRevealed()){
                    if (!tile.isFlagged() && tile.isMine())
                        unrevealedMines.add(tile);
                    else if (!tile.isMine()) {
                        unrevealedSafe.add(tile);
                    }
                }
            }
        }

        for (Tile tile : unrevealedSafe){
            for (Tile neighbour : tile.getNeighbours()){
                if (neighbour.isRevealed()) {
                    safesWithRevealedNeighbours.add(tile);
                    break;
                }
            }
        }

        if (safesWithRevealedNeighbours.isEmpty())
            return board;

        Tile chosenSafe = chooseRandomTile(safesWithRevealedNeighbours);
        Tile chosenMine = chooseRandomTile(unrevealedMines);

        int[][] intboard = board.intboard;

        intboard[chosenMine.getY()][chosenMine.getX()] = 0;
        intboard[chosenSafe.getY()][chosenSafe.getX()] = 9;

        for (int k = 0; k < intboard.length; k++) {
            for (int l = 0; l < intboard[k].length; l++) {
                if (intboard[k][l] != 9)
                    intboard[k][l] = 0;
            }
        }

        boardFromMines(intboard);

        return new Board(intboard);

    }

    /**
     * Method that picks a random utils.Tile from given set of Tiles
     * @param set given Set<utils.Tile>
     * @return picked utils.Tile
     */

    public Tile chooseRandomTile(Set<Tile> set){
        if (set.isEmpty())
            return null;
        Random rand = new Random();
        int i = rand.nextInt(set.size());

        for (Tile tile1: set) {
            i--;
            if (i <= 0)
                return tile1;
        }
        return null;
    }

    @Override
    public String toString() {
        return getClass().getName().split("\\.")[1];
    }
}
