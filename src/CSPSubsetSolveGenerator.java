import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CSPSubsetSolveGenerator extends GeneratorInterface{

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        CSPSolverSubsets cspSolverSubsets = new CSPSolverSubsets();

        Board board = null;

        Board boardcopy = null;

        int i = 0;
        while (i < 1000) {

            i++;

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);
            Game game = new Game(board, minecount, startx, starty);

            boardcopy = new Board(board.intboard);

            SolveInfo info = cspSolverSubsets.solve(game);

            if (info.solved){
                return boardcopy;
            }

            Map<Set<Tile>, Integer> constraints = info.constraints;

            if (constraints.isEmpty())
                continue;


            int minesLeft = 0;
            Set<Tile> unrevealedTiles = new HashSet<>();
            for (Tile[] row: board.board){
                for (Tile tile: row){
                    if (!tile.isRevealed() && !tile.isFlagged()) {
                        unrevealedTiles.add(tile);
                        if (tile.isMine())
                            minesLeft++;
                    }
                }
            }

            Set<Tile> chosenConstraint = new HashSet<>();

            for (Set<Tile> constraint: constraints.keySet()){
                    chosenConstraint = constraint;
                    break;
            }

            unrevealedTiles.removeAll(chosenConstraint);

            int[][] intboard = board.intboard;

            if ( chosenConstraint.size() < minesLeft ){
                for (Tile tile : chosenConstraint)
                    if (!tile.isMine()){
                        Tile change = chooseRandomTile(unrevealedTiles);
                        intboard[tile.y][tile.x] = 9;
                        intboard[change.y][change.x] = 0;
                    }
            }

            for (int[] row: intboard){
                for (int number: row)
                    if (number != 9)
                        number = 0;
            }

            //TODO tee uue intboardiga uus board ja proovi uuesti solveda.
            

        }
        return boardcopy;
    }

    public Tile chooseRandomTile(Set<Tile> set){

        for (Tile tile1: set) {
            if (tile1.isMine())
                return tile1;
        }

        return null;
    }

}
