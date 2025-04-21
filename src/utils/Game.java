package utils;

import generators.*;

public class Game {
    private final Board board;

    private final int minecount;

    private final int startx;
    private final int starty;

    //private boolean gameover;

    private final AbstractGenerator[] generatorOptions = {new FirstSafeRandomGenerator(), new RandomGenerator(), new AutomatonSolveGenerator(), new AutomatonSolveMineMoveGenerator(), new CSPSubsetSolveGenerator() , new CSPSubsetSolveMineMoveGenerator()};
    private AbstractGenerator chosenGenerator = generatorOptions[0];

    public Game(int width, int height, int minecount, int startx, int starty, AbstractGenerator abstractGenerator) {
        this.board = abstractGenerator.generate(width, height, minecount, startx, starty);
        this.minecount = minecount;
        this.startx = startx;
        this.starty = starty;
        this.chosenGenerator = abstractGenerator;
    }

    public Game(Board board, int minecount, int startx, int starty) {
        this.board = board;
        this.minecount = minecount;
        this.startx = startx;
        this.starty = starty;
    }

    public Game(Board board, int minecount, int startx, int starty, AbstractGenerator abstractGenerator) {
        this.board = board;
        this.minecount = minecount;
        this.startx = startx;
        this.starty = starty;
        this.chosenGenerator = abstractGenerator;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getMinecount() {
        return this.minecount;
    }

    public int getStartx() {
        return this.startx;
    }

    public int getStarty() {
        return this.starty;
    }

    public AbstractGenerator getChosenGenerator() {
        return this.chosenGenerator;
    }

    public AbstractGenerator[] getGeneratorOptions() {
        return this.generatorOptions;
    }

    public void setChosenGenerator(AbstractGenerator chosenGenerator) {
        this.chosenGenerator = chosenGenerator;
    }
}
