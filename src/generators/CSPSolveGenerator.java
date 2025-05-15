package generators;

import solvers.CSPSolver;
import utils.*;

public class CSPSolveGenerator extends AbstractGenerator {

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        CSPSolver cspSolver = new CSPSolver();

        Board board;

        Board boardcopy = null;

        //Generates a board until it is solvable by CSPsolver, if not solved by 100 generations then returns unsolvable board.
        int i = 0;
        while (i < 100) {

            i++;

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);

            boardcopy = new Board(board.intboard);

            if (cspSolver.solve(new Game(board, minecount, startx, starty)).solved){
                return boardcopy;
            }
        }
        return boardcopy;
    }
}
