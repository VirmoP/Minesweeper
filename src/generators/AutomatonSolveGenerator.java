package generators;

import solvers.AutomatonSolver;
import utils.*;

public class AutomatonSolveGenerator extends AbstractGenerator {

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        AutomatonSolver automatonSolver = new AutomatonSolver();

        Board board = null;

        //Generates a board until it is solvable by automaton solver, if not solved by 100 generations then returns unsolvable board.
        int i = 0;
        while (i < 100) {

            i++;

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);

            Board boardcopy = new Board(board.intboard);

            Game game = new Game(board, minecount, startx, starty);

            if (automatonSolver.solve(game).solved){
                return boardcopy;
            }
        }
        return board;
    }
}
