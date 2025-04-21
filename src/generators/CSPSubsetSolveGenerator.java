package generators;

import solvers.CSPSolverSubsets;
import utils.*;

public class CSPSubsetSolveGenerator extends AbstractGenerator {

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

            boardcopy = new Board(board.intboard);

            if (cspSolverSubsets.solve(new Game(board, minecount, startx, starty)).solved){
                return boardcopy;
            }
        }
        return boardcopy;
    }

}
