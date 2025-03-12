import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;

public class Window {


    private static final JFrame frame = new JFrame("minesweeper");

    private static boolean gameover;

    private static TileButton[][] buttons;

    private static Game game;

    private static final JPanel header = new JPanel();

    private static final JLabel counter = new JLabel();
    private static final JPanel playfield = new JPanel();

    private static final JButton resetButton = new JButton();

    private static final JButton solveButton = new JButton();

    private static final JPopupMenu generatorChoiceMenu = new JPopupMenu();

    static int width;
    static int heigth;
    static int tilewidth;
    static int tabsize;
    static int minecount;
    static int headersize;

    static boolean firstClick;
    public static void main(String[] args){
        startGame(32,18,100);
    }

    public static void makeWindow(int varx, int vary, int varminecount){
        frame.setSize(tilewidth*(varx+2), tilewidth* vary + tabsize + headersize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(header);
        frame.add(playfield);

        header.setBounds(0,0,tilewidth*(varx +1), headersize);
        playfield.setBounds(0,headersize,tilewidth*(varx +1), tilewidth* vary);

//        resetButton.setBounds(tilewidth*(varx -2), 10, tilewidth*3/2, headersize/2);
        resetButton.setPreferredSize(new Dimension(tilewidth*3, headersize/2));
        resetButton.setText("Reset");
        resetButton.addActionListener(e -> {
            playfield.removeAll();
            header.removeAll();
            startGame(varx,vary,varminecount);

            updateBoard();

            header.repaint();
            header.revalidate();

            playfield.repaint();
            playfield.revalidate();
        });


        AtomicBoolean solvecheck = new AtomicBoolean(false);
//        solveButton.setBounds(tilewidth*(varx -4), 10, tilewidth*3/2, headersize/2);
        solveButton.setPreferredSize(new Dimension(tilewidth*3, headersize/2));
        solveButton.setText("Solve");
        solveButton.addActionListener(e -> {
            SolverInterface[] solvers = {new AutomatonSolver(), new AutomatonSolverWithGuess(), new SinglePointSolver(), new CSPSolver(), new CSPSolverSubsets()};

            SolverInterface solver = solvers[4];
//          AutomatonSolver automatonSolver = new AutomatonSolver();
            boolean solve = solver.solve(game);

            System.out.println("Solved - " + solve);
            updateBoard();
        });

        createMenu();

        counter.setBounds(10,10, 100,30);
        counter.setText("Mines left - " + minecount);
        header.add(counter);
        header.add(resetButton);
        header.add(solveButton);

        header.setLayout(new FlowLayout());
        playfield.setLayout(new GridLayout(vary, varx));
        header.setVisible(true);
        playfield.setVisible(true);

        // using no layout managers
        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);
    }
    public static void startGame(int varx, int vary, int varminecount){
        width = varx;
        heigth = vary;
        minecount = varminecount;
        tilewidth = 30;
        tabsize = 40;
        headersize = 60;
        gameover = false;
        firstClick = true;

        buttons = new TileButton[vary][varx];

        if (game != null) {
            game = new Game(new Board(new int[vary][varx]), 0, 0, 0, game.getChosenGenerator());
        } else {
            game = new Game(new Board(new int[vary][varx]), 0, 0, 0);
        }
        createGrid(varx, vary, tilewidth);
        makeWindow(varx, vary, varminecount);
    }

