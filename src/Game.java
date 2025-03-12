public class Game {
    private final Board board;

    private final int minecount;

    private final int startx;
    private final int starty;

    //private boolean gameover;

    private final GeneratorInterface[] generatorOptions = {new FirstSafeRandomGenerator(), new RandomGenerator(), new AutomatonSolveGenerator(), new CSPSubsetSolveGenerator()};
    private GeneratorInterface chosenGenerator = generatorOptions[0];

    public Game(int width, int height, int minecount, int startx, int starty, GeneratorInterface generatorInterface ) {
        this.board = generatorInterface.generate(width, height, minecount, startx, starty);
        this.minecount = minecount;
        this.startx = startx;
        this.starty = starty;
        this.chosenGenerator = generatorInterface;
    }

    public Game(Board board, int minecount, int startx, int starty) {
        this.board = board;
        this.minecount = minecount;
        this.startx = startx;
        this.starty = starty;
    }

    public Game(Board board, int minecount, int startx, int starty, GeneratorInterface generatorInterface) {
        this.board = board;
        this.minecount = minecount;
        this.startx = startx;
        this.starty = starty;
        this.chosenGenerator = generatorInterface;
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

    public GeneratorInterface getChosenGenerator() {
        return this.chosenGenerator;
    }

    public GeneratorInterface[] getGeneratorOptions() {
        return this.generatorOptions;
    }


//    public void setGameover(boolean gameover) {
//        this.gameover = gameover;
//    }

    public void setChosenGenerator(GeneratorInterface chosenGenerator) {
        this.chosenGenerator = chosenGenerator;
    }
}
