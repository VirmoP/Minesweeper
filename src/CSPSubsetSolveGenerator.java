import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CSPSubsetSolveGenerator extends GeneratorInterface{

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        CSPSolverSubsets cspSolverSubsets = new CSPSolverSubsets();

        Board board;

        Board boardcopy = null;

        int i = 0;
        while (i < 1000) {

            i++;
            System.out.println("i - " + i);

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);

            int j = 0;

            while(j < 50) {
                j++;
                System.out.println("j - " + j);

                Game game = new Game(board, minecount, startx, starty);

                boardcopy = new Board(board.intboard);

                SolveInfo info = cspSolverSubsets.solve(game);

                if (info.solved) {
                    System.out.println("Solved it");
                    return boardcopy;
                }

                Map<Set<Tile>, Integer> constraints = info.constraints;

                if (constraints.isEmpty())
                    break;

                int minesLeft = 0;
                Set<Tile> unrevealedTiles = new HashSet<>();

                for (Tile[] row : board.board) {
                    for (Tile tile : row) {
                        if (!tile.isRevealed() && !tile.isFlagged()) {
                            unrevealedTiles.add(tile);
                            if (tile.isMine())
                                minesLeft++;
                        }
                    }
                }

                Set<Tile> chosenConstraint = new HashSet<>();

                for (Set<Tile> constraint : constraints.keySet()) {
                    chosenConstraint = constraint;
                    break;
                }

                unrevealedTiles.removeAll(chosenConstraint);

                int[][] intboard = board.intboard;

                if (chosenConstraint.size() < minesLeft - constraints.get(chosenConstraint)) {
                    for (Tile tile : chosenConstraint)
                        if (!tile.isMine()) {
                            Tile change = chooseRandomTile(unrevealedTiles);
                            unrevealedTiles.remove(change);
                            System.out.println("-----");
                            System.out.println(tile);
                            System.out.println(change);
                            System.out.println("------");
                            intboard[tile.y][tile.x] = 9;
                            intboard[change.y][change.x] = 0;
                        }
                }

                int miinid = 0;

                for (int k = 0; k < intboard.length; k++) {
                    for (int l = 0; l < intboard[k].length; l++) {
                        if (intboard[k][l] != 9)
                            intboard[k][l] = 0;
                        else
                            miinid++;
                    }
                }
                System.out.println("miinid " + miinid);

                boardFromMines(intboard);

                board = new Board(intboard);
            }

        }
        System.out.println("was not solved");
        return boardcopy;
    }

    public Tile chooseRandomTile(Set<Tile> set){
        for (Tile tile1: set) {
            if (tile1.isMine())
                return tile1;
        }

        return null;
    }

    public static void printmatrix(int[][] matrix){
        // Loop through all rows
        for (int[] ints : matrix) {
            System.out.println();
            // Loop through all elements of current row
            for (int anInt : ints) System.out.print(anInt + " ");
        }
    }

}