    public static void createMenu(){
        generatorChoiceMenu.removeAll();
        generatorChoiceMenu.setLocation(130, 10);

        JLabel text = new JLabel(game.getChosenGenerator().toString());
        text.setPreferredSize(new Dimension(200,10));
        header.add(text);
        text.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                generatorChoiceMenu.show(header, text.getX(), text.getY());
            }
        });

        for (GeneratorInterface item : game.getGeneratorOptions()){
            JMenuItem menuitem = new JMenuItem(item.toString());
            menuitem.addActionListener(e -> {
                text.setText(menuitem.getText());
                game.setChosenGenerator(item);
            });
            generatorChoiceMenu.add(menuitem);
        }

        header.add(generatorChoiceMenu);
    }


    public static void createGrid(int x, int y, int tilewidth){
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                buttons[j][i] = new TileButton(game.getBoard().board[j][i]);

//                buttons[j][i].setBounds(i*tilewidth+3,j*tilewidth,tilewidth,tilewidth );
                buttons[j][i].setPreferredSize(new Dimension(tilewidth, tilewidth));
                //buttons[j][i].setBackground(new Color(23,93,207));

                buttons[j][i].setMargin(new Insets(0,0,0,0));

                //buttons[j][i].setText(i+", "+j);

                buttonLogistics(buttons[j][i]);

                playfield.add(buttons[j][i]);
            }
        }
    }

    public static void buttonLogistics(TileButton button){
        button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {

                if (firstClick){
                    firstClick = false;
                    buttons = new TileButton[heigth][width];
                    playfield.removeAll();
                    GeneratorInterface valitud = game.getChosenGenerator();

                    game = new Game(width, heigth,minecount, button.tile.x, button.tile.y, valitud);
                    
                    createGrid(width, heigth, tilewidth);
                    printmatrix(game.getBoard().intboard);

                    //mouseReleased(e);
                    //buttons[button.tile.y][button.tile.x].tile.revealWithNeighbours();
                    buttons[button.tile.y][button.tile.x].tile.setRevealed(true);
                    updateBoard();
                    return;
                }

                if (gameover) {
                    System.out.println(button.tile);
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (!button.tile.isRevealed()){
                        button.tile.setFlagged(!button.tile.isFlagged());
                        updateBoard();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getClickCount() == 2){
                        revealNeighbours(button);
                        updateBoard();
                    }
                    if (!button.tile.isFlagged()) {
                        button.tile.revealWithNeighbours();
                        updateBoard();
                    }
                }
            }
        });

    }

    private static void revealNeighbours(TileButton button) {
        for (Tile neighbour : button.tile.neighbours){
            if (!neighbour.isFlagged()){
                neighbour.setRevealed(true);
            }
        }
    }

    public static void unrevealedToMines(){
        for (TileButton[] row : buttons){
            for (TileButton element : row){
                if (!element.tile.isRevealed()) {
                    element.tile.setFlagged(true);
                    element.setIcon(new ImageIcon("src/images/flag.png"));
                }
            }
        }
    }


    public static void updateBoard(){
        int flaggedCount = 0;
        int revealed = 0;
        for (TileButton[] row : buttons) {
            for (TileButton element : row){
                updateButton(element);
                if (element.tile.isMine() && element.tile.isRevealed()) {
                    gameover = true;
                    counter.setText("Game lost");
                }
                if (element.tile.isFlagged())
                    flaggedCount++;
                if (element.tile.isRevealed())
                    revealed++;

            }
        }

        counter.setText("Mines left - " + (minecount-flaggedCount));

        if (revealed == width * heigth - minecount){
            counter.setText("Game Won");
            gameover = true;
            unrevealedToMines();
        }

    }

    public static void updateButton(TileButton button){
        if (!button.tile.isFlagged() && !button.tile.isRevealed())
            button.setIcon(null);
        if (button.tile.isFlagged())
            button.setIcon(new ImageIcon("src/images/flag.png"));
        if (button.tile.isRevealed()){

            if (button.tile.isMine())
                button.setIcon(new ImageIcon("src/images/mine.png"));
            else {
                button.setText(button.tile.minesInNeighbourhood + "");
                button.setBackground(new Color(255,255,255));
            }
        }

    }


    public static void printmatrix(int[][] matrix){
        // Loop through all rows
        for (int[] ints : matrix) {
            System.out.println();
            // Loop through all elements of current row
            for (int anInt : ints) System.out.print(anInt + " ");
        }
    }
}
