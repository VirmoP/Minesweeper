package generators;

import solvers.SinglePointSolver;
import utils.*;

public class SinglePointSafeMoveGenerator extends AbstractGenerator {

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        SinglePointSolver singlePointSolver = new SinglePointSolver();

        Board board = null;
        Board boardcopy;
        Game game;

        //Generates a board until it is solvable by single point solver, if not solved by 100 generations then returns unsolvable board.
        int i = 0;
        while (i < 100) {

            i++;
            int j = 0;

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);

            //tries moving safe spots 25 times
            while (j < 25) {
                j++;
                boardcopy = new Board(board.intboard);

                game = new Game(board, minecount, startx, starty);

                if (singlePointSolver.solve(game).solved) {
                    return boardcopy;
                }

                if (j == 25)
                    break;

                board = moveSafe(board);
                if (boardcopy.equals(board))
                    break;
            }
        }
        return board;
    }
}
