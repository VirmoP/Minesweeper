import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SolveTimer {
    /**
     * Class for generating solvable tables and timing them.
     */


    public static void main(String[] args) throws IOException {
        /*final GeneratorInterface[] generatorOptions = {new RandomGenerator(), new FirstSafeRandomGenerator(), new AutomatonSolveGenerator(), new AutomatonSolveMineMoveGenerator(), new AutomatonSolveSafeMoveGenerator(),
                                                        new SinglePointGenerator(), new SinglePointMineMoveGenerator(), new SinglePointSafeMoveGenerator(),
                                                        new CSPSolveGenerator(), new CSPSolveMineMoveGenerator(), new CSPSolveSafeMoveGenerator(),
                                                        new CSPSubsetSolveGenerator(), new CSPSubsetSolveMineMoveGenerator(), new CSPSubsetSolveSafeMoveGenerator()};

         */
        final AbstractGenerator[] generatorOptions = {new CSPSolveGenerator(), new CSPSolveMineMoveGenerator(), new CSPSolveSafeMoveGenerator(),
                new CSPSubsetSolveGenerator(), new CSPSubsetSolveMineMoveGenerator(), new CSPSubsetSolveSafeMoveGenerator()};

        double[][] startlocations = new double[5][2];
        startlocations[0] = new double[]{0,0};
        startlocations[1] = new double[]{0.25,0.25};
        startlocations[2] = new double[]{0.5,0.5};
        startlocations[3] = new double[]{0,0.5};
        startlocations[4] = new double[]{0.25,0.5};

        CSPSolverSubsets solver = new CSPSolverSubsets();
        Game game;
        Board board;
        String s = "";
        File file = new File("results_CSP.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter("results_CSP.txt");

        for (AbstractGenerator generator : generatorOptions){
            for (int percentage = 5; percentage < 51; percentage += 5) {
                for (int length = 10; length < 51; length += 5) {
                    for (int height = 10; height < 51; height += 5) {
                        for (double[] start: startlocations ){
                            for (int iteration = 0; iteration < 25; iteration++) {
                                System.gc();
                                int mines = (int) ((length*height)*((float)percentage/100.0));
                                int startx = (int) start[0]*length;
                                int starty = (int) start[1]*height;
                                long startTime = System.currentTimeMillis();
                                board = generator.generate(length,height, mines, startx, starty);
                                long endTime = System.currentTimeMillis();

                                game = new Game(board, mines, startx, starty);

                                s = endTime-startTime + " ms, " + generator + ", x = " + length + ", y = " + height + ", mines = " + mines + ", startloc " + Arrays.toString(start) + ", solved = " + solver.solve(game).solved + "\n";
                                writer.write(s);

                            }
                        }
                    }
                }
                System.out.println(s);
            }

        }
        writer.close();
    }
}
