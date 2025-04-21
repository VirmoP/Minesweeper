package generators;

import solvers.SinglePointSolver;
import utils.*;

public class SinglePointGenerator extends AbstractGenerator {

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        SinglePointSolver singlePointSolver = new SinglePointSolver();

        Board board = null;

        //Generates a board until it is solvable by single point solver, if not solved by 100 generations then returns unsolvable board.
        int i = 0;
        while (i < 100) {

            i++;

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);

            Board boardcopy = new Board(board.intboard);

            Game game = new Game(board, minecount, startx, starty);

            if (singlePointSolver.solve(game).solved){
                return boardcopy;
            }
        }
        return board;
    }
}
