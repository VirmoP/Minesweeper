import generators.*;
import solvers.CSPSolverSubsets;
import utils.Board;
import utils.Game;

import java.io.*;
import java.util.Arrays;
public class SolveTimer {
    /**
     * Class for generating solvable tables and timing them.
     */

    public static void main(String[] args) throws IOException {
        CSPSubsetSolveMineMoveGenerator generator = new CSPSubsetSolveMineMoveGenerator();
        CSPSolverSubsets solver = new CSPSolverSubsets();
        Board board = null;
        Game game = null;
        String s = "";
        File file = new File("results_CSPsimple30percent.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter("results_CSPsimple30percent.txt");


        for (int percentage = 30; percentage < 31; percentage += 5) {
            for (int sizex = 10; sizex < 201; sizex += 5) {
                for (int sizey = sizex; sizey < 201; sizey += 5) {
                    for (int iteration = 0; iteration < 10; iteration++) {
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
                }
                System.out.println(s);
            }
        }
        writer.close();
    }

    public static void timer(String[] args) throws IOException {
        /*final GeneratorInterface[] generatorOptions = {new generators.RandomGenerator(), new generators.FirstSafeRandomGenerator(), new generators.AutomatonSolveGenerator(), new generators.AutomatonSolveMineMoveGenerator(), new generators.AutomatonSolveSafeMoveGenerator(),
                                                        new generators.SinglePointGenerator(), new generators.SinglePointMineMoveGenerator(), new generators.SinglePointSafeMoveGenerator(),
                                                        new generators.CSPSolveGenerator(), new generators.CSPSolveMineMoveGenerator(), new generators.CSPSolveSafeMoveGenerator(),
                                                        new generators.CSPSubsetSolveGenerator(), new generators.CSPSubsetSolveMineMoveGenerator(), new generators.CSPSubsetSolveSafeMoveGenerator()};

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
        String savetext = "";
        File file = new File("results_CSP1.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter("results_CSP1.txt");

        File savefile = new File("save.txt");


        int generatorsave = 0;
        int percentagesave = 5;
        int lengthsave = 10;
        int heightsave = 10;
        int startsave = 0;
        int iterationsave = 0;
        boolean usesave = true;

        if (!savefile.createNewFile()){
            BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
            String saveline = reader.readLine().strip();
            System.out.println(saveline);
            String[] info = saveline.split(",");
            generatorsave = Integer.parseInt(info[0]);
            percentagesave = Integer.parseInt(info[1]);
            lengthsave = Integer.parseInt(info[2]);
            heightsave = Integer.parseInt(info[3]);
            startsave = Integer.parseInt(info[4]);
            iterationsave = Integer.parseInt(info[5]);
            reader.close();
        }




        for (int generator = 0; generator < generatorOptions.length; generator++){
            if (usesave)
                generator = generatorsave;
            for (int percentage = 5; percentage < 51; percentage += 5) {
                if (usesave)
                    percentage = percentagesave;
                for (int length = 10; length < 51; length += 5) {
                    if (usesave)
                        length = lengthsave;
                    for (int height = 10; height < 51; height += 5) {
                        if (usesave)
                            height = heightsave;
                        for (int start = 0; start < startlocations.length; start++){
                            if (usesave)
                                start = startsave;
                            for (int iteration = 0; iteration < 25; iteration++) {
                                if (usesave)
                                    iteration = iterationsave;
                                usesave = false;
                                System.gc();
                                int mines = (int) ((length*height)*((float)percentage/100.0));
                                int startx = (int) startlocations[start][0]*length;
                                int starty = (int) startlocations[start][1]*height;
                                AbstractGenerator generator1 = generatorOptions[generator];
                                long startTime = System.currentTimeMillis();
                                board = generator1.generate(length,height, mines, startx, starty);
                                long endTime = System.currentTimeMillis();

                                game = new Game(board, mines, startx, starty);

                                s = endTime-startTime + " ms, " + generatorOptions[generator] + ", x = " + length + ", y = " + height + ", mines = " + mines + ", startloc " + Arrays.toString(startlocations[start]) + ", solved = " + solver.solve(game).solved + "\n";
                                writer.write(s);
                                savetext = generator + "," + percentage + "," + length + "," + height + "," + start + "," + iteration;
                                try {
                                    FileWriter writer1 = new FileWriter(savefile, false);
                                    writer1.write(savetext);
                                }catch (IOException ignored){}
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
