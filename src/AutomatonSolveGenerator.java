
public class AutomatonSolveGenerator extends GeneratorInterface{

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        FirstSafeRandomGenerator firstSafeRandomGenerator = new FirstSafeRandomGenerator();

        AutomatonSolver automatonSolver = new AutomatonSolver();

        Board board = null;

        int i = 0;
        while (i < 1000) {

            i++;

            board = firstSafeRandomGenerator.generate(x, y, minecount, startx, starty);
            Game game = new Game(board, minecount, startx, starty);

            if (automatonSolver.solve(game)){
                return board;
            }
        }
        return board;
    }
}
