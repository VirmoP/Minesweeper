package generators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import solvers.CSPSolverSubsets;
import utils.*;

public class CSPSubsetSolveSafeMoveGenerator extends AbstractGenerator {

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        CSPSolverSubsets cspSolverSubsets = new CSPSolverSubsets();

        Board board;

        Board boardcopy = null;

        int i = 0;
        while (i < 100) {

            i++;

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);

            int j = 0;

            while(j < 25) {
                j++;

                Game game = new Game(board, minecount, startx, starty);

                boardcopy = new Board(board.intboard);

                SolveInfo info = cspSolverSubsets.solve(game);

                if (info.solved) {
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

                int i1 = constraints.keySet().size();
                for (Set<Tile> constraint : constraints.keySet()) {
                    if (0 < unrevealedTiles.size() - constraint.size() - minesLeft) {
                        chosenConstraint = constraint;
                        break;
                    }
                    i--;
                }
                //If no constraint was chosen generate a new random board
                if (i1 == 0)
                    break;

                unrevealedTiles.removeAll(chosenConstraint);

                int[][] intboard = board.intboard;

                //choose constraint that can be changed
                for (Tile tile : chosenConstraint)
                    if (tile.isMine()) {
                        Tile change = chooseRandomTile(unrevealedTiles);
                        unrevealedTiles.remove(change);
                        intboard[tile.getY()][tile.getX()] = 0;
                        intboard[change.getY()][change.getX()] = 9;
                    }


                for (int k = 0; k < intboard.length; k++) {
                    for (int l = 0; l < intboard[k].length; l++) {
                        if (intboard[k][l] != 9)
                            intboard[k][l] = 0;
                    }
                }


                boardFromMines(intboard);

                board = new Board(intboard);
            }

        }

        return boardcopy;
    }

    /**
     * Picks first mine from set
     * @param set given Set<utils.Tile>
     * @return chosen utils.Tile
     */
    public Tile chooseRandomTile(Set<Tile> set){
        for (Tile tile1: set) {
            if (tile1.isMine())
                return tile1;
        }
        return null;
    }
}
