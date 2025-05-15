package solvers;

import utils.Game;
import utils.SolveInfo;

public interface SolverInterface {
    /**
     * Method that acts on a game object. Change happens to tiles and their revealed status.
     *
     * @param game given utils.Game object
     * @return boolean of solved status. If game is solved true, otherwise false.
     */
    SolveInfo solve(Game game);
}
