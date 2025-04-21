package generators;

import solvers.AutomatonSolver;
import utils.*;

public class AutomatonSolveSafeMoveGenerator extends AbstractGenerator {

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        AutomatonSolver automatonSolver = new AutomatonSolver();

        Board board;
        Board boardcopy = null;
        Game game;

        //Generates a board until it is solvable by automaton solver, if not solved by 100 generations then returns unsolvable board.
        int i = 0;
        while (i < 100) {

            i++;

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);

            //Tries moving safes 25 times and checks if solvable
            int j = 0;
            while (j < 25) {
                j++;

                boardcopy = new Board(board.intboard);
                game = new Game(board, minecount, startx, starty);

                if (automatonSolver.solve(game).solved) {
                    return boardcopy;
                }

                if (j == 25)
                    break;

                board = moveSafe(board);
                if (boardcopy.equals(board))
                    break;

            }
        }
        return boardcopy;
    }

}
