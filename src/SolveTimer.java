import generators.*;
import solvers.CSPSolverSubsets;
import utils.Board;
import utils.Game;

import java.io.*;


public class SolveTimer {
    /**
     * Class for generating solvable tables and timing them.
     */

    public static void main(String[] args) throws IOException {
        CSPSubsetSolveMineMoveGenerator generator = new CSPSubsetSolveMineMoveGenerator();
        CSPSolverSubsets solver = new CSPSolverSubsets();
        Board board;
        Game game;
        String s;
        File file = new File("results_expert.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter("results_expert.txt");

        int sum = 0;

        System.out.println("what");
        for (int i = 0; i < 1000; i++) {
            System.gc();
            long startTime = System.currentTimeMillis();
            board = generator.generate(30,16,99,15,8);
            long endTime = System.currentTimeMillis();

            game = new Game(board, 99, 15, 8);
            s = endTime - startTime + " ms, solved = " + solver.solve(game).solved + "\n";

            sum += (int) (endTime-startTime);
            System.out.println(i);
            writer.write(s);
        }

        System.out.println(sum/1000.0);



        /*
        for (int percentage = 5; percentage < 21; percentage += 5) {
            for (int sizex = 10; sizex < 101; sizex += 10) {
                for (int sizey = sizex; sizey < 101; sizey += 10) {
                    for (int iteration = 0; iteration < 5; iteration++) {
                        System.gc();
                        int mines = (int) ((sizey * sizex) * ((float) percentage / 100.0));
                        int startx = (int) (sizex * 0.5);
                        int starty = (int) (sizey * 0.5);
                        long startTime = System.currentTimeMillis();
                        board = generator.generate(sizex, sizey, mines, startx, starty);
                        long endTime = System.currentTimeMillis();

                        game = new Game(board, mines, startx, starty);

                        s = endTime - startTime + " ms, " + generator + ",  x = " + sizex + ", y = " + sizey+ ", mines = " + mines + ", solved = " + solver.solve(game).solved + "\n";
                        writer.write(s);
                    }
                    System.out.println("res - " + s);
                }
                System.out.println(s);
            }
        }

         */
        writer.close();
    }
}
